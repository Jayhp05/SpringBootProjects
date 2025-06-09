package com.springbootstudy.bbs.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		// 여기에 적용하는 뷰 전용 컨트롤러는 prefix, suffix가 기본 적용된다.
		registry.addViewController("/writeForm").setViewName("views/writeForm");
		registry.addViewController("/writeBoard").setViewName("views/writeForm");
		
//		로그인 폼 요청
		registry.addViewController("/loginForm").setViewName("member/loginForm");
		
		// 회원가입 폼 뷰 전용 컨트롤러 설정 추가
		registry.addViewController("/joinForm").setViewName("member/memberJoinForm");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		registry.addResourceHandler("/resources/files/**").addResourceLocations("files:./src/main/resources/static/files/")
				.setCachePeriod(1);
	}
	
}
