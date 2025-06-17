package com.springbootstudy.bbs.ajax;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springbootstudy.bbs.domain.Reply;
import com.springbootstudy.bbs.service.BoardService;

@RestController
public class BoardAjaxController {
	
	@Autowired
	private BoardService boardService;
	
	// 댓글 삭제 Ajax 요청을 처리하는 메서드
	@DeleteMapping("/replyDelete.ajax")
	public List<Reply> deleteReply(@RequestParam("no") int no, @RequestParam("bbsNo") int bbsNo) {
		// 댓글 번호에 해당하는 댓글을 삭제한다.
		boardService.deleteReply(no);
		
		// 새롭게 갱신된 댓글 리스트를 가져와 반환한다.
		return boardService.replyList(bbsNo);
	}
	
	// 댓글 수정 Ajax 요청을 처리하는 메서드
	@PatchMapping("/replyUpdate.ajax")
	public List<Reply> updateReply(Reply reply) {
		// 수정된 댓글 정보를 받아서 댓글 번호에 해당하는 댓글을 수정한다.
		boardService.updateReply(reply);
		
		// 새롭게 갱신된 댓글 리스트를 가져와 반환한다.
		return boardService.replyList(reply.getBbsNo());
	}
	
	@PostMapping("/replyWrite.ajax")
	public List<Reply> addReply(Reply reply) {
		// 새로운 댓글을 등록한다.
		boardService.addReply(reply);

	 	return boardService.replyList(reply.getBbsNo());
	}
	
	@PostMapping("/recommend.ajax")
	public Map<String, Integer> recommend(@RequestParam("no") int no, @RequestParam("recommend") String recommend) {
	
		return boardService.recommend(no, recommend);
	}
}
