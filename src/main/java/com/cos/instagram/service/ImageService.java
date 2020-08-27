package com.cos.instagram.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.instagram.domain.image.Image;
import com.cos.instagram.domain.image.ImageRepository;
import com.cos.instagram.domain.tag.Tag;
import com.cos.instagram.domain.tag.TagRepository;
import com.cos.instagram.domain.user.User;
import com.cos.instagram.domain.user.UserRepository;
import com.cos.instagram.util.Utils;
import com.cos.instagram.web.dto.ImageReqDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImageService {
	
	@Value("${file.path}")// yml의 주소 // org.springframework.beans.factory.annotation.Value;
	private String uploadFolder;
	
	private final ImageRepository imageRepository;
	private final TagRepository tagRepository;
	private final UserRepository UserRepository;
	
	@Transactional // springframework.transaction
	public void 사진업로드(ImageReqDto imageReqDto, int userId) {
		
		User userEntitiy = UserRepository.findById(userId)
				.orElseThrow(null);
		
		UUID uuid = UUID.randomUUID();
		// 파일명 중복방지
		String imageFilename = uuid + "_"+imageReqDto.getFile().getOriginalFilename(); // 이름중복방지
		// 주소
		Path imageFilePath = Paths.get(uploadFolder+imageFilename); // import java.nio.file.Path;
		
		try {
			// 파일저장
			Files.write(imageFilePath, imageReqDto.getFile().getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// 1 이미지 저장 // 파일명만 넣어야 서버가 바뀌어도 문제가 없다
		
		Image image = imageReqDto.toEntity(imageFilename, userEntitiy);
		
		Image imageEntity = imageRepository.save(image);
		
		
		// 2 태그 저장
		
		List<String> tagNames = Utils.tagParse(imageReqDto.getTags());
		
		for (String name : tagNames) {
			Tag tag = Tag.builder()
					.image(imageEntity)
					.name(name)
					.build();
			tagRepository.save(tag);
		}
		
	}
}
