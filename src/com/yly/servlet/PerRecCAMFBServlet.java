package com.yly.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yly.core.Predict_CAMFB;
import com.yly.dao.ItemDao;
import com.yly.dao.impl.ItemDaoImpl;

/**
 * Servlet implementation class PerRecCAMFBServlet
 */
@WebServlet("/PerRecCAMFBServlet")
public class PerRecCAMFBServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PerRecCAMFBServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");

		// 获得客户端参数
		String user_id = request.getParameter("user_id");
		double latitude = Double.valueOf(request.getParameter("latitude"));
		double longitude = Double.valueOf(request.getParameter("longitude"));
		int radius = Integer.parseInt(request.getParameter("radius"));
		// context
		int weather = Integer.valueOf(request.getParameter("weather"));
		int time = Integer.valueOf(request.getParameter("time"));
		int daytype = Integer.valueOf(request.getParameter("daytype"));
		int season = Integer.valueOf(request.getParameter("season"));

		int recNum = Integer.valueOf(request.getParameter("recNum"));

		ItemDao dao = new ItemDaoImpl();

		int contextNum = 4;
		int[] currCon = new int[contextNum];
		currCon[0] = time;
		currCon[1] = weather;
		currCon[2] = season;
		currCon[3] = daytype;
		// 获得候选推荐集
		ArrayList<Integer> init_item_list = dao.getItemWithinDistance(latitude, longitude, radius);
		PrintWriter out = response.getWriter();

		String recItems = null;

		if (init_item_list.size() != 0) {
			Predict_CAMFB p = new Predict_CAMFB(user_id, init_item_list, recNum,currCon);
			try {
				recItems = p.getRecommendation();

			} catch (Exception e) {
				e.printStackTrace();
			}
			if (recItems.isEmpty()) {
				out.print("1,4.8;2,4.6;3,4.5;4,4.3;5,4.2");
			}
			out.print(recItems);
		} else {
			out.print(0);
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
