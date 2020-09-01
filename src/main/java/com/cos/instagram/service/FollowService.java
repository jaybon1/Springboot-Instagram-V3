package com.cos.instagram.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.instagram.config.auth.dto.LoginUser;
import com.cos.instagram.domain.follow.Follow;
import com.cos.instagram.domain.follow.FollowRepository;
import com.cos.instagram.domain.user.User;
import com.cos.instagram.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FollowService {

	private final UserRepository userRepository;
	private final FollowRepository followRepository;
	
	@Transactional // springframework
	public String follow(LoginUser loginUser,Map<String, Integer> data) {
		
		int id = data.get("id");
		
		User userEntity = userRepository.findById(id).get();
		
		if(userEntity == null) {
			return "-1";
		}
		
		Follow follow = Follow.builder()
				.fromUser(loginUser.getUser())
				.toUser(userEntity)
				.build();
		
		Follow followEntity = followRepository.save(follow);
		
		if(followEntity == null) {
			return "-1";
		}
			
		return "1";
		
	}
	
	@Transactional
	public String unFollow(LoginUser loginUser, Map<String, Integer> data) {
		
		int id = data.get("id");
		
		followRepository.deleteByFromUserIdAndToUserId(loginUser.getId(), id);
		
		return "1";
		
	}
	
}
