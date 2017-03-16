package com.yly.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yly.core._SVDRecommender;

/**
 * Servlet implementation class PerRecUB
 * get personal recommendations with mahout's userbased algorithm
 * 没有范围判断以及物品限制，基于所有物品做推荐
 */
@WebServlet("/PerRecSVDnoLimitServlet")
public class PerRecSVDnoLimitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PerRecSVDnoLimitServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		// TODO Auto-generated method stub
		
		//获得客户端参数
		String user_id = request.getParameter("user_id");
		int recNum = Integer.valueOf(request.getParameter("recNum"));
		
		String recItems = null;
		PrintWriter out = response.getWriter();
		
		try {
			_SVDRecommender r = new _SVDRecommender(user_id,recNum);
			recItems=r.getRecommendation();
		} catch (Exception e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
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
