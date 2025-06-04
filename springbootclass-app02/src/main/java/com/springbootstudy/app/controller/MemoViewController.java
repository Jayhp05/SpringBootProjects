package com.springbootstudy.app.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springbootstudy.app.service.MemoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class MemoViewController {

	private final MemoService memoService;
	
	@Autowired
	public MemoViewController(MemoService memoService) {
		this.memoService = memoService;
	}
	
	@GetMapping("/thymeleaf1")
	public String thDefault01(Model model) {
		model.addAttribute("text1", "<h1>Hello Thymeleaf</h1>");
		model.addAttribute("memo", memoService.getMemo(2));
		model.addAttribute("today", LocalDate.now());
		
//		classpath:/templates/	th/default1		.html
		return "th/default1";
	}
	
	@GetMapping("/thymeleaf2/{no}")
	public String thDefault02(Model model, @PathVariable("no") int no) {
		model.addAttribute("mList", memoService.memoList());
		
		return "th/default2";
	}
	
	@GetMapping("/thymeleaf3/{no}")
	public String thDefault03(Model model, @RequestParam("no") int no) {
		model.addAttribute("memo", memoService.getMemo(no));
		model.addAttribute("score", 70);
		
		return "th/default3";
	}
	
	@GetMapping("/memoListJsp")
	public String memoListJSP(Model model) {
		
		model.addAttribute("mList", memoService.memoList());
		
//		/WEB-INF/views/main.jsp
		return "main";
	}
}
