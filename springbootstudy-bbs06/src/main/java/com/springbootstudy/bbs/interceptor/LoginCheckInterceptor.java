package com.springbootstudy.bbs.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/* HandlerInterceptor는 요청 경로마다 접근 제어를 다르게 하거나 특정 URL에 접근할 때
 * 공통적으로 처리해야 할 것이 있을 때 주로 사용한다. 앞의 프로젝트에서는 회원이 로그인
 * 상태인지 컨트롤러 메서드마다 세션을 체크해 로그인 상태를 체크 했기 때문에 동일한
 * 코드의 중복을 피할 수 없었다. 하지만 스프링프레임워크가 제공하는 HandlerInterceptor를
 * 구현해 애플리케이션에 적용하면 중복 코드를 줄일 수 있다. 이처럼 여러 요청 경로 또는
 * 여러 컨트롤러에서 공통으로 적용해야 할 기능을 구현할 때 HandlerInterceptor를
 * 사용하면 아주 유용하다.
 * 
 * HandlerInterceptor를 사용하면 다음과 같은 시점에 공통 기능을 적용할 수 있다.
 * 
 * 1. 컨트롤러 실행 전
 * 2. 컨트롤러 실행 후 - 아직 뷰가 생성되지 않았다.
 * 3. 뷰가 생성되고 클라이언트로 전송된 후
 * 
 * HandlerInterceptor 인터페이스에 정의된 추상 메서드는 모두 3개이며 
 * HandlerInterceptor를 구현하고 동작시키기 위해서는 Spring MVC 환경설정
 * 클래스에 Bean으로 등록하고 해당 인터셉터가 동작할 경로 패턴(PathPattern)을
 * 설정해야 한다.
 **/
// 접속자가 로그인 상태인지 체크하는 인터셉터
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
	
	/* preHandle() 메서드는 클라이언트의 요청이 들어오고 컨트롤러가 실행되기
	 * 전에 공통으로 적용할 기능을 구현할 때 사용한다.
	 * 예를 들면 특정 요청에 대해 로그인이 되어 있지 않으면 컨트롤러를 실행하지
	 * 않거나 컨트롤러를 실행하기 전에 그 컨트롤러에서 필요한 정보를 생성해
	 * 넘겨 줄 필요가 있을 때 preHandler() 메서드를 이용해 구현하면 된다.
	 * 이 메서드가 false를 반환하면 다음으로 연결된 HandlerInterceptor
	 * 또는 컨트롤러 자체를 실행하지 않게 할 수 있다. 
	 **/
	@Override
	public boolean preHandle(HttpServletRequest request, 
			HttpServletResponse response, Object handler) throws Exception {
		log.info("##########LoginCheckInterceptor - preHandle()##########");
		// 현재 세션에 저장된 loginMsg 속성을 삭제
		HttpSession session = request.getSession();
		session.removeAttribute("loginMsg");
				
		// 세션에 isLogin란 이름의 속성이 없으면 로그인 상태가 아님
		if(request.getSession().getAttribute("isLogin") == null) {
			// 로그인 상태가 아니라면 로그인 폼으로 리다이렉트 시킨다.
			response.sendRedirect("loginForm");
			session.setAttribute("loginMsg", "로그인이 필요한 서비스");
			return false;
		}		
		return true;
	}

	/* postHandle() 메서드는 클라이언트 요청이 들어오고 컨트롤러가 정상적으로
	 * 실행된 이후에 공통적으로 적용할 추가 기능이 있을 때 주로 사용한다.
	 * 만약 컨트롤러 실행 중에 예외가 발생하게 되면 postHandle() 메서드는 
	 * 호출되지 않는다. 
	 **/
	@Override
	public void postHandle(HttpServletRequest request, 
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.info("##########LoginCheckInterceptor - postHandle()##########");		
		/* 수정 폼에서 수정 요청을 보내면서 비밀번호가 틀리면 자바스크립트로
		 * history.back()을 사용하는데 POST 요청에서 Redirect 시키지 않을 경우
		 * 브라우저에서 "양식 다시 제출 확인 - ERR_CACHE_MISS" 페이지가 뜰 수
		 * 있다. 이런 경우 응답 데이터에 노캐쉬 설정을 하면 해결할 수 있다.
		 **/
		response.setHeader("Cache-Control", "no-cache");
	}
	
	/* afterCompletion() 메서드는 클라이언트의 요청을 처리하고 뷰를 생성해
	 * 클라이언트로 전송한 후에 호출된다. 클라이언트 실행 중에 예외가 발생하게 되면
	 * 이 메서드 4번째 파라미터로 예외 정보를 받을 수 있다. 예외가 발생하지 않으면
	 * 4번째 파라미터는 null을 받게 된다.
	 **/
	@Override
	public void afterCompletion(HttpServletRequest request, 
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		log.info("##########LoginCheckInterceptor - afterCompletion()##########");
	}	
}
