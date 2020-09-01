package com.cos.instagram.config.hanlder.ex;

// 실행시에 발생할 수 있는 익셉션
public class MyUserIdNotFoundException extends RuntimeException {
	
	private String message;

	public MyUserIdNotFoundException(String message) {
		this.message = message;
	}

	public MyUserIdNotFoundException() {
		this.message = ""; // message에 값이 없기 때문에 부모의 것을 사용
	}
	
	@Override
	public String getMessage() {
//		return super.getMessage(); // 메세지가 없기 때문에 null 뜸
		return message;
	}
	
	
	
	

}
