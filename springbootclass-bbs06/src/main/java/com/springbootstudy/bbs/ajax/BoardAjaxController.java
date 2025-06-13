package com.springbootstudy.bbs.ajax;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springbootstudy.bbs.service.BoardService;

@RestController
public class BoardAjaxController {
	
	@Autowired
	private BoardService boardService;
	
	@PostMapping("/recommend.ajax")
	public Map<String, Integer> recommend(@RequestParam("no") int no, @RequestParam("recommend") String recommend) {
	
		return boardService.recommend(no, recommend);
	}
}
