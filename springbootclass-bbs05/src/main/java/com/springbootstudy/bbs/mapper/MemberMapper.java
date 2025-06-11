package com.springbootstudy.bbs.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.springbootstudy.bbs.domain.Member;

@Mapper
public interface MemberMapper {

	public Member getMember(String id); 
	
	// 회원 정보를 회원 테이블에 저장하는 메서드
	public void addMember(Member member);
}