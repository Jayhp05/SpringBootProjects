package com.springbootstudy.bbs.ajax;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.springbootstudy.bbs.domain.Reply;
import com.springbootstudy.bbs.service.BoardService;

/* RestController 클래스 임을 정의
 * @RestController 애노테이션은 @Controller에 @ResponseBody가
 * 추가된 것과 동일하다. RestController의 주용도는 JSON으로 응답하는 것이다.  
 **/
@RestController
public class BoardAjaxController {
	
	// 의존객체 주입 설정
	@Autowired
	private BoardService boardService;
	
	/* 추천/탱큐에 대한 Ajax 요청을 처리하는 메서드 
	 *
	 * @RestController 애노테이션이 클래스에 적용되었기 때문에 
	 * 이 메서드에서 반환하는 값은 JSON으로 직렬화되어 응답 본문에 포함된다.
	 **/
	@PostMapping("/recommend.ajax")
	public Map<String, Integer> recommend(@RequestParam("no") int no,
			@RequestParam("recommend") String recommend) {
		
		/* RestController 클래스에서 @RequestMapping, @GetMapping, 
		 * @PostMapping 등과 같은 요청 맵핑이 적용된 메서드의 반환 타입이
		 * String인 경우 HttpMessageConverter를 이용해 String 객체를 직렬화
		 * 하여 반환하고 반환 타입이 위와 같이 Map이거나 자바 객체인 경우
		 * MappingJackson2HttpMessageConverter를 사용해 JSON으로 변환한다.
		 * 
		 * Service 클래스에서 맵에 저장할 때 아래와 같이 저장하였다.
		 * 
		 * map.put("recommend", board.getRecommend());
		 * map.put("thank", board.getThank()); 
		 * 
		 * 이 데이터는 다음과 같이 JSON 형식으로 변환되어 응답된다.
		 * 
		 * { "recommend": 15, "thank": 26 }
		 **/		
		return boardService.recommend(no, recommend);		
	}
	
	// 댓글 쓰기 Ajax 요청을 처리하는 메서드
	@PostMapping("/replyWrite.ajax")	
	public List<Reply> addReply(Reply reply) {
		
		// 새로운 댓글을 등록한다.
		boardService.addReply(reply);
		
		/* 댓글 쓰기가 완료되면 새롭게 추가된 댓글을 포함해서 게시글 
		 * 상세보기에 다시 출력해야 하므로 갱신된 댓글 리스트를 가져와 반환한다.
		 * 
		 * 아래는 게시글 번호에 해당하는 댓글이 List<Reply>로 반환되기
		 * 때문에 스프링은 MappingJackson2HttpMessageConverter를
		 * 사용해 다음과 같이 객체 배열 형태의 JSON 형식으로 변환되어 응답된다.
		 * 
		 * [
		 *  {bbsNo: 100, no: 27, regDate: 1516541138000, 
		 * 		replyContent: "저도 동감이여..", replyWriter: "midas"},
		 *  {bbsNo: 100, no: 20, ... }, 
		 *    ...
		 *  {bbsNo: 100, no: 1, regDate: 1462682672000, 
		 *  	replyContent: "항상 감사합니다...", replyWriter: "midas"}
		 * ]
		 **/
		return boardService.replyList(reply.getBbsNo());
	}
	
	// 댓글 수정 Ajax 요청을 처리하는 메서드	
	@PatchMapping("/replyUpdate.ajax")	
	public List<Reply> updateReply(Reply reply) {
		
		// 수정된 댓글 정보를 받아서 댓글 번호에 해당하는 댓글을 수정한다.
		boardService.updateReply(reply);
		
		// 새롭게 갱신된 댓글 리스트를 가져와 반환한다.
		return boardService.replyList(reply.getBbsNo());
	}
	
	// 댓글 삭제 Ajax 요청을 처리하는 메서드
	@DeleteMapping("/replyDelete.ajax")
	public List<Reply> deleteReply(@RequestParam("no") int no, 
			@RequestParam("bbsNo") int bbsNo) {
		
		// 댓글 번호에 해당하는 댓글을 삭제한다.
		boardService.deleteReply(no);
		
		// 새롭게 갱신된 댓글 리스트를 가져와 반환한다.
		return boardService.replyList(bbsNo);
	}
}
