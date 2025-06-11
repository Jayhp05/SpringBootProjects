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
	
	@PostMapping("/joinResult")
	public String joinResult(Model model, Member member,
			@RequestParam("pass1") String pass1, 
			@RequestParam("emailId") String emailId, 
			@RequestParam("emailDomain") String emailDomain,
			@RequestParam("mobile1") String mobile1, 
			@RequestParam("mobile2") String mobile2, 
			@RequestParam("mobile3") String mobile3,
			@RequestParam("phone1") String phone1, 
			@RequestParam("phone2") String phone2, 
			@RequestParam("phone3") String phone3,
			@RequestParam(value="emailGet", required=false, 
			defaultValue="false")boolean emailGet) {
			
		member.setPass(pass1);
		member.setEmail(emailId + "@" + emailDomain);
		member.setMobile(mobile1 + "-" + mobile2 + "-" + mobile3);
		  	
		if(phone2.equals("") || phone3.equals("")) {
			member.setPhone("");
		} 
		else {
			member.setPhone(phone1 + "-" + phone2 + "-" + phone3);
		}
		  	
		member.setEmailGet(Boolean.valueOf(emailGet));
		  	
		// MemberService를 통해서 회원 가입 폼에서 들어온 데이터를 DB에 저장한다.
		memberService.addMember(member);
		  	
		// 로그인 폼으로 리다이렉트 시킨다.
		return "redirect:loginForm";
	}
	
	@GetMapping("/overlapIdCheck")
	public String overlapIdCheck(Model model, @RequestParam("id") String id) {
		
		// 회원 아이디 중복 여부를 받아 온다.
		boolean overlap = memberService.overlapIdCheck(id);
		
		model.addAttribute("id", id);
		model.addAttribute("overlap", overlap);
		
		return "member/overlapIdCheck";
	}
	
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
