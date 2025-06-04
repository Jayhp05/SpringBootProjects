package com.springbootstudy.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springbootstudy.app.entity.MemoEntity;

/* 우리의 인터페이스를 저장소(Repository) 역할을 하는 레포지토리로 만들기 위해서는 
 * JpaRepository 인터페이스를 상속 받아야 한다. JpaRepository 인터페이스를
 * 상속할 때 제네릭은 <엔티티 타입, 엔티티의 기본 키(PK)의 타입>을 지정하면 된다.
 **/
@Repository
public interface MemoRepository extends JpaRepository<MemoEntity, Integer>{
	// 상위 인터페이스에 정의되어 있는 메서드가 자동으로 실행되어 쿼리를 생성함
	// List<MemoEntity> findAll();
	// Optinal<MemoEntiry> findById(no)
	// MemoEntity save(memoEntity) 
	// void deleteById(no)
}
