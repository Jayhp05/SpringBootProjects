package com.springbootstudy.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.springbootstudy.app.domain.Memo;

@Mapper
public interface MemoMapper {
	
	void addMemo(Memo memo);
	
	void updateMemo(Memo memo);
	
	void deleteMemo(int no);
	
//	맵퍼 인터페이스에서 메소드를 이용해 직접 쿼리를 맵핑
	@Select("SELECT * FROM memo")
	List<Memo> memoList();
	
//	맵퍼 XML에 쿼리를 작성한 다음에 그 쿼리를 맵퍼 인퍼에이스에서 호출
	List<Memo> findAll(); // public abstract을 선언하지 않아도 이미 추상클래스로 여겨짐
	
	Memo findById(int no);
	
}