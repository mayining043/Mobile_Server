package com.yly.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.yly.dao.DataDao;
import com.yly.dao.ItemDao;
import com.yly.dao.impl.DataDaoImpl;
import com.yly.dao.impl.ItemDaoImpl;

import net.librec.common.LibrecException;
import net.librec.math.structure.DenseMatrix;
import net.librec.math.structure.DenseVector;
import net.librec.recommender.cf.rating.BiasedMFRecommender;
/**
 * 用LibRec中提供的BiasedMF进行推荐 */
public class Predict_BiaseMF {
	private int user_id;// 目标用户id
	private ItemDao dao = new ItemDaoImpl();
	private ArrayList<Integer> item_list = dao.getAllItem();// 待推荐物品列表,默认为全体item
	private int recommendNums = 3;// 推荐物品数量，默认为3个

	public Predict_BiaseMF(String user_id, ArrayList<Integer> item_list, int recommendNums) {
		this.user_id = Integer.parseInt(user_id);
		if (item_list != null) {
			this.item_list = item_list;
		}
		if (recommendNums >= 0)
			this.recommendNums = recommendNums;
	}

	// 根据候选物品列表、当前用户id和推荐数量获得推荐。
	public String getRecommendation() throws Exception {
		String recList = new String();
		BiasedMF recommender = new BiasedMF(user_id, item_list, recommendNums);
		recList = recommender.getRecommendation();
		return recList;
	}

	public static void main(String[] args) throws Exception {
		String user_id = "1";

		String result = new Predict_BiaseMF(user_id, null, 3).getRecommendation();
		if (result.isEmpty()) {
			System.out.println("can not recommend");
		} else {
			System.out.println(result);
		}
	}
}

class BiasedMF extends BiasedMFRecommender {
	private int user_id;// 目标用户id
	private ArrayList<Integer> item_list;
	private int recommendNums;
	private DataDao dao = new DataDaoImpl();
	public BiasedMF() {
		// TODO Auto-generated constructor stub
	}
	BiasedMF(int user_id, ArrayList<Integer> item_list, int recommendNums) {
		this.user_id = user_id;
		this.item_list = item_list;
		this.recommendNums = recommendNums;
		trainMatrix = dao.readData();
	}

	@Override
	protected void setup() throws LibrecException {
		earlyStop = false;
		verbose = true;

		trainMatrix = dao.readData();

		numUsers = trainMatrix.numRows();
		numItems = trainMatrix.numColumns();
		numRates = trainMatrix.size();
		ratingScale = new ArrayList<>(trainMatrix.getValueSet());
		Collections.sort(ratingScale);
		maxRate = Collections.max(trainMatrix.getValueSet());
		minRate = Collections.min(trainMatrix.getValueSet());
		globalMean = trainMatrix.sum() / numRates;
		numIterations = 20;
		learnRate = 0.01f;
		maxLearnRate = 1000.0f;

		regUser = 0.01f;
		regItem = 0.01f;

		numFactors = 10;
		isBoldDriver = false;
		decay = 1.0f;

		userFactors = new DenseMatrix(numUsers, numFactors);
		itemFactors = new DenseMatrix(numItems, numFactors);
		globalMean = trainMatrix.mean();

		initMean = 0.0f;
		initStd = 0.1f;

		// initialize factors
		userFactors.init(initMean, initStd);
		itemFactors.init(initMean, initStd);

		regBias = 0.01;

		// initialize the userBiased and itemBiased
		userBiases = new DenseVector(numUsers);
		itemBiases = new DenseVector(numItems);

		userBiases.init(initMean, initStd);
		itemBiases.init(initMean, initStd);
	}

	public String getRecommendation() throws LibrecException {
		setup();
		trainModel();
		StringBuffer recList = new StringBuffer();
		HashMap<Integer, String> init_list = new HashMap<>();
		for (int item_id : item_list) {
			if(0==dao.getInnderItemId(item_id))
				continue;
			double rate = predict(dao.getInnerUserId(user_id), dao.getInnderItemId(item_id));
			// 保留1位小数
			BigDecimal b = new BigDecimal(rate);
			rate = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
			String item_rating = String.valueOf(rate > 5 ? 5 : rate);
			init_list.put(item_id, item_rating);
		}
		int count = 0;
		Set<Integer> keys = init_list.keySet();
		Iterator<Integer> iterator = keys.iterator();
		while (iterator.hasNext() && count < recommendNums) {
			int item = iterator.next();
			recList.append(item + "," + init_list.get(item) + ";");
			count++;
		}
		return recList.toString();
	}
}
