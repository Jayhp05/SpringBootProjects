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

import com.springbootstudy.app.dto.MemoDTO;
import com.springbootstudy.app.service.MemoEntityService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
/* RestController 클래스 임을 정의
 * @RestController 애노테이션은 @Controller에 @ResponseBody가
 * 추가된 것과 동일하다. RestController의 주용도는 JSON으로 응답하는 것이다.  
 **/
@RestController
@RequiredArgsConstructor
@Slf4j
public class MemoEntityController {	
	
	// 클래스에 롬복의 @RequiredArgsConstructor가 적용되어 생성자를 통해 주입된다.
	private final MemoEntityService entityService;
	
	/* @RestController 애노테이션이 클래스에 적용되었기 때문에 
	 * 이 메서드에서 반환되는 값은 JSON으로 직렬화되어 응답 본문에 포함된다.
	 * ResponseEntity 객체는 사용자 요청에 대한 응답 데이터를 포함하는 객체로
	 * HttpStatus, HttpHeaders, HttpBody를 포함하고 있어서 HTTP 상태
	 * 코드와 헤더 그리고 응답 본문에 포함되는 데이터를 제어할 수 있는 객체이다.	 
	 **/
	@GetMapping("/jpaMemos")
	public ResponseEntity<List<MemoDTO>> memoList() {		
		return ResponseEntity
				.ok()	// 정상처리 200 OK
				.body(entityService.memoList()); // 응답 본문 데이터		
	}

	@GetMapping("/jpaMemos/{no}")
	public MemoDTO getMemo(@PathVariable("no") int no) {
		return entityService.getMemo(no);
	}

	/* 메모 추가 요청 처리 메서드
	 * @RequestBody는 요청 본문으로 들어오는 JSON이나 XML 데이터를 MemoDTO
	 * 객체로 변환한다. JSON이나 XML이 아니거나 포맷에 맞지 않으면 400 오류가 발생한다. 
	 **/
	@PostMapping("/jpaMemos")
	public ResponseEntity<MemoDTO> addMemo(@RequestBody MemoDTO memo) {
		log.info("title : ", memo.getTitle());		
		// HTTP 상태 코드를 201 CREATED로 설정하여 응답
		return ResponseEntity
					.status(HttpStatus.CREATED)
					.body(entityService.addMemo(memo));
	}

	// 메모 수정 요청 처리 메서드
	@PutMapping("/jpaMemos")
	public ResponseEntity<MemoDTO> updateMemo(MemoDTO memo) {		
		// HTTP 상태 코드를 201 CREATED로 설정하여 응답
		return ResponseEntity
					.status(HttpStatus.CREATED)
					.body(entityService.updateMemo(memo));
	}
	
	// 메모 삭제 요청 처리 메서드
	@DeleteMapping("/jpaMemos")
	public Map<String, Object> deleteMemo(@RequestParam("no") int no) {		
		entityService.deleteMemo(no);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();		
		resultMap.put("result", true);		
		return resultMap;
	}
}
