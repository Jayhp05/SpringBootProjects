package com.springbootstudy.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springbootstudy.app.domain.Memo;
import com.springbootstudy.app.mapper.MemoMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MemoService {

	@Autowired
	private MemoMapper memoMapper;
	
	public void addMemo(Memo memo) {
		log.info("service: addMemo(memo)");
		memoMapper.addMemo(memo);
	}
	
	public void updateMemo(Memo memo) {
		log.info("service: updateMemo(memo)");
		memoMapper.updateMemo(memo);
	}
	
	public void deleteMemo(int no) {
		log.info("service: deleteMemo(no)");
		memoMapper.deleteMemo(no);
	}
	
	public List<Memo> memoList() {
		log.info("service: memoList()");
		
//		return memoMapper.memoList();
		return memoMapper.findAll();
	}
	
	public Memo getMemo(int no) {
		log.info("service: getMemo(int no)");
		
		return memoMapper.findById(no);
	}
}
