package com.cos.instagram.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.instagram.config.auth.CosAnnotation;
import com.cos.instagram.config.auth.LoginUserAnnotation;
import com.cos.instagram.config.auth.dto.LoginUser;

@Controller
public class ImageController {

	@GetMapping({"","/","image/feed"})
	public String feed(@LoginUserAnnotation LoginUser loginUser) {
		
		System.out.println(loginUser.getName());
		
		return "image/feed"; // 메인페이지가 되는 곳
	}
	
	// 어노테이션 연습
	@GetMapping({"costest"})
	public @ResponseBody String costest(@CosAnnotation String cos) {

		return cos; // 메인페이지가 되는 곳
	}
}
