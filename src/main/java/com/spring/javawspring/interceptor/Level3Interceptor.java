package com.spring.javawspring.interceptor;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class Level3Interceptor extends HandlerInterceptorAdapter {
	//상속하는 HandlerInterceptorAdapter에서 ctrl+클릭해서 preHandle가져오기
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession(); //주소창복사해서 로그인하는거 막기위해 작성
		int level = session.getAttribute("sLevel")==null? 99 : (int) session.getAttribute("sLevel");
		
		if(level > 3) {
			RequestDispatcher dispatcher;
			if(level == 99) {	// 비회원인 경우
				dispatcher = request.getRequestDispatcher("/msg/memberNo");
			}
			else {	// 준회원(level : 4)인경우
				dispatcher = request.getRequestDispatcher("/msg/levelCheckNo");
			}
			dispatcher.forward(request, response);
			return false;
		}
		
		return true;
	}
}
