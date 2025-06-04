package com.springbootstudy.bbs.controller;

import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springbootstudy.bbs.domain.Board;
import com.springbootstudy.bbs.service.BoardService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class BoardController {

	@Autowired
	private BoardService boardService;
	
//	게시글 삭제 요청을 받는 메서드
	@PostMapping("/delete")
	public String deleteBoard(HttpServletResponse response, PrintWriter out, @RequestParam("no") int no, @RequestParam("pass") String pass) {

//		비번이 맞지 않으면 - 경고창을 띄운다.
		boolean isPassCheck = boardService.isPassCheck(no, pass);
		
		if(! isPassCheck) { // 비번이 맞이 않으면 - 자바스크립트가 응답되도록 해야 됨.
			response.setContentType("text/html; charset=utf-8");
			out.println("<script>");
			out.println("	alert('비밀번호가 맞지 않음');");
			out.println("	history.back();");
			out.println("</script>");
			
			return null;
		}
//		비번이 맞으면 - 이전 게시글을 수정하기
		boardService.deleteBoard(no);
		
//		게시글 삭제가 완료되면 리다이렉트
		return "redirect:boardList";
	}
	
//	수정 폼에서 게시글 수정
	@PostMapping("/update")
	public String updateBoard(Board board, HttpServletResponse response, PrintWriter out) {

//		비번이 맞지 않으면 - 경고창을 띄운다.
		boolean isPassCheck = boardService.isPassCheck(board.getNo(), board.getPass());
		if(! isPassCheck) { // 비번이 맞이 않으면 - 자바스크립트가 응답되도록 해야 됨.
			response.setContentType("text/html; charset=utf-8");
			out.println("<script>");
			out.println("	alert('비밀번호가 맞지 않음');");
			out.println("	history.back();");
			out.println("</script>");
			
			return null;
		}
//		비번이 맞으면 - 이전 게시글을 수정하기
		boardService.updateBoard(board);
		
		return "redirect:boardList";
	}
	
//	게시글 수정폼 요청을 받는 메서드
	@PostMapping("/updateForm")
	public String updateBoard(Model model, HttpServletResponse response, PrintWriter out, @RequestParam("no") int no, @RequestParam("pass") String pass) {

//		비번이 맞지 않으면 - 경고창을 띄운다.
		boolean isPassCheck = boardService.isPassCheck(no, pass);
		if(! isPassCheck) { // 비번이 맞이 않으면 - 자바스크립트가 응답되도록 해야 됨.
			response.setContentType("text/html; charset=utf-8");
			out.println("<script>");
			out.println("	alert('비밀번호가 맞지 않음');");
			out.println("	history.back();");
			out.println("</script>");
			
			return null;
		}
		
//		비번이 맞으면 - 이전 게시글을 읽어와 폼에 출력
		Board board = boardService.getBoard(no);
		model.addAttribute("board", board);
		
		return "views/updateForm";
	}
	
	@PostMapping("/addBoard")
	public String addBoard(Board board) {
		log.info("title : ", board.getTitle());
		boardService.addBoard(board);
		return "redirect:boardList";
	}
	
	@GetMapping("/addBoard")
	public String addBoard() {
		// 게시글 쓰기 폼은 모델이 필요 없기 때문에 뷰만 반환 
		return "views/writeForm";
	}
	
	@GetMapping("/boardDetail")						 // name 또는 value로 작성 가능
	public String getBoard(Model model, @RequestParam(value="no", required=false, defaultValue="1") int no) {
		model.addAttribute("board", boardService.getBoard(no));
		return "views/boardDetail";
	}
	 
	@GetMapping({"/", "/boardList"})
	public String boardList(Model model) {
		 
		 
		model.addAttribute("bList", boardService.boardList());
	
//		classpath = templates/main.html 기본경로임
		return "views/boardList";
	}
}
