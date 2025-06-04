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

/* Spring MVC Controller 클래스 임을 정의
 * @Controller 애노테이션이 적용된 클래스의 메서드는 기본적으로 뷰의 이름을 반환한다.
 **/
@Controller
@Slf4j
public class BoardController {
	
	@Autowired
	private BoardService boardService;	
	
	/* 게시글 리스트 요청을 처리하는 메서드
	 * "/", "/boardList" 로 들어오는 HTTP GET 요청을 처리하는 메서드
	 * 
	 * 스프링은 컨트롤러에서 모델에 데이터를 담을 수 있는 다양한 방법을 제공하며
	 * 아래와 같이 파라미터에 Model을 지정하는 방식이 많이 사용된다. 
	 * @RequestMapping 애노테이션이 적용된 메서드의 파라미터에 Model
	 * 을 지정하면 스프링이 이 메서드를 호출하면서 Model 타입의 객체를 넘겨준다.
	 * 우리는 Model을 받아 이 객체에 결과 데이터를 담기만 하면 뷰에서 사용할 수 있다.
	 **/
	@GetMapping({"/", "/boardList"})
	public String boardList(Model model) {		
		model.addAttribute("bList", boardService.boardList());

		/* application.properties 파일에서 Thymeleaf 설정을 별도로 지정하지
		 * 않았기 때문에 다음과 같이 기본 설정이 적용되어 templates/ 폴더를 
		 * 기준으로 뷰를 찾아서 포워드 되고 제어가 뷰 페이지로 이동한다.
		 *  
		 * spring.thymeleaf.prefix=classpath:/templates/
		 * spring.thymeleaf.suffix=.html  
		 **/
		//return "main";
		
		// 페이지 모듈화로 content 페이지가 "/templates/views" 폴더로 이동함
		return "views/boardList";
	}
	
