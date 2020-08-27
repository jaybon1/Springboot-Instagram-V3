package com.cos.instagram.web.dto;

import org.springframework.web.multipart.MultipartFile;

import com.cos.instagram.domain.image.Image;
import com.cos.instagram.domain.user.User;

import lombok.Data;

@Data
public class ImageReqDto {
	
	// 멀티파트 파일 저장
	private MultipartFile file;
	private String caption;
	private String location;
	private String tags;
	
	public Image toEntity(String imageUrl, User user) {
		return Image.builder()
				.location(location)
				.caption(caption)
				.imageUrl(imageUrl)
				.user(user)
				.build();
	}
}


