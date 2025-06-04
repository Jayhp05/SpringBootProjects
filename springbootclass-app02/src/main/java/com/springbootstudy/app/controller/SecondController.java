package com.springbootstudy.app.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springbootstudy.app.domain.Member;

@RestController
public class SecondController {
	
//	http://localhost:8080/members/{id}
	@GetMapping("/members/{id}")
	public Map<String, Object> getMember(@PathVariable("id") String id) {
		Map<String, Object> map = new HashMap<>();
		
		map.put("id", id);
		map.put("name", "홍길동");
		map.put("age", 25);
		
		return map;
	}
	
	@PostMapping("/members")
	public Member addMember(Member member) {
		return member;
	}

//	http://localhost:8080/
	@GetMapping("/")
	public String main() {
		
		return "{\"main\":\"여기는 메인\"}";
	}
	
//	http://localhost:8080/hello
	@GetMapping("/hello")
//	@RequestMapping("/hello")
	public Map<String, String> hello(@RequestParam(value="name", required = false) String name) {
		Map<String, String> map = new HashMap<>();
		
		map.put("title", "Hello");
		map.put("greeting", "안녕하세요 " + name + "님~");
		
		return map;
	}
	
}
