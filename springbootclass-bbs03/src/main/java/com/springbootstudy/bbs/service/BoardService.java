package com.springbootstudy.bbs.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		
	// 한 페이지에 출력할 게시글의 수를 상수로 선언
	private static final int PAGE_SIZE = 10;
	
	/* 한 페이지에 출력할 페이지 그룹의 수를 상수로 선언
	 * [이전] 1 2 3 4 5 6 7 8 9 10 [다음]
	 **/
	private static final int PAGE_GROUP = 10;	
	
	/* 한 페이지에 출력할 게시글 리스트 또는 검색 리스트와 
	 * 페이징 처리에 필요한 데이터를 Map 객체로 반환하는 메서드 
	 **/
	public Map<String, Object> boardList(int pageNum, String type, String keyword) {
		log.info("boardList(int pageNum, String type, String keyword)");
		
		int currentPage = pageNum;

		int startRow = (currentPage - 1) * PAGE_SIZE;
		log.info("startRow : " + startRow);
		
		boolean searchOption = (type.equals("null") 
				|| keyword.equals("null")) ? false : true; 
				
		int listCount = boardMapper.getBoardCount(type, keyword);
		
		List<Board> boardList = boardMapper.boardList(startRow, PAGE_SIZE, type, keyword);		
			
		int pageCount = 
				listCount / PAGE_SIZE + (listCount % PAGE_SIZE == 0 ? 0 : 1);
		
		int startPage = (currentPage / PAGE_GROUP) * PAGE_GROUP + 1
				- (currentPage % PAGE_GROUP == 0 ? PAGE_GROUP : 0);		
					
		// 현재 페이지 그룹의 마지막 페이지 : 10, 20, 30...
		int endPage = startPage + PAGE_GROUP - 1;			
		
		if(endPage > pageCount) {
			endPage = pageCount;
		}	
		
		Map<String, Object> modelMap = new HashMap<String, Object>();		
		
		modelMap.put("bList", boardList);
		modelMap.put("pageCount", pageCount);
		modelMap.put("startPage", startPage);
		modelMap.put("endPage", endPage);
		modelMap.put("currentPage", currentPage);
		modelMap.put("listCount", listCount);
		modelMap.put("pageGroup", PAGE_GROUP);
		modelMap.put("searchOption", searchOption);

		// 검색 요청이면 type과 keyword를 모델에 저장한다.
		if(searchOption) {
			modelMap.put("type", type);
			modelMap.put("keyword", keyword);
		}
		
		return modelMap;
	}
	
	/* no에 해당하는 게시글을 읽어와 반환하는 메서드
	 *  
 	 * isCount == true : 게시 상세보기 요청, false : 그 외 요청임 
	 **/
	public Board getBoard(int no, boolean isCount) {	
		log.info("BoardService: getBoard(int no, boolean isCount)");
		
		// 게시글 상세보기 요청만 게시글 읽은 횟수를 증가시킨다.
		if(isCount) {
			boardMapper.incrementReadCount(no);
		}
		return boardMapper.getBoard(no);
	}
	
	// 게시글 정보를 추가하는 메서드
	public void addBoard(Board board) {
		log.info("BoardService: insertBoard(Board board)");
		boardMapper.insertBoard(board);
	}

	public boolean isPassCheck(int no, String pass) {	
		log.info("BoardService: isPassCheck(int no, String pass)");
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
	
	// no에 해당하는 게시글을 삭제하는 메서드
	public void deleteBoard(int no) {
		log.info("BoardService: deleteBoard(int no)");
		boardMapper.deleteBoard(no);
	}	
}
