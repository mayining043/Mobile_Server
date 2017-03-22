package com.yly.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yly.dao.ItemDao;
import com.yly.dao.impl.ItemDaoImpl;
import com.yly.entity.Item;

/**
 * Servlet implementation class GetUserFeedbackServlet
 */
@WebServlet("/GetUserFeedbackServlet")
public class GetUserFeedbackServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetUserFeedbackServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();

		ItemDao dao = new ItemDaoImpl();

		// 获得客户端参数
		int user_id = Integer.parseInt(request.getParameter("user_id"));
		double rating = Double.parseDouble(request.getParameter("rating"));
		double longitude = Double.parseDouble(request.getParameter("longitude"));
		double latitude = Double.parseDouble(request.getParameter("latitude"));
		int time = Integer.parseInt(request.getParameter("time"));
		int weather = Integer.parseInt(request.getParameter("weather"));
		int season = Integer.parseInt(request.getParameter("season"));
		int daytype = Integer.parseInt(request.getParameter("daytype"));
		// item parameters
		String item_name = request.getParameter("item_name");
		System.out.println(dao.search_itemId(item_name));
		// 如果物品没有添加过到数据库里
		if (-1 == dao.search_itemId(item_name)) {
			String item_address = request.getParameter("item_address");
			double item_longitude = Double.parseDouble(request.getParameter("item_longitude"));
			double item_latitude = Double.parseDouble(request.getParameter("item_latitude"));

			Item item = new Item();
			item.setItem_name(item_name);
			item.setItem_address(item_address);
			item.setLatitude(item_latitude);
			item.setLongitude(item_longitude);
			dao.addItem(item);
	
		}

		int item_id = dao.search_itemId(item_name);
		int addFeedbackResult = dao.addUserFeedback(user_id, item_id, rating, latitude, longitude, time, daytype,
				weather, season);

		if (addFeedbackResult > 0) {
			out.print("评分提交成功!");
		} else {
			out.print("评分提交失败！");
		}
		out.flush();
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
