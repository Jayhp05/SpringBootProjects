package com.springbootstudy.bbs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.springbootstudy.bbs.domain.Board;
import com.springbootstudy.bbs.mapper.BoardMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Service
@Slf4j
public class BoardService {

	@Autowired
	private BoardMapper boardMapper;
	
//	게시글 삭제 요청을 처리하는 메서드 
	public void deleteBoard(int no) {
		boardMapper.deleteBoard(no);
	}
	
//	사용자가 입력한 게시글 비번하고 no에 해당하는 비번 맞는지 체크하느 메서드
	public boolean isPassCheck(int no, String pass) {
		boolean result = false;
		String dbPass = boardMapper.isPassCheck(no);
		if(dbPass.equals(pass)) {
			result = true;
		}
		
		 return result;
	}
	
	// 게시글을 수정하는 메서드
	public void updateBoard(Board board) {
		 log.info("BoardService: updateBoard(Board board)");
		 boardMapper.updateBoard(board);
	}
	
//	게시글 등록 요청을 처리하는 메서드
	public void addBoard(Board board) {
		log.info("BoardService: addBoard(Board board)");
		
		boardMapper.insertBoard(board);
	}
	
//	no에 해당하는 게시글 상세보기 요청을 처리하는 메서드
	public Board getBoard(int no) {
		return boardMapper.getBoard(no);
	}
	
//	게시글 리스트 요청을 처리하는 메서드
	public List<Board> boardList() {
		
		log.info("BoardService: boardList()");
		
		return boardMapper.boardList();
	}
}
