package com.springbootstudy.bbs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springbootstudy.bbs.domain.Member;
import com.springbootstudy.bbs.mapper.MemberMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MemberService {

	@Autowired
	private MemberMapper memberMapper;
	
	public boolean overlapIdCheck(String id) {
		Member member = memberMapper.getMember(id);
		
		log.info("overlapIdCheck - member : " + member);
		
		if(member == null) { 
			return false;
		}
		
		return true;
	}
	
	//	회원 로그인, 회원 가입시 암호화 처리
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public void updateMember(Member member) {
		// BCryptPasswordEncoder 객체를 이용해 비밀번호를 암호화한 후 저장
		member.setPass(passwordEncoder.encode(member.getPass()));
		log.info(member.getPass());
		memberMapper.updateMember(member);
	}
	
	public void addMember(Member member) {
		member.setPass(passwordEncoder.encode(member.getPass()));
		
		log.info(member.getPass());
		
		memberMapper.addMember(member);
	}
	
	public boolean memberPassCheck(String id, String pass) {
		String dbPass = memberMapper.memberPassCheck(id);
		
		boolean result = false;
		
		if(passwordEncoder.matches(pass, dbPass)) {
			result = true;
		}
		
		return result;
	}
	
	public int login(String id, String pass) {
		int result = -1;
		Member m = memberMapper.getMember(id);
		
		if(m == null) {
			return result;
		}
		
		if(passwordEncoder.matches(pass, m.getPass())) {
			result = 1;
		}
		else {
			result = 0;
		}
		
		return result;
	}
	
	public Member getMember(String id) {
		return memberMapper.getMember(id);
	}
}
