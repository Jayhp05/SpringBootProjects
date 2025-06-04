package com.springbootstudy.bbs.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	private static final int PAGE_SIZE = 10;
	private static final int PAGE_GROUP = 10;

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
	public Board getBoard(int no, boolean isCount) {
		if(isCount) {
			boardMapper.incrementReadCount(no);
		}
		
		return boardMapper.getBoard(no);
	}
	
//	한 페이지에 해당하는 게시글 리스트 요청을 처리하는 메서드
	public Map<String, Object> boardList(int pageNum) {
		
		log.info("BoardService: boardList()");
		
		int currentPage = pageNum;
		
//		1page = 0, 2page = 10, 3page = 20, ..... 10page = 90
		int startRow = (currentPage - 1) * PAGE_SIZE;
		
//		전체 페이지 수 계산 = 전체 게시글의 수 / 페이디 당 게시글 수 -+ 보정(나머지가 있는지 없는지 확인)
		int listCount = boardMapper.getBoardCount();
		int pageCount = listCount/PAGE_SIZE + (listCount % PAGE_SIZE == 0 ? 0 : 1);
		
		int startPage = (currentPage / PAGE_GROUP) * PAGE_GROUP + 1
						- (currentPage % PAGE_GROUP == 0 ? PAGE_GROUP : 0);
		
		int endPage = startPage + PAGE_GROUP - 1; 
		
//		pageCount = 25 [21] ~ [30]
		if(endPage > pageCount) {
			endPage = pageCount;
		}
		
		List<Board> bList = boardMapper.boardList(startRow, PAGE_SIZE);
		
		Map<String, Object> modelMap = new HashMap<>();
		modelMap.put("bList", bList);
		modelMap.put("pageCount", pageCount);
		modelMap.put("startPage", startPage);
		modelMap.put("endPage", endPage);
		modelMap.put("currentPage", currentPage);
		modelMap.put("pageGroup", PAGE_GROUP);
		modelMap.put("listCount", listCount);
		
		
		return modelMap;
	}
}
