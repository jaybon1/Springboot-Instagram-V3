package com.cos.instagram.domain.follow;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FollowRepository extends JpaRepository<Follow, Integer>{
	
	@Query(value = "SELECT count(*) FROM follow WHERE toUserId = ?1", nativeQuery = true)
	int mCountByFollower(int userId);
	
	@Query(value = "SELECT count(*) FROM follow WHERE fromUserId = ?1", nativeQuery = true)
	int mCountByFollowing(int userId);
	
	@Query(value = "select count(*) from follow where fromUserId = ?1 and toUserId = ?2", nativeQuery = true)
	int mfollowState(int loginUserId, int pageUserId);
	
	// 수정, 삭제 , 추가 시는 트랜잭션 어노테이션필요 javax @Transactional
	// 수정 시에는 모디파이 어노테이션 필요 @Modifying
	@Modifying
	int deleteByFromUserIdAndToUserId(int fromUserId, int toUserId);
}
