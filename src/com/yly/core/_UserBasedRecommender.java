package com.yly.core;

import java.util.List;

import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.*;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.*;

import com.yly.dao.ItemDao;
import com.yly.dao.impl.ItemDaoImpl;
import com.yly.entity.Item;
import com.yly.util.GetDataModel;

/**
 * 常规的基于用户推荐算法
 * 
 */
public class _UserBasedRecommender {

	private List<RecommendedItem> recommendations;
	private StringBuffer recList;

	/**
	 * 用传统的基于用户的推荐算法，邻居数为3，推荐结果可以通过getRecommendation()获取
	 * 
	 * @param user_id
	 *            用户ID
	 * @param recommendNum
	 *            推荐物品总数
	 * @param mode
	 *            选择相似度算法，1为皮尔森，2为欧几里得，默认为欧几里得
	 * @throws TasteException
	 * @throws Exception
	 */
	public _UserBasedRecommender(long user_id, int recommendNum, int mode) throws TasteException, Exception {
		DataModel model = new GetDataModel().getDataModel();

		UserSimilarity similarity;

		if (mode == 1)
			similarity = new PearsonCorrelationSimilarity(model);
		else
			similarity = new EuclideanDistanceSimilarity(model);

		UserNeighborhood neighborhood = new NearestNUserNeighborhood(3, similarity, model);

		Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

		recommendations = recommender.recommend(user_id, recommendNum);
	}

	public _UserBasedRecommender(long user_id, int recommendNum) throws Exception {
		this(user_id, recommendNum, 2);
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
		// DecimalFormat df = new DecimalFormat("######0.0");
		recList = new StringBuffer("");// StringBuffer为空好像会出错
		// double rating ;
		ItemDao dao = new ItemDaoImpl();
		for (RecommendedItem item : recommendations) {
			/*
			 * 预测评分的时候，有时会出现评分>5的现象，这里做个限制
			 */
			// rating = item.getValue() > 5 ? 5.0 : item.getValue();
			/*
			 * 输出格式
			 */
			int item_id = (int) item.getItemID();
			Item itemEntity = dao.search_itemInfo(item_id);
			// recList.append(item_id+","+df.format(rating)+";");
			recList.append(itemEntity.getItem_name() + "," + itemEntity.getItem_address() + ";");
		}
		return recList.toString();
	}

}
