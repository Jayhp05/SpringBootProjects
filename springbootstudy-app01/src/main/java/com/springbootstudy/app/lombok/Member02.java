package com.springbootstudy.app.lombok;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Member02 {
	
	@NonNull
	private String name;
	private String gender;
	private int age;
}
