package com.yly.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yly.dao.UserDao;
import com.yly.dao.impl.UserDaoImpl;
import com.yly.entity.User;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegisterServlet() {
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

		// 获得数据库操作dao接口
		UserDao dao = new UserDaoImpl();
		// 获得客户端参数
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		// 实例化用户类
		User u = new User();
		// 设置用户属性
		u.setUser_name(username);
		u.setPassword(password);
		// 返回注册是否成功
		int result = dao.register(u);

		if (result > 0) {
			out.print(username+"注册成功!");
		} else if (result == -1) {
			out.print("该用户名已被注册!");
		} else {
			out.print("注册失败！");
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
