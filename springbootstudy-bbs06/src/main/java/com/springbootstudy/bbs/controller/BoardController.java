package com.springbootstudy.bbs.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springbootstudy.bbs.domain.Board;
import com.springbootstudy.bbs.domain.Reply;
import com.springbootstudy.bbs.service.BoardService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/* Spring MVC Controller 클래스 임을 정의
 * @Controller 애노테이션이 적용된 클래스의 메서드는 기본적으로 뷰의 이름을 반환한다.
 **/
@Controller
@Slf4j
public class BoardController {

	// 업로드한 파일을 저장할 폴더 위치를 상수로 선언하고 있다.
	private static final String DEFAULT_PATH = "src/main/resources/static/files/";	
	
	@Autowired
	private BoardService boardService;	
	
	/* 게시글 리스트 요청을 처리하는 메서드
	 * "/", "/boardList" 로 들어오는 HTTP GET 요청을 처리하는 메서드
	 * 
	 * @RequestParam 애노테이션을 이용해 pageNum이라는 요청 파라미터를 받도록 하였다. 
	 * 아래에서 pageNum이라는 요청 파라미터가 없을 경우 required=false를 지정해 필수
	 * 조건을 주지 않았고 기본 값을  defaultValue="1"로 지정해 메서드의 파라미터인
	 * pageNum으로 받을 수 있도록 하였다. defaultValue="1"이 메서드의 파라미터인
	 * pageNum에 바인딩될 때 스프링이 int 형으로 형 변환하여 바인딩 시켜준다.또한 검색
	 * 타입과 검색어를 받기 위해 type과 keyword를 메서드의 파라미터로 지정하고 요청
	 * 파라미터가 없을 경우를 대비해 required=false를 지정해 필수 조건을 주지 않았고
	 * 기본 값을 defaultValue="null"로 지정해 type과 keyword로 받을 수 있도록 하였다.
	 **/
	@GetMapping({"/", "/boardList"})
	public String boardList(Model model, 
			@RequestParam(value="pageNum", required=false, 
						defaultValue="1") int pageNum,
			@RequestParam(value="type", required=false,  
						defaultValue="null") String type,
			@RequestParam(value="keyword", required=false,
						defaultValue="null") String keyword) {		
		
		// Service 클래스를 이용해 게시글 리스트를 가져온다.
		Map<String, Object> modelMap = boardService.boardList(pageNum, type, keyword);
		
		/* 파라미터로 받은 모델 객체에 뷰로 보낼 모델을 저장한다.
		 * 모델에는 도메인 객체나 비즈니스 로직을 처리한 결과를 저장한다. 
		 **/		
		model.addAllAttributes(modelMap);
		
		return "views/boardList";
	}
	
	/* 게시글 상세보기 요청 처리 메서드
	 * "/boardDetail"로 들어오는 HTTP GET 요청을 처리하는 메서드
	 **/
	@GetMapping("/boardDetail")
	public String getBoard(Model model, @RequestParam("no") int no, 
			@RequestParam(value="pageNum", defaultValue="1") int pageNum,
			@RequestParam(value="type", defaultValue="null") String type,
			@RequestParam(value="keyword", defaultValue="null") String keyword) {
		
		/* 요청 파라미터에서 type 또는 keyword가 비어 있으면 일반 게시글 리스트를
		 * 요청한 것으로 간주하여 false 값을 갖게 한다. Controller에서 type 또는 
		 * keyword의 요청 파라미터가 없으면 기본 값을 "null"로 지정했기 때문에 
		 * 아래와 같이 체크했다.
		 **/
		boolean searchOption = (type.equals("null") 
				|| keyword.equals("null")) ? false : true;
		
		/* 게시글 상세보기는 게시글 조회에 해당하므로 no에 해당하는 게시글 정보를
		 * 읽어오면서 두 번째 인수에 true를 지정해 게시글 읽은 횟수를 1 증가시킨다.
		 **/
		Board board = boardService.getBoard(no, true);
		
		// no에 해당하는 게시글 정보와 pageNum, searchOption을 모델에 저장한다.
		model.addAttribute("board", board);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("searchOption", searchOption);
		
		// 현재 게시글 번호에 해당하는 댓글 리스트를 가져와 모델에 저장한다.
		List<Reply> replyList = boardService.replyList(no);
		model.addAttribute("replyList", replyList);
		
		// 검색 요청이면 type과 keyword를 모델에 저장한다.
		if(searchOption) {
			model.addAttribute("type", type);
			model.addAttribute("keyword", keyword);
		}
		
		return "views/boardDetail";
	}

