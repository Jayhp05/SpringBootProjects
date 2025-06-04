package com.springbootstudy.bbs.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/* @Configuration 애노테이션은 스프링 환경설정과 스프링 빈(Spring Bean)을 
 * 등록하기 위한 애노테이션 이다. 이 애노테이션은 스프링 Bean을 등록할 때
 * 싱글톤(Singleton)으로 생성해주며 스프링 DI 컨테이너가 Bean을 관리해 준다.
 * WebMvcConfigurer는 스프링 MVC 환경 설정을 위해서 제공되는 인터페이스 이다.  
 **/
@Configuration
public class WebConfig implements WebMvcConfigurer{
	
	/* 어떤 요청에 대해서 요청을 처리한 결과 데이터인 모델은 필요 없고 화면만
	 * 보여주는 경우 아래와 같이 addViewControllers() 메서드를 오버라이드하여
	 * 뷰 전용 컨트롤러를 설정할 수 있다. 
	 **/	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		/* 아래는 /writeForm과 /writeBoard 요청에 대한 뷰를 지정한 것이다.
		 * 여기서 지정한 뷰의 이름과 prefix, suffix를 조합하여 실제 뷰의
		 * 물리적인 이름이 결정된다.   
		 **/
		registry.addViewController("/writeForm").setViewName("views/writeForm");
		registry.addViewController("/writeBoard").setViewName("views/writeForm");		
	}
}
