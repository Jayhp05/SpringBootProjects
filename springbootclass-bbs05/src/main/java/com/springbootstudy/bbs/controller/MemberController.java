package com.springbootstudy.bbs.controller;

import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.springbootstudy.bbs.domain.Member;
import com.springbootstudy.bbs.service.MemberService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@SessionAttributes("member")
public class MemberController {

	@Autowired
	private MemberService memberService;
	
//	로그인 요청을 받는 메서드
	@PostMapping("/login")
	public String login(Model model, @RequestParam("userId") String id, @RequestParam("pass") String pass,
						HttpSession session, HttpServletResponse response, PrintWriter out) {
		
//		서비스를 통해서 로그인 처리
		int result = memberService.login(id, pass);
		
//		회원 아이디가 없음, 비밀번호가 틀림 - 경고창 - 로그인 폼으로
		if(result == -1) { // 회원 아이디 없음
			response.setContentType("text/html; charset=utf-8");
			out.println("<script>");
			out.println("	alert('존재하지 않는 아이디 입니다.');");
			out.println("	history.back();");
			out.println("</script>");
			
			return null;
		}
		else if(result == 0) { // 비밀번호가 틀림
			response.setContentType("text/html; charset=utf-8");
			out.println("<script>");
			out.println("	alert('비밀번호가 다릅니다.');");
			out.println("	location.href='loginForm'");
			out.println("</script>");
			
			return null;
		}
		
//		로그인 성공 - 세션에 필요한 데이터(isLogin = true, 회원 아이디 저장) 저장
		Member member = memberService.getMember(id);
		session.setAttribute("isLogin", true);
//		session.setAttribute("member", member);
		model.addAttribute("member", member);
		
		return "redirect:boardList";
	}
	
	@GetMapping("/memberLogout")
//	@RequestMapping(value="/memberLogout", method=RequestMethod.GET) request맵핑은 따로 지정하지 않으면 get과 post 둘 다 받아옴.
	public String logout(HttpSession session) {
		
//		세션을 초기화(다시 시작)
		session.invalidate();
		
		return "redirect:loginForm";
	}
}
