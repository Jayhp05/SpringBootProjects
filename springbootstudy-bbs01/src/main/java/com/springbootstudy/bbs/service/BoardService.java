package com.springbootstudy.bbs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springbootstudy.bbs.domain.Board;
import com.springbootstudy.bbs.mapper.BoardMapper;

import lombok.extern.slf4j.Slf4j;

// BoardService 클래스가 서비스 계층의 스프링 빈(Bean) 임을 정의 
@Service
@Slf4j
public class BoardService {
	
	// DB 작업에 필요한 BoardMapper 객체를 의존성 주입 설정 
	@Autowired
	private BoardMapper boardMapper;
	
	// 전체 게시글을 읽어와 반환하는 메서드
	public List<Board> boardList() {
		log.info("BoardService: boardList()");
		return boardMapper.boardList();
	}
	
	// no에 해당하는 게시글을 읽어와 반환하는 메서드
	public Board getBoard(int no) {	
		log.info("BoardService: getBoard(int no)");
		return boardMapper.getBoard(no);
	}
	
	// 게시글 정보를 추가하는 메서드
	public void addBoard(Board board) {
		log.info("BoardService: addBoard(Board board)");
		boardMapper.insertBoard(board);
	}

	/* 게시글 수정과 삭제 할 때 비밀번호가 맞는지 체크하는 메서드	
	 * 
	 * - 게시글의 비밀번호가 맞으면 : true를 반환
	 * - 게시글의 비밀번호가 맞지 않으면 : false를 반환
	 **/
	public boolean isPassCheck(int no, String pass) {	
		log.info("BoardService: isPassCheck(int no, String pass)");
		boolean result = false;
		
		// BoardDao를 이용해 DB에서 no에 해당하는 비밀번호를 읽어온다.
		String dbPass = boardMapper.isPassCheck(no);
		
		if(dbPass.equals(pass)) {
			result = true;		
		}
		
		// 비밀번호가 맞으면 true, 맞지 않으면 false가 반환된다.
		return result;
	}	

	// 게시글을 수정하는 메서드
	public void updateBoard(Board board) {
		log.info("BoardService: updateBoard(Board board)");
		boardMapper.updateBoard(board);
	}
	
	// no에 해당하는 게시글을 삭제하는 메서드
	public void deleteBoard(int no) {
		log.info("BoardService: deleteBoard(int no)");
		boardMapper.deleteBoard(no);
	}	
}
