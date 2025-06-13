package com.springbootstudy.bbs.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.springbootstudy.bbs.dao.BoardDao;
import com.springbootstudy.bbs.domain.Board;
import com.springbootstudy.bbs.domain.Reply;
import com.springbootstudy.bbs.mapper.BoardMapper;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class BoardService {
	
	private static final int PAGE_SIZE = 10;
	private static final int PAGE_GROUP = 10;
	
	@Autowired
	private BoardMapper boardMapper;
	
	// 추천/땡큐 정보를 업데이트하고 갱신된 추천/땡큐를 가져오는 메서드
	public Map<String, Integer> recommend(int no, String recommend) {
		
		// 추천/땡튜를 증가시킨다.
		boardMapper.updateRecommend(no, recommend); 
		Board board = boardMapper.getRecommend(no);
		
		Map<String, Integer> map = new HashMap<String, Integer>(); 
		map.put("recommend", board.getRecommend());
		map.put("thank", board.getThank());
		
		// 최신 추천/떙큐를 읽어와서 맵에 추가
		return map;
	}
	
	// 게시글 번호에 해당하는 댓글 리스트 요청을 처리하는 메서드
	public List<Reply> replyList(int no) {
		return boardMapper.replyList(no);
	}
	
	// 게시 글 삭제 요청을 처리하는 메서드
	public void deleteBoard(int no) {
		boardMapper.deleteBoard(no);
	}	
	
	// 게시 글 수정 요청을 처리하는 메서드
	public void updateBoard(Board board) {
		boardMapper.updateBoard(board);
	}
	
	// 사용자가 입력한 게시 글 비번하고 no에 해당하는 비번 맞는지 체크하는 메서드
	public boolean isPassCheck(int no, String pass) {
		boolean result = false;
		String dbPass = boardMapper.isPassCheck(no);
		
		if(dbPass.equals(pass)) {
			result = true;
		}
		
		return result;
	}
	
	// 게시글 등록 요청을 처리하는 메서드
	public void addBoard(Board board) {
		boardMapper.insertBoard(board);
	}	
	
	// no에 해당하는 게시글 상세보기 요청을 처리하는 메서드
	public Board getBoard(int no, boolean isCount) {
		
		if(isCount) { // 게시 글 조회 일때만 조회수를 증가
			boardMapper.incrementReadCount(no);			
		}
		return boardMapper.getBoard(no);		
	}	
	
	// 한 페이지에 해당하는 게시 글 리스트 요청을 처리하는 메서드
	public Map<String, Object> boardList(int pageNum, String type, String keyword) {
		log.info("BoardService: boardList(int pageNum, String type, String keyword)");
		
		int currentPage = pageNum;
		
		// 1 = 0, 						2 = 10  						3 = 20
		// (1 - 1) * 10 = 0			(2 - 1) * 10 = 10		(3 - 1) * 10 = 20		
		int startRow = (currentPage - 1) * PAGE_SIZE;		
		// 1 * 10 - 10 = 0			2 * 10 - 10 = 10		3 * 10 - 10 = 20			
		//int startRow = currentPage * PAGE_SIZE - PAGE_SIZE;
		
		// 전체 페이지수 계산 = 전체 게시 글의 수  /  페이지 당 게시 글 수 -+  보정
		//  20 / 10 = 2, 50 / 10 = 5,  35 / 10 = 3.5
		
		int listCount = boardMapper.getBoardCount(type, keyword);
		
		int pageCount = listCount / PAGE_SIZE 
					+ (listCount % PAGE_SIZE == 0 ? 0 : 1);
		
		// [1] ~ [10]    					[11] ~ [20]    				[21] ~ [30]
		// 1 / 10 * 10 = 0 + 1		11 / 10 * 10 = 10 + 1	21 / 10 * 10 = 20 + 1	
		// 7 / 10 * 10 = 0 + 1		17 / 10 * 10 = 10 + 1	27 / 10 * 10 = 20 + 1
		// 10 / 10 * 10 = 10 + 1	20 / 10 * 10 = 20 + 1	30 / 10 * 10 = 30 + 1
		int startPage = (currentPage / PAGE_GROUP) * PAGE_GROUP + 1 
					- (currentPage % PAGE_GROUP == 0 ? PAGE_GROUP : 0);
		
		int endPage = startPage + PAGE_GROUP - 1;
		
		// pageCount = 25   [21] ~ [30]
		if(endPage > pageCount) {
			endPage = pageCount;
		}
		
		List<Board> bList = boardMapper.boardList(startRow, PAGE_SIZE, type, keyword);
				
		Map<String, Object> modelMap = new HashMap<>(); 
		modelMap.put("bList", bList);
		modelMap.put("pageCount", pageCount);
		modelMap.put("startPage", startPage);
		modelMap.put("endPage", endPage);
		modelMap.put("currentPage", currentPage);
		modelMap.put("pageGroup", PAGE_GROUP);
		modelMap.put("listCount", listCount);
		
		boolean searchOption = type.equals("null") || keyword.equals("null") ? false : true;
		modelMap.put("searchOption", searchOption);
		if(searchOption) {
			modelMap.put("type", type);
			modelMap.put("keyword", keyword);
		}
		
		log.info("startPage : " + startPage + ", endPage : " + endPage);
		return modelMap;
	}
}
