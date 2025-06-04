package com.springbootstudy.app.lombok;

public class LombokTest01 {
	
	public static void main(String[] args) {
		Member01 m1 = new Member01();
		m1.setName("홍길동");
		m1.setGender("남성");
		m1.setAge(33);
		
		System.out.println(m1);
		System.out.println(m1.getGender());
	}

}
