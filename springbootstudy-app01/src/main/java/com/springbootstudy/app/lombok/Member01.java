package com.springbootstudy.app.lombok;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = "gender", callSuper = true)
public class Member01 {
	
	private String name;
	private String gender;
	private int age;

}
