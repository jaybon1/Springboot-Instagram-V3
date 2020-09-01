package com.cos.instagram.web;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.instagram.config.auth.LoginUserAnnotation;
import com.cos.instagram.config.auth.dto.LoginUser;
import com.cos.instagram.service.FollowService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class FollowController {
	
	private final FollowService followService;
	
	@PostMapping("follow")
	public @ResponseBody String follow(@LoginUserAnnotation LoginUser loginUser, @RequestBody Map<String, Integer> data) {
		
		return followService.follow(loginUser, data);

	}
	
	@DeleteMapping("follow")
	public @ResponseBody String unFollow(@LoginUserAnnotation LoginUser loginUser, @RequestBody Map<String, Integer> data) {

		return followService.unFollow(loginUser, data);
		
	}
	

}
