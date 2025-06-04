package com.springbootstudy.bbs.controller;

import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	public String deleteBoard(RedirectAttributes reAttrs, @RequestParam(value="pageNum", required=false, defaultValue="1") int pageNum, HttpServletResponse response, PrintWriter out, @RequestParam("no") int no, @RequestParam("pass") String pass) {

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
		
		reAttrs.addAttribute("pageNum", pageNum);
		
//		게시글 삭제가 완료되면 리다이렉트
		return "redirect:boardList";
	}
	
//	수정 폼에서 게시글 수정
	@PostMapping("/update")
	public String updateBoard(Board board, RedirectAttributes reAttrs, @RequestParam(value="pageNum", required=false, defaultValue="1") int pageNum, HttpServletResponse response, PrintWriter out) {

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
		
//		게시글 쓰기, 수정, 삭제는 완료된 후레 리다이렉트
//		return "redirect:boardList?pageNum=" + pageNum;
		reAttrs.addAttribute("pageNum", pageNum);
		reAttrs.addFlashAttribute("test1", "1회성 파라미터");
		
		return "redirect:boardList";
	}
	
//	게시글 수정폼 요청을 받는 메서드
	@PostMapping("/updateForm")
	public String updateBoard(Model model, HttpServletResponse response, PrintWriter out, @RequestParam("no") int no, @RequestParam("pass") String pass,
				@RequestParam(value="pageNum", required=false, defaultValue="1") int pageNum) {

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
		Board board = boardService.getBoard(no, false);
		model.addAttribute("board", board);
		model.addAttribute("pageNum", pageNum);
		
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
	public String getBoard(Model model, @RequestParam("no") int no,
			@RequestParam(value="pageNum", required=false, defaultValue="1") int pageNum) {
		
		model.addAttribute("board", boardService.getBoard(no, true));
		model.addAttribute("pageNum", pageNum);
		
		return "views/boardDetail";
	}
	 
//	요청을 받으면서 입력값 처리
	@GetMapping({"/", "/boardList"})
	public String boardList(Model model, @RequestParam(value="pageNum", required=false, defaultValue="1") int pageNum) {
		
		model.addAllAttributes(boardService.boardList(pageNum));
	
//		classpath = templates/main.html 기본경로임
		return "views/boardList";
	}
}
