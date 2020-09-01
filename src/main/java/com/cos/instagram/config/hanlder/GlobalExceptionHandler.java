package com.cos.instagram.config.hanlder;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.cos.instagram.config.hanlder.ex.MyUserIdNotFoundException;
import com.cos.instagram.config.hanlder.ex.MyUsernameNotFoundException;
import com.cos.instagram.util.Script;

@RestController
@ControllerAdvice // 모든 익셉션을 낚아챈다
public class GlobalExceptionHandler {
	
	@ExceptionHandler(value = MyUserIdNotFoundException.class)
	public String myUserIdNotFoundException(Exception e) {
		
		return Script.back("해당 id의 유저가 없습니다.");
		
	}
	
	@ExceptionHandler(value=MyUsernameNotFoundException.class)
	public String myUsernameNotFoundException(Exception e) {
		return Script.alert("아이디 혹은 패스워드가 잘못되었습니다.");
	}
}
