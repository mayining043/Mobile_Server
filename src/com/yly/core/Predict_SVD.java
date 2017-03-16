package com.yly.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.cf.taste.impl.recommender.svd.ALSWRFactorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import com.yly.dao.ItemDao;
import com.yly.dao.impl.ItemDaoImpl;
import com.yly.util.GetDataModel;

/**
 * 用svd算法进行推荐，返回的推荐列表保存在String中，格式为“item_id，预测评分；item_id，预测评分”
 */
public class Predict_SVD {
	private int user_id;// 目标用户id
	private ItemDao dao = new ItemDaoImpl();
	private ArrayList<Integer> item_list = dao.getAllItem();// 待推荐物品列表,默认为全体item
	private int recommendNums = 3;// 推荐物品数量

	public Predict_SVD(String user_id, ArrayList<Integer> item_list, int recommendNums) {
		this.user_id = Integer.parseInt(user_id);
		if (item_list != null) {
			this.item_list = item_list;
		}
		if (recommendNums >= 0)
			this.recommendNums = recommendNums;
	}

	// 根据候选物品列表、当前用户id和推荐数量获得推荐。
	public String getRecommendation() throws Exception {
		StringBuffer recList = new StringBuffer();

		DataModel model = new GetDataModel().getDataModel();
		SVDRecommender recommender = new SVDRecommender(model, new ALSWRFactorizer(model, 10, 0.05, 10));

		List<RecommendedItem> recommendations = recommender.recommend(user_id, recommendNums);

		for (RecommendedItem recItem : recommendations) {
			int item_id = (int) recItem.getItemID();
			// 如果推荐的物品在候选物品列表中
			if (item_list.contains(item_id)) {
				double rate = recItem.getValue();
				String item_rating = String.valueOf(rate > 5 ? 5 : rate);
				recList.append(item_id + "," + item_rating + ";");
			}
		}
		return recList.toString();
	}

	public static void main(String[] args) throws Exception {
		String user_id = "1";

		String result = new Predict_SVD(user_id, null, 3).getRecommendation();
		if (result.isEmpty()) {
			System.out.println("can not recommend");
		} else {
			System.out.println(result);
		}
	}

}