	/* 게시글 쓰기 폼 요청 처리 메서드
	 * "/addBoard"로 들어오는 HTTP GET 요청을 처리하는 메서드
	 **/
	@GetMapping("/addBoard")
	public String addBoard() {
		// 게시글 쓰기 폼은 모델이 필요 없기 때문에 뷰만 반환 
		return "views/writeForm";
	}
	
	/* 게시글 쓰기 폼에서 들어오는 게시글 쓰기 요청을 처리하는 메서드
	 * "/addBoard"로 들어오는 HTTP POST 요청을 처리하는 메서드
	 **/
	@PostMapping("/addBoard")	
	public String addBoard(Board board,			
			@RequestParam(value="addFile", required=false) MultipartFile multipartFile) 
					throws IOException {
		
		System.out.println("originName : " + multipartFile.getOriginalFilename());
		System.out.println("name : " + multipartFile.getName());
		
		// 업로된 파일이 있으면
        if (multipartFile != null && !multipartFile.isEmpty()) {
        	// File 클래스는 파일과 디렉터리를 다루기 위한 클래스               
        	File parent = new File(DEFAULT_PATH);
        	
           	// 파일 업로드 위치에 폴더가 존재하지 않으면 폴더 생성
           	if (!parent.isDirectory() && !parent.exists()) {
           		parent.mkdirs();
           	}
              
           	UUID uid = UUID.randomUUID();
            String extension = StringUtils.getFilenameExtension(
            								multipartFile.getOriginalFilename());
          	String saveName = uid.toString() + "." + extension;
          	
          	File file = new File(parent.getAbsolutePath(), saveName);
          	// File 객체를 이용해 파일이 저장될 절대 경로 출력
          	log.info("file abs path : " + file.getAbsolutePath());
          	log.info("file path : " + file.getPath());
          	
          	// 업로드 되는 파일을 static/files 폴더에 복사한다.
          	multipartFile.transferTo(file);
          	
          	// 업로드된 파일 이름을 게시글의 첨부 파일로 설정한다.
          	board.setFile1(saveName);
          	
          } else {
          	// 파일이 업로드 되지 않으면 콘솔에 로그 출력
          	log.info("No file uploaded - 파일이 업로드 되지 않음");
          	
          }
        
		boardService.addBoard(board);
		
		// 게시글 쓰기가 완료되면 게시글 리스트로 리다이렉트 시킨다.
		return "redirect:boardList";
	}

	/* 게시글 수정 폼 요청을 처리하는 메서드
	 * "/updateForm"으로 들어오는 HTTP POST 요청을 처리하는 메서드
	 **/
	@PostMapping("/updateForm")
	public String updateBoard(Model model, 
			HttpServletResponse response, PrintWriter out,
			@RequestParam("no") int no, @RequestParam("pass") String pass,
			@RequestParam(value="pageNum", defaultValue="1") int pageNum,
			@RequestParam(value="type", defaultValue="null") String type,
			@RequestParam(value="keyword", defaultValue="null") String keyword) {
		
		// 사용자가 입력한 비밀번호가 틀리면 자바스크립트로 응답
		boolean isPassCheck = boardService.isPassCheck(no, pass);		
		if(! isPassCheck) {
			response.setContentType("text/html; charset=utf-8");				
			out.println("<script>");
			out.println("	alert('비밀번호가 맞지 않습니다.');");
			out.println("	history.back();");
			out.println("</script>");
			
			return null;
		}		
		
		// 게시 글 수정 폼 요청은 읽은 횟수를 증가시키지 않는다.
		Board board = boardService.getBoard(no, false);
		
		// 현재 요청이 검색 요청인지 여부를 판단하는 searchOption 설정
		boolean searchOption = (type.equals("null") 
				|| keyword.equals("null")) ? false : true; 
				
		// no에 해당하는 게시글 정보와 pageNum, searchOption을 모델에 저장한다.
		model.addAttribute("board", board);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("searchOption", searchOption);
		
		// 검색 요청이면 type과 keyword를 모델에 저장한다.
		if(searchOption) {
			model.addAttribute("type", type);
			model.addAttribute("keyword", keyword);
		}
		return "views/updateForm";		
	}

