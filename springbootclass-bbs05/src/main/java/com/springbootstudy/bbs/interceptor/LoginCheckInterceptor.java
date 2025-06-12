package com.springbootstudy.bbs.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		log.info("##################### LoginCheckInterceptor - preHandle() #####################");
		
		HttpSession session = request.getSession();
		session.removeAttribute("logMsg");
		
		if(session.getAttribute("isLogin") == null) {
			
			response.sendRedirect("loginForm");
			session.setAttribute("loginMsg", "로그인이 필요한 서비스");
//			우리 컨트롤러의 메서드 실행이 취소
			return false;
		}
		
//		여기서 true를 반환하면 맵핑되어있는 우리 컨트롤러의 메서드가 호출
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		log.info("##################### LoginCheckInterceptor - postHandle() #####################");
		
		response.setHeader("Cache-Control", "no-cache");
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		log.info("##################### LoginCheckInterceptor - afterCompletion() #####################");

	}

}
