package com.springbootstudy.bbs.controller;

import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
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

	// 게시 글 삭제 요청을 받는 메서드
	@PostMapping("/delete")
	public String deleteBoard(RedirectAttributes reAttrs,
			@RequestParam(value="pageNum", required=false, defaultValue="1") int pageNum,
			HttpServletResponse response, PrintWriter out,
			@RequestParam("no") int no, @RequestParam("pass") String pass,
			@RequestParam(value="type", required=false, 
				defaultValue="null") String type, 
			@RequestParam(value="keyword", required=false, 
				defaultValue="null")String keyword) {
		
		// 비번이 맞지 않으면 - 경고창을 띄운다.
		boolean isPassCheck = boardService.isPassCheck(no, pass);
		
		if(!isPassCheck) { // 비번이 맞지 않으면 - 자바스크립트가 응답되도록 하면 됩니다.
			response.setContentType("text/html; charset=utf-8");
			out.println("<script>");
			out.println("	alert('비밀번호가 맞지 않음');");
			out.println("	history.back();");
			out.println("</script>");
			
			return null;
		}
		
		// 비번이 맞으면 - 이전 게시 글을 수정하기
		boardService.deleteBoard(no);		
		
		// boardList?pageNum=10
		reAttrs.addAttribute("pageNum", pageNum);
		reAttrs.addFlashAttribute("test1", "1회성 파라미터");		
		
		boolean searchOption = type.equals("null") || keyword.equals("null") ? false : true;
		if(searchOption) {
			reAttrs.addAttribute("type", type);
			reAttrs.addAttribute("keyword", keyword);
		}
		
		// 게시 글 쓰기, 수정, 삭제는 완료된 후에 리다이렉트
		return "redirect:boardList";
	}		
	
	// 게시 글 수정 폼에서 게시 글 수정 요청을 받는 메서드
	@PostMapping("/update")
	public String updateBoard(Board board, RedirectAttributes reAttrs,
			@RequestParam(value="pageNum", required=false, defaultValue="1") int pageNum,
			HttpServletResponse response, PrintWriter out,
			@RequestParam(value="type", required=false, 
				defaultValue="null") String type, 
			@RequestParam(value="keyword", required=false, 
				defaultValue="null")String keyword) {
		
		// 비번이 맞지 않으면 - 경고창을 띄운다.
		boolean isPassCheck = boardService.isPassCheck(board.getNo(), board.getPass());
		
		if(!isPassCheck) { // 비번이 맞지 않으면 - 자바스크립트가 응답되도록 하면 됩니다.
			response.setContentType("text/html; charset=utf-8");
			out.println("<script>");
			out.println("	alert('비밀번호가 맞지 않음');");
			out.println("	history.back();");
			out.println("</script>");
			
			return null;
		}
		
		// 비번이 맞으면 - 이전 게시 글을 수정하기
		boardService.updateBoard(board);		
		
		boolean searchOption = type.equals("null") || keyword.equals("null") ? false : true;
		if(searchOption) {
			reAttrs.addAttribute("type", type);
			reAttrs.addAttribute("keyword", keyword);
		}
				
		// 게시 글 쓰기, 수정, 삭제는 완료된 후에 리다이렉트
		// return "redirect:boardList?pageNum=" + pageNum;
		reAttrs.addAttribute("pageNum", pageNum);
		reAttrs.addFlashAttribute("test1", "1회성 파라미터");
		return "redirect:boardList";
	}	
	
	// 게시 글 수정폼 요청을 받는 메서드
	@PostMapping("/updateForm")
	public String updateBoard(Model model,
			HttpServletResponse response, PrintWriter out,
			@RequestParam("no") int no, 
			@RequestParam("pass") String pass, 
			@RequestParam(value="pageNum", required=false, defaultValue="1") int pageNum,
			@RequestParam(value="type", required=false, 
				defaultValue="null") String type, 
			@RequestParam(value="keyword", required=false, 
				defaultValue="null")String keyword) {
		
		// 비번이 맞지 않으면 - 경고창을 띄운다.
		boolean isPassCheck = boardService.isPassCheck(no, pass);
		
		if(!isPassCheck) { // 비번이 맞지 않으면 - 자바스크립트가 응답되도록 하면 됩니다.
			response.setContentType("text/html; charset=utf-8");
			out.println("<script>");
			out.println("	alert('비밀번호가 맞지 않음');");
			out.println("	history.back();");
			out.println("</script>");
			
			return null;
		}
		
		// 비번이 맞으면 - 이전 게시 글을 읽어와 폼에 출력
		Board board = boardService.getBoard(no, false);
		model.addAttribute("board", board);
		model.addAttribute("pageNum", pageNum);
		
		boolean searchOption = type.equals("null") || keyword.equals("null") ? false : true;
		
		model.addAttribute("searchOption", searchOption);
		if(searchOption) {
			model.addAttribute("type", type);
			model.addAttribute("keyword", keyword);
		}
				
		return "views/updateForm";
	}
	
	// 게시 글 쓰기 폼으로부터 게시 글 등록 요청을 받는 메서드
	@PostMapping("/addBoard")
	public String addBoard(Board board) { // 커멘트 객체
		
		boardService.addBoard(board);
		
		// 리다이렉트 
		return "redirect:boardList";
	}	
	
	// 게시 글 쓰기 폼 요청을 받는 메서드
	@GetMapping("/addBoard")
	public String addBoard() {
		return "views/writeForm";
	}
	
	@GetMapping("/boardDetail")  
	public String getBoard(Model model, @RequestParam("no") int no,
			@RequestParam(value="pageNum", required=false, defaultValue="1") int pageNum,
			@RequestParam(value="type", required=false, 
				defaultValue="null") String type, 
			@RequestParam(value="keyword", required=false, 
				defaultValue="null")String keyword) {
		
		boolean searchOption = (type.equals("null") || keyword.equals("null")) ? false : true;
		if(searchOption) {
			model.addAttribute("type", type);
			model.addAttribute("keyword", keyword);
		}
		model.addAttribute("searchOption", searchOption);
		model.addAttribute("board", boardService.getBoard(no, true));
		model.addAttribute("pageNum", pageNum);
		return "views/boardDetail";
	}
	
	//  요청을 받으면서 입력갑 처리
	@GetMapping({"/", "/boardList"})
	public String boardList(Model model,
			@RequestParam(value="pageNum", 
					required=false, defaultValue="1") int pageNum,
			@RequestParam(value="type", required=false, 
					defaultValue="null") String type, 
			@RequestParam(value="keyword", required=false, 
					defaultValue="null")String keyword) {
			
		model.addAllAttributes(boardService.boardList(pageNum, type, keyword));
		// spring.thymeleaf.prefix=classpath:/templates/
		// spring.thymeleaf.suffix=.html
		// classpath:/templates/ main .htm
		// 논리적인 뷰의 이름
		return "views/boardList";
	}
	
}
