package com.springbootstudy.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FirstController {

	@RequestMapping("/greeting")
	@ResponseBody
	public String greeting() {
		return "안녕 스프링 부트";
	}
}
