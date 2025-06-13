package com.springbootstudy.bbs.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.springbootstudy.bbs.domain.Member;

@Mapper
public interface MemberMapper {
	
	// 회원 정보를 회원 테이블에서 수정하는 메서드
	public void updateMember(Member member);

	// 회원 정보 수정 시에 기존 비밀번호가 맞는지 체크하는 메서드
	String memberPassCheck(String id);
	
	Member getMember(String id); 
	
	// 회원 정보를 회원 테이블에 저장하는 메서드
	void addMember(Member member);
}