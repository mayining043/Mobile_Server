package com.yly.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yly.dao.DataDao;
import com.yly.dao.ItemDao;
import com.yly.dao.impl.DataDaoImpl;
import com.yly.dao.impl.ItemDaoImpl;

import net.librec.common.LibrecException;
import net.librec.math.structure.DenseMatrix;
import net.librec.math.structure.MatrixEntry;
import net.librec.math.structure.SparseMatrix;

/**
 * 对BiasedMF进行改进，引入上下文
 */
public class Predict_CAMFB {
	private int user_id;// 目标用户id
	private ItemDao dao = new ItemDaoImpl();
	private ArrayList<Integer> item_list = dao.getAllItem();// 待推荐物品列表,默认为全体item
	private int recommendNums = 3;// 推荐物品数量，默认为3个
	private int[] currCon;//当前上下文

	public Predict_CAMFB(String user_id, ArrayList<Integer> item_list, int recommendNums,int[] currCon) {
		this.user_id = Integer.parseInt(user_id);
		if (item_list != null) {
			this.item_list = item_list;
		}
		if (recommendNums >= 0)
			this.recommendNums = recommendNums;
		this.currCon=currCon;
	}

	// 根据候选物品列表、当前用户id和推荐数量获得推荐。
	public String getRecommendation() throws Exception {
		String recList = new String();
		ContextMF recommender = new ContextMF(user_id, item_list, recommendNums,currCon);
		recList = recommender.getRecommendation();
		return recList;
	}

	public static void main(String[] args) throws Exception {
		String user_id = "1";

		String result = new Predict_CAMFB(user_id, null, 3,null).getRecommendation();
		if (result.isEmpty()) {
			System.out.println("can not recommend");
		} else {
			System.out.println(result);
		}
	}
}

class ContextMF extends BiasedMF {
	private int user_id;// 目标用户id
	private ArrayList<Integer> item_list;
	private int recommendNums;
	private DataDao dao = new DataDaoImpl();
	// item-context biases
	protected DenseMatrix[] B;
	// number of context
	public static int numContext;
	// context matrix
	public static SparseMatrix[] contextMatrix;
	// number of each context's condition
	public static int[] numCondition;
	private int[] currCon;

	ContextMF(int user_id, ArrayList<Integer> item_list, int recommendNums,int[] currCon) {
		this.user_id = user_id;
		this.item_list = item_list;
		this.recommendNums = recommendNums;
		trainMatrix = dao.readData();
		contextMatrix = dao.readContextData();
		numContext = contextMatrix.length;
		this.currCon=currCon;
		if(null==this.currCon){
			this.currCon=new int[numContext];
		}
		numCondition=dao.getNumContextCondition();
	}

	@Override
	protected void setup() throws LibrecException {
		super.setup();
		B = new DenseMatrix[numContext];
		for (int i = 0; i < numContext; i++) {
			B[i] = new DenseMatrix(numItems, numCondition[i]);
			B[i].init();
		}
	}

	@Override
	protected void trainModel() throws LibrecException {

		for (int iter = 1; iter <= numIterations; iter++) {

			loss = 0;
			for (MatrixEntry me : trainMatrix) {

				int u = me.row(); // user
				int j = me.column(); // item
				double ruj = me.get();

				double pred = predict(u, j, false);
				double euj = ruj - pred;

				loss += euj * euj;

				// update factors
				double bu = userBiases.get(u);
				double sgd = euj - regBias * bu;
				userBiases.add(u, regBias * sgd);

				loss += regBias * bu * bu;

				double bj = itemBiases.get(j);
				sgd = euj - regBias * bj;
				itemBiases.add(j, learnRate * sgd);

				loss += regBias * bj * bj;

				for (int f = 0; f < numFactors; f++) {
					double puf = userFactors.get(u, f);
					double qjf = itemFactors.get(j, f);

					double delta_u = euj * qjf - regUser * puf;
					double delta_j = euj * puf - regItem * qjf;

					userFactors.add(u, f, learnRate * delta_u);
					itemFactors.add(j, f, learnRate * delta_j);

					loss += regUser * puf * puf + regItem * qjf * qjf;
				}
				// add item-context bias
				for (int k = 0; k < numContext; k++) {
					int context = (int) contextMatrix[k].get(u, j) - 1;
					if (context >= 0) {
						int innderContext=dao.getInnderContextId(context,k);
						double bjc = B[k].get(j, innderContext);
						sgd = euj - regBias * bjc;
						B[k].add(j, innderContext, learnRate * sgd);
					}
				}
			}
			loss *= 0.5;

			if (isConverged(iter))
				break;

		} // end of training

	}

	@Override
	protected double predict(int u, int j) {
		double bjc = 0;
		for (int i = 0; i < numContext; i++) {
			int context = (int) contextMatrix[i].get(u, j);
			if (context > 0) {
				bjc += B[i].get(j, dao.getInnderContextId(context, i));
			}
		}
		return globalMean + userBiases.get(u) + itemBiases.get(j) + DenseMatrix.rowMult(userFactors, u, itemFactors, j)
				+ bjc;
	}
	/**
	 * get prediction for current user and item in current condition
	 * */
	protected double predict_curr(int u, int j,int curr_con[]) {
		u=dao.getInnerUserId(u);
		j=dao.getInnderItemId(j);
		double bjc = 0;
		for (int i = 0; i < numContext; i++) {
			int context = dao.getInnderContextId(curr_con[i], i);
			if (context > 0) {
				bjc += B[i].get(j, context - 1);
			}
		}
		return globalMean + userBiases.get(u) + itemBiases.get(j) + DenseMatrix.rowMult(userFactors, u, itemFactors, j)
				+ bjc;
	}
	public String getRecommendation() throws LibrecException {
		setup();
		trainModel();
		StringBuffer recList = new StringBuffer();
		HashMap<Integer, Double> init_list = new HashMap<>();
		for (int item_id : item_list) {
			if (0 == dao.getInnderItemId(item_id))
				continue;
			double rate = predict_curr(user_id, item_id,currCon);
			// 保留1位小数
			BigDecimal b = new BigDecimal(rate);
			rate = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
			Double item_rating =rate > 5 ? 5 : rate;
			init_list.put(item_id, item_rating);
		}
		//对候选推荐结果排序
		List<Map.Entry<Integer, Double>> list=dao.sortHashMapByValue(init_list);
		for(int i=0;i<recommendNums&&!list.isEmpty();i++){
			int item=list.get(i).getKey();
			double rate=list.get(i).getValue();
			recList.append(item + "," + rate + ";");
			list.remove(i);
		}
		return recList.toString();
	}
}