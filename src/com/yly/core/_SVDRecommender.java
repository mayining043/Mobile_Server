package com.yly.core;

import java.text.DecimalFormat;
import java.util.List;

import org.apache.mahout.cf.taste.impl.recommender.svd.ALSWRFactorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import com.yly.util.GetDataModel;

/**
 * 用SVD算法做推荐
 */
public class _SVDRecommender {

	private List<RecommendedItem> recommendations;
	private StringBuffer recList;

	/**
	 * 调用SVD算法生成推荐，推荐结果可以通过getRecommendation()获取
	 * 
	 * @param 用户ID
	 * @param 推荐物品总数
	 * @throws Exception
	 */
	public _SVDRecommender(String user_id, int recommendNum) throws Exception {
		long id = Integer.parseInt(user_id);

		DataModel model = new GetDataModel().getDataModel();

		/*
		 * SVD推荐器，参数全部使用默认参数
		 */
		SVDRecommender recommender = new SVDRecommender(model, new ALSWRFactorizer(model, 10, 0.05, 10));

		recommendations = recommender.recommend(id, recommendNum);
	}

	/**
	 * 获取推荐结果
	 * 
	 * @return String型字符串，格式为“被推荐物品1ID，被推荐物品1预测评分；......”
	 */
	public String getRecommendation() {
		/*
		 * 预测的评分保留位数太多，不好看
		 */
		DecimalFormat df = new DecimalFormat("######0.0");
		recList = new StringBuffer("");// StringBuffer为空好像会出错
		double rating;
		if (recommendations != null) {
			for (RecommendedItem item : recommendations) {
				/*
				 * SVD预测评分的时候，有时会出现评分>5的现象，这里做个限制
				 */
				rating = item.getValue() > 5 ? 5.0 : item.getValue();
				/*
				 * 输出格式
				 */
				recList.append(item.getItemID() + "," + df.format(rating) + ";");
			}
		}

		else
			recList.append("Sorry, we can not make recommendations!");

		return recList.toString();
	}

	public static void main(String[] args) throws Exception {
		_SVDRecommender r = new _SVDRecommender("15", 5);
		String t = r.getRecommendation();
		System.out.println(t);
	}

}
