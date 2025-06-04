package com.springbootstudy.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springbootstudy.app.dto.MemoDTO;
import com.springbootstudy.app.entity.MemoEntity;
import com.springbootstudy.app.repository.MemoRepository;

import lombok.extern.slf4j.Slf4j;

// MemoEntityService 클래스가 서비스 계층의 스프링 빈(Bean) 임을 정의 
@Service
@Slf4j
public class MemoEntityService {
	
	@Autowired
	private MemoRepository memoRepository;

	public List<MemoDTO> memoList() {
		log.info("entityService: memoList()");
		// DB 테이블의 메모 리스트를 no를 기준으로 내림차순 정렬해 가져와 
		// StreamAPI를 사용해 List<MemoEntity>를 List<MemoDTO>로 변환해 반환
		return memoRepository.findAll(Sort.by(Sort.Direction.DESC, "no"))
					.stream()
					.map(MemoDTO::new)
					.toList();
	}
		  
	public MemoDTO getMemo(int no) {	
		log.info("entityService: getMemo(int no)");
		return new MemoDTO(memoRepository.findById(no).get());
	}
		  
	public MemoDTO addMemo(MemoDTO memo) {
		log.info("entityService: addMemo(Memo memo)");
		MemoEntity entity = memoRepository.save(MemoDTO.toEntity(memo));
		return new MemoDTO(entity);
	}
	
	/* 아래에서 save() 메서드를 사용하지 않고 수정된 값을 객체에 적용하면 UPDATE 쿼리가
	 * DB에 발행되는데 이는 Dirty Checking(상태 변경 검사)으로 실행되는 것으로 JPA에서
	 * 트랜잭션이 끝나는 시점에 변화가 있는 모든 엔티티 객체를 DB에 자동으로 반영해 준다. 
	 **/
	// 트랜잭션 처리 애노테이션
	@Transactional
	public MemoDTO updateMemo(MemoDTO memo) {
		log.info("entityService: updateMemo(Memo memo)");
		
		// Dirty Checking에서 상태 변화는 현재 DB에서 읽어온 최초 조회 상태에서
		MemoEntity entity = memoRepository.findById(memo.getNo()).get();
		
		// 이 코드가 실행되면 메모가 수정되었기 때문에 현재 메서드가 종료되면
		// 즉 트랜잭션이 끝나면 UPDATE 쿼리가 DB에 발행된다.
		entity.updateMemo(memo);
		
		// 다음과 같이 save() 함수를 사용해 DB 테이블에서 직접 수정할 수도 있지만
		// 이런 코드는 객체지향 관점에서는 좋은 코드는 아니다.
		//entity = memoRepository.save(MemoDTO.toEntity(memo));
		return new MemoDTO(entity);
	}
	
	public void deleteMemo(int no) {
		log.info("entityService: deleteMemo(int no)");
		memoRepository.deleteById(no);
	}
}
