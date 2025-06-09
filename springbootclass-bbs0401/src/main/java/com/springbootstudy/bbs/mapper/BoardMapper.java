package com.springbootstudy.bbs.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.springbootstudy.bbs.domain.Board;

@Mapper
public interface BoardMapper {
	
	// no에 해당하는 게시 글의 읽은 횟수를 증가시키는 메서드
	void incrementReadCount(int no);
	
	// DB에서 no에 해당하는 게시 글을 삭제하는 메서드
	void deleteBoard(int no);
	
	// 게시글을 DB 테이블에서 수정하는 메서드
	void updateBoard(Board board);
	
	// no에 해당하는 게시 글의 비밀번호를 읽어와 반환하는 메서드
	String isPassCheck(int no);
	
	// 게시글을 DB 테이블에 추가하는 메서드
	void insertBoard(Board board);
	
	// 게시글 상세보기 - no에 해당하는 게시글 하나를 읽어와 반환하는 메서드 proxy
	Board getBoard(int no);
	
	// 전체 게시글 리스트를 읽어와 반환하는 메서드	
	List<Board> boardList(
			@Param("startRow") int startRow, @Param("num") int num,
			@Param("type") String type, @Param("keyword") String keyword);
	
	int getBoardCount(
			@Param("type") String type, @Param("keyword") String keyword); 
}




