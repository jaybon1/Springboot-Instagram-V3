package com.cos.instagram.web.dto;

import java.util.List;

import com.cos.instagram.domain.image.Image;
import com.cos.instagram.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDto {
	
	private boolean pageHost;
	private boolean followState; // true 팔로우취소 , false 팔로우
	
	private User user;
	private List<UserProfileImageRespDto> images;
	
	private int imageCount;
	private int followerCount;
	private int followingCount;
	
	public boolean getPageHost() {
		return pageHost;
	}
	
	public boolean getFollowState() {
		return followState;
	}


}