	/* 게시글 수정 폼에서 들어오는 게시글 수정 요청을 처리하는 메서드
	 * "/update"로 들어오는 HTTP POST 요청을 처리하는 메서드
	 **/
	@PostMapping("/update")
	public String updateBoard(Board board, RedirectAttributes reAttrs,
			HttpServletResponse response, PrintWriter out,
			@RequestParam("no") int no, @RequestParam("pass") String pass,
			@RequestParam(value="pageNum", defaultValue="1") int pageNum,
			@RequestParam(value="type", defaultValue="null") String type,
			@RequestParam(value="keyword", defaultValue="null") String keyword) {
		
		// 사용자가 입력한 비밀번호가 틀리면 자바스크립트로 응답
		boolean isPassCheck = boardService.isPassCheck(board.getNo(), board.getPass());		
		if(! isPassCheck) {
			response.setContentType("text/html; charset=utf-8");				
			out.println("<script>");
			out.println("	alert('비밀번호가 맞지 않습니다.');");
			out.println("	history.back();");
			out.println("</script>");
			
			return null;
		}
				
		// 비밀번호가 맞으면 DB 테이블에서 no에 해당하는 게시글 정보를 수정한다.		
		boardService.updateBoard(board);

		// 현재 요청이 검색 요청인지 여부를 판단하는 searchOption 설정
		boolean searchOption = (type.equals("null") 
				|| keyword.equals("null")) ? false : true;
		
		// RedirectAttributs의 addAttribute() 메서드를 사용해 파라미터 설정  
		reAttrs.addAttribute("searchOption", searchOption);
		reAttrs.addAttribute("pageNum", pageNum);
		
		// 검색 요청이면 type과 keyword를 모델에 저장한다.
		if(searchOption) {			
			
			/* Redirect 되는 경우 주소 끝에 파라미터를 지정해 GET방식의 파라미터로
			 * 전송할 수 있지만 스프링프레임워크가 지원하는 RedirectAttributs객체를
			 * 이용하면 한 번만 사용할 임시 데이터와 지속적으로 사용할 파라미터를 구분해
			 * 지정할 수 있다.
			 * 
			 * 리다이렉트 될 때 필요한 파라미터를 스프링이 제공하는 RedirectAttributs의
			 * addAttribute() 메서드를 사용해 파라미터를 지정하면 자동으로 주소 뒤에 
			 * 요청 파라미터로 추가되며 파라미터에 한글이 포함되는 경우 URLEncoding을
			 * java.net 패키지의 URLEncoder 클래스를 이용해 인코딩을 해줘야 하지만
			 * application.properties에 인코딩 관련 설정이 되어 있기 때문에 별도로
			 * 처리할 필요가 없다. 
			 * 
			 * 다음은 검색 리스트로 Redirect 하면서 같이 보내야할 keyword와 type을
			 * RedirectAttributs를 이용해 파라미터로 설정하고 있다. 
			 **/
			reAttrs.addAttribute("type", type);
			reAttrs.addAttribute("keyword", keyword);				
		}
		
		/* 게시 글 수정이 완료되면 게시 글 리스트로 리다이렉트 시킨다.
		 * 클라이언트 요청을 처리한 후 리다이렉트 해야 할 경우 아래와 같이 redirect:
		 * 접두어를 붙여 뷰 이름을 반환하면 된다. 뷰 이름에 redirect 접두어가 붙으면
		 * HttpServletResponse를 사용해서 지정한 경로로 Redirect 된다.
		 **/ 
		return "redirect:boardList";
	}
	
