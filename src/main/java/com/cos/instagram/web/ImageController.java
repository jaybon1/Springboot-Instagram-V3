package com.cos.instagram.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.instagram.config.auth.CosAnnotation;
import com.cos.instagram.config.auth.LoginUserAnnotation;
import com.cos.instagram.config.auth.dto.LoginUser;
import com.cos.instagram.service.ImageService;
import com.cos.instagram.web.dto.ImageReqDto;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ImageController {
	
	private final ImageService imageService;

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
	
	@GetMapping("image/uploadForm")
	public String imageUploadform() {
		return "image/image-upload";
	}
	
	// 사진 업로드 받는 메소드
	@PostMapping("image")
	public String imageUpload(@LoginUserAnnotation LoginUser loginUser, ImageReqDto imageReqDto) {
		
		imageService.사진업로드(imageReqDto, loginUser.getId());
		
		return "redirect:/";
	}
	
}
