package com.springbootstudy.bbs.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.springbootstudy.bbs.domain.Board;

//@Repository
public class BoardDao {
	
	private static final String NAME_SPACE = 
							"com.springbootstudy.bbs.mapper.BoardMapper";
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	// 게시글 상세보기 - no에 해당하는 게시글 하나를 읽어와 반환하는 메서드 proxy
	public Board getBoard(int no) {
		return sqlSession.selectOne(NAME_SPACE + ".getBoard", no);
	}
	
	// 전체 게시글 리스트를 읽어와 반환하는 메서드	
	public List<Board> boardList(int startRow,  int num) {
		
		Map<String, Integer> params = new HashMap<>();
		params.put("startRow", startRow);
		params.put("num", num);
		return sqlSession.selectList(NAME_SPACE + ".boardList", params);
	}
	
	public int getBoardCount() {
		return sqlSession.selectOne(NAME_SPACE + ".getBoardCount");
	} 

}
