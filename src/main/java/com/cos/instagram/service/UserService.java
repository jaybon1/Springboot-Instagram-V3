package com.cos.instagram.service;

import java.util.List;
import java.util.function.Supplier;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.instagram.config.auth.dto.LoginUser;
import com.cos.instagram.config.hanlder.ex.MyUserIdNotFoundException;
import com.cos.instagram.domain.follow.FollowRepository;
import com.cos.instagram.domain.image.Image;
import com.cos.instagram.domain.image.ImageRepository;
import com.cos.instagram.domain.user.User;
import com.cos.instagram.domain.user.UserRepository;
import com.cos.instagram.web.dto.JoinReqDto;
import com.cos.instagram.web.dto.ProfileDto;
import com.cos.instagram.web.dto.RespDto;
import com.cos.instagram.web.dto.UserProfileImageRespDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	@PersistenceContext
	private EntityManager em; // jpa를 마이바티스 처럼 사용
	
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final ImageRepository imageRepository;
	private final FollowRepository followRepository;

	@Transactional
	public void 회원가입(JoinReqDto joinReqDto) {
		String encPassword = bCryptPasswordEncoder.encode(joinReqDto.getPassword());
		joinReqDto.setPassword(encPassword);
		userRepository.save(joinReqDto.toEntity());
	}

	// readOnly 읽기전용 트랜잭션
	// 1. 변경 감지 연산을 하지 않음 - 더티체킹 등을 안함
	// 2. isolation 고립성 - 팬톰 현상이 일어나지 않음
	@Transactional(readOnly = true)
	public ProfileDto 회원프로필(LoginUser loginUser ,int id) {
		
		int imageCount;
		int followerCount;
		int followingCount;
		boolean followState;

		User userEntity = userRepository.findById(id).orElseThrow(new Supplier<MyUserIdNotFoundException>() {

			@Override
			public MyUserIdNotFoundException get() { // 여기는 무조건 런타임익셉션을 넣어야한다
				// 익셉션던지기
				return new MyUserIdNotFoundException("유저서비스에서 회원조회를 못했습니다.");
			}
		});
		
		// 이 방식을 잘 안쓰는 이유
		// 이 방식을 쓰려면 마이바티스가 좋다 - jpa와 마이바티스를 같이 이용
		// 고급쿼리를 쓰려면 jpa + QueryDSL 사용
		StringBuilder sb = new StringBuilder();
		sb.append("select im.id, im.imageUrl, "); // mysql은 별칭 안적어도 이름이 나온다
		sb.append("(select count(*) from likes lk where lk.imageId = im.id) as likeCount, "); // 이것은 별칭 붙여야됨
		sb.append("(select count(*) from comment ct where ct.imageId = im.id) as commentCount ");
		sb.append("from image im where im.userId = ? ");
		
		String q = sb.toString();
		
		// import javax.persistence.Query;
		Query query = em.createNativeQuery(q, "UserProfileImageRespDtoMapping").setParameter(1, id); // 쿼리의 첫번째 파라미터에 id를 넣어준다
		
		// db에 있는 값이 아니지만 영속화가 된다
		// 영속화가 되면 퍼시스턴스 컨텍스트에 등록되서 캐싱을 이용하여 IO를 줄일 수 있다
		// 영속 - 영속성 컨텍스트에 저장된 상태
		// 준영속 - 영속성 컨텍스트에 저장되었다가 분리된 상태
		// 삭제 - 삭제된 상태
		List<UserProfileImageRespDto> imagesEntity = query.getResultList(); 
		
//		em.persist(imagesEntity); // 영속화를 시키는 코드
//		em.detach(imagesEntity); // 영속화를 푸는 코드
		
//		// 1. 이미지 카운트
//		List<Image> imagesEntity = imageRepository.findByUserId(id);
		imageCount = imagesEntity.size();

		// 2. 팔로우 팔로워 수(수정해야 됨)
		followerCount = followRepository.mCountByFollower(userEntity.getId());
		followingCount = followRepository.mCountByFollowing(userEntity.getId());
		
		System.out.println(loginUser.getId());
		System.out.println(id);
		
		followState = followRepository.mfollowState(loginUser.getId(), id) == 1 ? true : false; // 삼항연산자
		
		// 머스태치를 이용하면 최종값을 던져야하기 때문에 api를 잘 짤 수 있다
		ProfileDto profileDto = ProfileDto.builder() // 엔티티가 아니라서 무한참조 현상이 일어나지 않는다
				.pageHost(id == loginUser.getId())
				.followState(followState)
				.user(userEntity)
				.images(imagesEntity) // dto만듬 댓글 좋아요
				.imageCount(imageCount)
				.followerCount(followerCount)
				.followingCount(followingCount) 
				.build();
		
		return profileDto;
	}
}
