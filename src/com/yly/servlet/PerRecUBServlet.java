package com.yly.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yly.core._UserBasedRecommender;

/**
 * Servlet implementation class PerRecUBServlet
 * 基于用户推荐，从所有物品中做推荐
 */
@WebServlet("/PerRecUBServlet")
public class PerRecUBServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PerRecUBServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		
		long user_id = Long.valueOf(request.getParameter("user_id"));
		int recNum = Integer.valueOf(request.getParameter("recNum"));
		//int mode = Integer.valueOf(request.getParameter("mode")); //选择相似度计算方法
		
		PrintWriter out =response.getWriter();
		
		String recItems = null;
		
		try {
			_UserBasedRecommender r = new _UserBasedRecommender(user_id,recNum);
			recItems = r.getRecommendation();
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		out.print(recItems);
		
		out.flush();
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
