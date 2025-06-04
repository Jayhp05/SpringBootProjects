package com.springbootstudy.app.lombok;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class LombokTest02 {

	public static void main(String[] args) {
		Member02 m1 = new Member02();
		Member02 m2 = new Member02("이순신");
		Member02 m3 = new Member02("어머나", "여성", 27);
		
		System.out.println(m1);
		System.out.println(m2);
		System.out.println(m3);
	}
}
