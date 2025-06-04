package com.springbootstudy.bbs.configurations;

import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {
	
	public void addViewController(ViewControllerRegistry registry) {
//		여기에 적용하는 뷰 전용 컨트롤러는 prefix, suffix가 기본 적용된다.
		registry.addViewController("/writeForm").setViewName("vieww/writeForm");
		registry.addViewController("/writeBoard").setViewName("views/writeForm");
	}

}
