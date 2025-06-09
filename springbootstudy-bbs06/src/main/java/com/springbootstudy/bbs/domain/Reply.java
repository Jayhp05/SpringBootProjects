package com.springbootstudy.bbs.domain;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/* 하나의 댓글 정보를 저장하는 댓글 DTO(Data Transfer Object) 클래스
 * 댓글 정보를 저장하고 있는 reply 테이블의 컬럼과 1:1 맵핑되는 클래스
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reply {
	private int no;
	private int bbsNo;
	private String replyContent;
	private String replyWriter;
	private Timestamp regDate;		
}