	/* 게시글 상세보기 요청 처리 메서드
	 * "/boardDetail"로 들어오는 HTTP GET 요청을 처리하는 메서드
	 * 
	 * 스프링은 클라이언트로부터 넘어 오는 요청 파라미터를 받을 수 있는 여러 가지
	 * 방법을 제공하고 있다. 아래와 같이 HTTP 요청을 처리하는 메서드의 파라미터
	 * 앞에 @RequestParam("요청 파라미터 이름") 애노테이션을 사용해 요청
	 * 파라미터의 이름을 지정하면 된다.
	 *
	 * @GetMapping 애노테이션이 적용된 Controller 메서드의 파라미터에
	 * @RequestParam 애노테이션을 사용해 요청 파라미터 이름을 지정하면 
	 * 이 애노테이션이 앞에 붙은 매개변수에 요청 파라미터 값을 바인딩 시켜준다.
	 *
	 * @RequestParam 애노테이션에 사용할 수 있는 속성은 아래와 같다.
	 * value : HTTP 요청 파라미터의 이름을 지정한다.
	 * required : 요청 파라미터가 필수인지 설정하는 속성으로 기본값은 true 이다.
	 * 			이 값이 true인 상태에서 요청 파라미터의 값이 존재하지 않으면
	 * 			스프링은 Exception을 발생시킨다.
	 * 
	 * defaultValue : 요청 파라미터가 없을 경우 사용할 기본 값을 문자열로 지정한다.
	 * 
	 * @RequestParam(value="no" required=false defaultValue="1")
	 * 
	 * @RequestParam 애노테이션은 요청 파라미터 값을 읽어와 Controller 메서드의
	 * 파라미터 타입에 맞게 변환해 준다. 만약 요청 파라미터를 Controller 메서드의 
	 * 파라미터 타입으로 변환할 수 없는 경우 스프링은 400 에러를 발생시킨다.
	 **/
	@GetMapping("/boardDetail")
	public String getBoard(Model model, @RequestParam("no") int no) {
		model.addAttribute("board", boardService.getBoard(no));
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
	 *
	 * @PostMapping 애노테이션이 적용된 Controller 메서드의 파라미터에
	 * 게시글 쓰기 폼에서 전달되는 요청 파라미터를 받을 Board 객체를
	 * 지정했다.
	 *  
	 * 스프링은 폼으로부터 전달된 파라미터를 객체로 처리 할 수 있는 아래와 같은
	 * 방법을 제공하고 있다. 아래와 같이 요청 파라미터를 전달받을 때 사용하는 
	 * 객체를 커맨드 객체라고 부르며 이 커맨드 객체는 자바빈 규약에 따라 프로퍼티에
	 * 대한 setter를 제공하도록 작성해야 한다. 그리고 파라미터 이름이 커맨드 객체의
	 * 프로퍼티와 동일하도록 폼 컨트롤의 name 속성을 지정해야 한다.
	 * 
	 * HTTP 요청을 맵핑하는 애노테이션(@PostMapping, @GetMapping 등)이
	 * 적용된 컨트롤러의 메서드에 커맨드 객체를 파라미터로 지정하면 커맨드 객체의
	 * 프로퍼티와 동일한 이름을 가진 요청 파라미터의 데이터를 스프링이 자동으로
	 * 연결해 준다. 이때 스프링은 자바빈 규약에 따라 적절한 setter 메서드를 사용해
	 * 값을 설정한다.
	 * 
	 * 커맨드 객체의 프로퍼티와 일치하는 파라미터 이름이 없다면 기본 값으로 설정된다.
	 * 또한 프로퍼티의 데이터 형에 맞게 적절히 형 변환 해 준다. 형 변환을 할 수 없는
	 * 경우 스프링은 400 에러를 발생 시킨다. 예를 들면 프로퍼티가 정수형 일 때 매칭
	 * 되는 값이 정수로 변환 할 수 없는 경우 400 에러를 발생 시킨다.
	 **/
	@PostMapping("/addBoard")	
	public String addBoard(Board board) {
		log.info("title : ", board.getTitle());
		boardService.addBoard(board);
		
		/* 게시글 쓰기가 완료되면 게시글 리스트로 리다이렉트 시킨다.
		 * 클라이언트 요청을 처리한 후 리다이렉트 해야 할 경우 아래와 같이 redirect:
		 * 접두어를 붙여 뷰 이름을 반환하면 된다. 뷰 이름에 redirect 접두어가 붙으면
		 * HttpServletResponse를 사용해서 지정한 경로로 Redirect 된다.
		 * redirect 접두어 뒤에 경로를 지정할 때 "/"로 시작하면 ContextRoot를
		 * 기준으로 절대 경로 방식으로 Redirect 된다. "/"로 시작하지 않으면 현재 
		 * 경로를 기준으로 상대 경로로 Redirect 된다. 또한 다른 사이트로 Redirect
		 * 되기를 원한다면 "redirect:http://사이트 주소"를 지정하면 된다.
		 **/
		return "redirect:boardList";
	}

	/* 게시글 수정 폼 요청을 처리하는 메서드
	 * "/updateForm"으로 들어오는 HTTP POST 요청을 처리하는 메서드	 
	 * 
	 * @GetMapping 애노테이션이 적용된 Controller 메서드의 파라미터에 PrintWriter와 
	 * HttpServletResponse를 지정했고 요청 파라미터를 받을 no와 pass도 지정했다. 
	 * 이렇게 Controller 메서드의 파라미터에 필요한 객체나 요청 파라미터 이름과 동일한
	 * 이름의 파라미터를 지정하면 스프링이 자동으로 연결해 준다. 만약 요청 파라미터와
	 * 메서드의 파라미터 이름이 다른 경우 Controller 메서드의 파라미터 앞에 
	 * @RequestParam("요청 파라미터 이름")을 사용해 요청 파라미터의 이름을 지정하면
	 * 스프링이 데이터 형에 맞게 적절히 형 변환까지 해 준다. 형 변환을 할 수 없는 경우
	 * 스프링은 400 에러를 발생 시킨다. 예를 들면 Controller 메서드의 파라미터가 정수형
	 * 일 때 요청 파라미터의 값이 정수형으로 형 변환 할 수 없는 경우 400 에러를 발생 시킨다.
	 **/
	@PostMapping("/updateForm")
	public String updateBoard(Model model, 
			HttpServletResponse response, PrintWriter out,
			@RequestParam("no") int no, @RequestParam("pass") String pass) {
		
		// 사용자가 입력한 비밀번호가 틀리면 자바스크립트로 응답
		boolean isPassCheck = boardService.isPassCheck(no, pass);		
		if(! isPassCheck) {
			response.setContentType("text/html; charset=utf-8");				
			out.println("<script>");
			out.println("	alert('비밀번호가 맞지 않습니다.');");
			out.println("	history.back();");
			out.println("</script>");

			// null을 반환하면 위에서 스트림에 출력한 자바스크립트 코드가 응답된다. 
			return null;
		}		
		
		// 비밀번호가 맞으면 no에 해당하는 게시글 정보를 모델에 담아 수정 폼으로 이동
		Board board = boardService.getBoard(no);
		model.addAttribute("board", board);
		return "views/updateForm";
	}
	
	
	/* 게시글 수정 폼에서 들어오는 게시글 수정 요청을 처리하는 메서드
	 * "/update"로 들어오는 HTTP POST 요청을 처리하는 메서드
	 * 
	 * @PostMapping 애노테이션이 적용된 Controller 메서드의 파라미터에
	 * HttpServletResponse와 PrintWriter를 지정했고 요청 파라미터를 받을
	 * Board 객체를 지정했다.
	 *  
	 * 스프링은 폼으로부터 전달된 파라미터를 객체로 처리 할 수 있는 아래와 같은
	 * 방법을 제공하고 있다. 아래와 같이 요청 파라미터를 전달받을 때 사용하는 
	 * 객체를 커맨드 객체라고 부르며 이 커맨드 객체는 자바빈 규약에 따라 프로퍼티에
	 * 대한 setter를 제공하도록 작성해야 한다. 그리고 파라미터 이름이 커맨드 객체의
	 * 프로퍼티와 동일하도록 폼 컨트롤의 name 속성을 지정해야 한다.
	 * 
	 * HTTP 요청을 맵핑하는 애노테이션(@PostMapping, @GetMapping 등)이
	 * 적용된 컨트롤러의 메서드에 커맨드 객체를 파라미터로 지정하면 커맨드 객체의
	 * 프로퍼티와 동일한 이름을 가진 요청 파라미터의 데이터를 스프링이 자동으로
	 * 연결해 준다. 이때 스프링은 자바빈 규약에 따라 적절한 setter 메서드를 사용해
	 * 값을 설정한다.
	 * 
	 * 커맨드 객체의 프로퍼티와 일치하는 파라미터 이름이 없다면 기본 값으로 설정된다.
	 * 또한 프로퍼티의 데이터 형에 맞게 적절히 형 변환 해 준다. 형 변환을 할 수 없는
	 * 경우 스프링은 400 에러를 발생 시킨다. 예를 들면 프로퍼티가 정수형 일 때 매칭
	 * 되는 값이 정수로 변환 할 수 없는 경우 400 에러를 발생 시킨다.
	 **/
	@PostMapping("/update")
	public String updateBoard(Board board,
			HttpServletResponse response, PrintWriter out) {
		
		// 사용자가 입력한 비밀번호가 틀리면 자바스크립트로 응답
		boolean isPassCheck = boardService.isPassCheck(board.getNo(), board.getPass());		
		if(! isPassCheck) {
			response.setContentType("text/html; charset=utf-8");				
			out.println("<script>");
			out.println("	alert('비밀번호가 맞지 않습니다.');");
			out.println("	history.back();");
			out.println("</script>");
			
			// null을 반환하면 위에서 스트림에 출력한 자바스크립트 코드가 응답된다.
			return null;
		}
				
		/* 비밀번호가 맞으면 DB 테이블에서 no에 해당하는 
		 * 게시글 정보를 수정하고 게시글 리스트로 리다이렉트
		 **/		
		boardService.updateBoard(board);		
		return "redirect:boardList";
	}
	
	/* 게시글 삭제 요청을 처리 메서드
	 *	"/delete"로 들어오는 HTTP POST 요청을 처리하는 메서드 
	 **/
	@PostMapping("/delete")
	public String deleteBoard(
			HttpServletResponse response, PrintWriter out,
			@RequestParam("no") int no, @RequestParam("pass") String pass) {
		
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
		
		/* 비밀번호가 맞으면 DB 테이블에서 no에 해당하는 
		 * 게시글을 삭제하고 게시글 리스트로 리다이렉트
		 **/
		boardService.deleteBoard(no);
		return "redirect:boardList";
	}
}
