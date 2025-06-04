package com.springbootstudy.app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springbootstudy.app.domain.Memo;
import com.springbootstudy.app.service.MemoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemoController {

	private final MemoService memoService;
	
//	삽입
	@PostMapping("/memos")
//	반환값 Memo, 결과 TRUE or FALSE - 폼 텍스트 데이터 = x-www-urlencoded
	public Map<String, Object> addMemo(Memo memo) {
		memoService.addMemo(memo);
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("memo", memo);
		resultMap.put("result", true);
		
		return resultMap;
	}
	
//	수정 - json 형식 으로 받기
	@PutMapping("/memos")
	public ResponseEntity<Memo> updateMemo(@RequestBody Memo memo) {
		memoService.updateMemo(memo);
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(memoService.getMemo(memo.getNo()));
	}
	
//	삭제
	@DeleteMapping("/memos")
	public Map<String, Object> deleteMemo(@RequestParam("no") int no) {
		memoService.deleteMemo(no);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", true);
		
		return resultMap;
	}
	
	@GetMapping("/memos/{no}")
	public Memo getMemo(@PathVariable("no") int no) {
		return memoService.getMemo(no);
	}
	
	@GetMapping("/memos")
	public List<Memo> memoList() { 
		return memoService.memoList();
	}
}
