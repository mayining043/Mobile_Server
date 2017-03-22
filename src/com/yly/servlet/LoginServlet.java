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
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();

		// 获得数据库操作dao接口
		UserDao dao = new UserDaoImpl();

		// 获得客户端参数
		String user_name = request.getParameter("username");
		String password = request.getParameter("password");

		User u = dao.login(user_name, password);
		if (u != null) {
			// 响应客户端内容，登录成功
			out.print(build(u));
		} else {
			// 响应客户端内容，登录失败
			out.print("0");
		}
		out.flush();
		out.close();
	}

	// 将用户对象转换为字符串返回给客户端
	private String build(User u) {
		String userMsg = "";
		userMsg += u.getUser_id();
		return userMsg;
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
