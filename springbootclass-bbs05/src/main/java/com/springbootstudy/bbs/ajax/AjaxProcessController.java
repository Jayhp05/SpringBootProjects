package com.springbootstudy.bbs.ajax;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springbootstudy.bbs.service.MemberService;

@RestController
public class AjaxProcessController {

	@Autowired
	private MemberService memberService;
	
//	자바스크립트에서 요청이 들어오는 메서드
	@GetMapping("/passCheck.ajax")
	public Map<String, Boolean> memberPassCheck(@RequestParam("id") String id, @RequestParam("pass") String pass) {
		
		boolean result = memberService.memberPassCheck(id, pass);
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		map.put("result", result);
		
		return map;
	}
}
