package com.springbootstudy.bbs.domain;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

	private String id, name, pass, email, mobile;
	private String zipcode, address1, address2, phone;
	private boolean emailGet;
	private Timestamp regDate;
}