	/* 게시 글 삭제 요청을 처리 메서드
	 *	"/delete"로 들어오는 HTTP POST 요청을 처리하는 메서드 
	 **/
	@PostMapping("/delete")
	public String deleteBoard(RedirectAttributes reAttrs,
			HttpServletResponse response, PrintWriter out,
			@RequestParam("no") int no, @RequestParam("pass") String pass,
			@RequestParam(value="pageNum", defaultValue="1") int pageNum,
			@RequestParam(value="type", defaultValue="null") String type,
			@RequestParam(value="keyword", defaultValue="null") String keyword) {
		
		// 사용자가 입력한 비밀번호가 틀리면 자바스크립트로 응답
		boolean isPassCheck = boardService.isPassCheck(no, pass);		
		if(! isPassCheck) {
			response.setContentType("text/html; charset=utf-8");				
			out.println("<script>");
			out.println("	alert('비밀번호가 맞지 않습니다.');");
			out.println("	history.back();");
			out.println("</script>");
			
			return null;
		}		
		
		// 비밀번호가 맞으면 DB 테이블에서 no에 해당하는 게시글을 삭제한다.
		boardService.deleteBoard(no);
		
		// 현재 요청이 검색 요청인지 여부를 판단하는 searchOption 설정
		boolean searchOption = (type.equals("null") 
				|| keyword.equals("null")) ? false : true; 
		
		// RedirectAttributes를 이용해 리다이렉트 할 때 필요한 파라미터 설정 
		reAttrs.addAttribute("pageNum", pageNum);
		reAttrs.addAttribute("searchOption", searchOption);
		
		// 검색 요청이면 type과 keyword를 모델에 저장한다.
		if(searchOption) {			
			
			/* 다음은 검색 리스트로 Redirect 하면서 같이 보내야할 keyword와 type을
			 * RedirectAttributs를 이용해 파라미터로 지정하고 있다. 
			 **/
			reAttrs.addAttribute("type", type);
			reAttrs.addAttribute("keyword", keyword);			
		}
		
		// 게시 글 삭제가 완료되면 게시글 리스트로 리다이렉트 시킨다.
		return "redirect:boardList";		
	}

	// 게시글 상세보기에서 들어오는 파일 다운로드 요청을 처리하는 메서드	
	@GetMapping("/fileDownload")
	public void download(HttpServletRequest request, 
			HttpServletResponse response) throws Exception {

		String fileName = request.getParameter("fileName");
		log.info("fileName : " + fileName);
		
		File parent = new File(DEFAULT_PATH);		
		File file = new File(parent.getAbsolutePath(), fileName);
		log.info("file.getName() : " + file.getName());
		
		// 응답 데이터에 파일 다운로드 관련 컨텐츠 타입 설정이 필요하다.
		response.setContentType("application/download; charset=UTF-8");
		response.setContentLength((int) file.length());
		
		// 한글 파일명을 클라이언트로 바로 내려보내기 때문에 URLEncoding이 필요하다.
		fileName = URLEncoder.encode(file.getName(), "UTF-8");
		log.info("다운로드 fileName : " + fileName);
		
		// 전송되는 파일 이름을 한글 그대(원본 파일 이름 그대로)로 보내주기 위한 설정이다.
		response.setHeader("Content-Disposition", 
				"attachment; filename=\"" + fileName + "\";");
		
		// 파일로 전송되야 하므로 전송되는 데이터 인코딩은 바이너리로 설정해야 한다.
		response.setHeader("Content-Transfer-Encoding", "binary");
		
		// 파일을 클라이언트로 보내기 위해 응답 스트림을 구한다.
		OutputStream out = response.getOutputStream();
		FileInputStream fis = null;
		
		/* FileInputStream을 통해 클라이언트로 보낼 파일을 읽어
		 * 스프링이 제공하는 FileCopyUtils 클래스를 통해서
		 * 데이터를 읽고 응답 스트림을 통해 클라이언트로 출력한다.
		 **/
		fis = new FileInputStream(file);

		// 스프링이 제공하는 FileCopyUtils를 이용해 응답 스트림에 파일을 복사한다.
		FileCopyUtils.copy(fis,  out);
		
		if(fis != null) {
			fis.close();
		}
		
		/* 파일 데이터를 클라이언트로 출력한다.
		 * 버퍼에 남아 있는 모든 데이터를 클라이언드로 출력한다.
		 **/
		out.flush();	
	}	
}
