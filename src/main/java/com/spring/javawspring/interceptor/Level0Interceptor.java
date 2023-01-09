package com.spring.javawspring.interceptor;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class Level0Interceptor extends HandlerInterceptorAdapter{

	//상속하는 HandlerInterceptorAdapter에서 ctrl+클릭해서 preHandle가져오기
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception  {
		HttpSession  session = request.getSession(); //주소창복사해서 로그인하는거 막기위해 작성
		int level = session.getAttribute("sLevel")==null? 99 : (int) session.getAttribute("sLevel");
		if(level != 0) { //관리자가 아닌경우는 무조건 초기화면창으로 보내준다.
			RequestDispatcher dispatcher = request.getRequestDispatcher("/msg/adminNo"); //메세지로 보내야됨
			dispatcher.forward(request, response);
			return false;
		}
		
		return true;
	}
}
