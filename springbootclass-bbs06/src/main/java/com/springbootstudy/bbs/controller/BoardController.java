package com.springbootstudy.bbs.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.springbootstudy.bbs.configurations.WebConfig;
import com.springbootstudy.bbs.domain.Board;
import com.springbootstudy.bbs.domain.Reply;
import com.springbootstudy.bbs.service.BoardService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class BoardController {

    private final MemberController memberController;

    private final WebConfig webConfig;
	
	private static final String DEFAULT_PATH = "src/main/resources/static/files/";
	
	@Autowired
	private BoardService boardService;
	
	BoardController(WebConfig webConfig, MemberController memberController) {
		this.webConfig = webConfig;
		this.memberController = memberController;
	}
	
	// 게시글 상세보기에서 들어오는 파일 다운로드 요청을 처리하는 메서드
	@GetMapping("/fileDownload")
	public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String fileName = request.getParameter("fileName");
		log.info("fileName : " + fileName);
		
		File parent = new File(DEFAULT_PATH);
		File file = new File(parent.getAbsolutePath(), fileName);
		log.info("file.getName() : " + file.getName());

		response.setContentType("application/download; charset=UTF-8");
		response.setContentLength((int) file.length());

		fileName = URLEncoder.encode(file.getName(), "UTF-8");
		log.info("다운로드 fileName : " + fileName);

		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
	 
		response.setHeader("Content-Transfer-Encoding", "binary");

		OutputStream out = response.getOutputStream();
		FileInputStream fis = null;
	 
		fis = new FileInputStream(file);

		FileCopyUtils.copy(fis,  out);
		
		if(fis != null) {
			fis.close();
		}
	 
		out.flush();
	}

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
	public String addBoard(Board board,
			 @RequestParam(value="addFile", required=false) MultipartFile multipartFile) throws IOException { // 커멘트 객체
		
		System.out.println("originName : " + multipartFile.getOriginalFilename());
		System.out.println("name : " + multipartFile.getName());
		
		 // 업로된 파일이 있으면
		if (multipartFile != null && !multipartFile.isEmpty()) {
			
		    // File 클래스는 파일과 디렉터리를 다루기 위한 클래스               
		    File parent = new File(DEFAULT_PATH);
		        
//		    parent = "src/main/resources/static/files/"
		    // 파일 업로드 위치에 폴더가 존재하지 않으면 폴더 생성
		    if (!parent.isDirectory() && !parent.exists()) {
		    	parent.mkdirs();
		    }
		              
		    UUID uid = UUID.randomUUID();
		    String extension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
		    String saveName = uid.toString() + "." + extension;
		          
			File file = new File(parent.getAbsolutePath(), saveName);
			// File 객체를 이용해 파일이 저장될 절대 경로 출력
			log.info("file abs path : " + file.getAbsolutePath());
			log.info("file path : " + file.getPath());
			  
			// 업로드 되는 파일을 static/files 폴더에 복사한다.
			multipartFile.transferTo(file);
			  
			// 업로드된 파일 이름을 게시글의 첨부 파일로 설정한다.
			board.setFile1(saveName);

		} 
		else {
			// 파일이 업로드 되지 않으면 콘솔에 로그 출력
			log.info("No file uploaded - 파일이 업로드 되지 않음");  
		}
		
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
		
		List<Reply> replyList = boardService.replyList(no);
		model.addAttribute("replyList", replyList);
		
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
