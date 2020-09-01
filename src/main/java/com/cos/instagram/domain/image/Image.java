package com.cos.instagram.domain.image;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;

import org.hibernate.annotations.CreationTimestamp;

import com.cos.instagram.domain.tag.Tag;
import com.cos.instagram.domain.user.User;
import com.cos.instagram.web.dto.UserProfileImageRespDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 실무에서 많이 쓴다. 하지만 마이바티스랑 다른 점이 없어진다. 통계 등 쿼리를 짤 때 사용한다.
@SqlResultSetMapping(name = "UserProfileImageRespDtoMapping", // 이름 정의
//		entities = 오브젝트를 매핑
		classes = @ConstructorResult(targetClass = UserProfileImageRespDto.class, 
		columns = {
				@ColumnResult(name = "id", type = Integer.class), // 오브젝트 매핑은 안된다 원시자료형만 가능
				@ColumnResult(name = "imageUrl", type = String.class),
				@ColumnResult(name = "likeCount", type = Integer.class),
				@ColumnResult(name = "commentCount", type = Integer.class) 
				}
		)
)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String location;
	private String caption;
	private String imageUrl;

	// Image를 select하면 한명의 User가 딸려옴.
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userId") // 칼럼명
	private User user; // 타입은 User오브젝트의 PK의 타입

	// Image를 select하면 여러개의 Tag가 딸려옴.
	@OneToMany(mappedBy = "image", fetch = FetchType.EAGER) // 연관관계 주인의 변수명을 적는다.
	@JsonIgnoreProperties({ "image" }) // Jackson한테 내리는 명령
	private List<Tag> tags;

	@CreationTimestamp
	private Timestamp createDate;

}
