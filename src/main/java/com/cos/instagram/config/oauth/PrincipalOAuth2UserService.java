package com.cos.instagram.config.oauth;

import java.util.function.Supplier;

import javax.print.attribute.standard.MediaSize.Other;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cos.instagram.config.auth.PrincipalDetails;
import com.cos.instagram.config.auth.dto.LoginUser;
import com.cos.instagram.domain.user.User;
import com.cos.instagram.domain.user.UserRepository;
import com.cos.instagram.domain.user.UserRole;


// oauth용 서비스
@Service
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Value("${cos.secret}")
	private String cosSecret;

	
	private static final Logger log = LoggerFactory.getLogger(PrincipalOAuth2UserService.class);

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		
		// OAuth서버에 내 서버정보와 Access Token을 던지고 회원 프로필 정보를 OAuth2User타입으로 받아온다
		log.info(userRequest.getAccessToken().getTokenValue());
		log.info(userRequest.getClientRegistration().toString()); // 어떤 프로바이더로 로그인 했는지 알 수 있다
		
		// super.loadUser(userRequest) - 페이스북으로 받아서 정보를 넣어준다 
		OAuth2User oAuth2User = super.loadUser(userRequest);
		
		System.out.println("회원정보 : "+oAuth2User.getAttributes());
		
		User userEntity = oAuthLoginOrJoin(oAuth2User);
		
//		return super.loadUser(userRequest);
		return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
	}
	
	private User oAuthLoginOrJoin(OAuth2User oAuth2User) {
		
		// 유저네임을 프로바이더와 프로바이더 아이디를 이용해서 완성
		String provider = "facebook";
		String providerId = oAuth2User.getAttribute("id");
		String username = provider+"_"+providerId;
		String password = bCryptPasswordEncoder.encode(cosSecret);
		String email = oAuth2User.getAttribute("email");
		
		// 옵셔널을 사용해서 이용하는 것이 좋다 orElseGet 등
		User userEntity = userRepository.findByUsername(username).orElseGet(new Supplier<User>() {

			// 화살표함수로 써도됨
			@Override
			public User get() {
				
				// 회원가입
				User user = User.builder()
						.username(username)
						.password(password) // yml파일의 cos secret 데이터를 가져온다
						.email(email)
						.role(UserRole.USER)
						.provider(provider)
						.providerId(providerId)
						.build();
				
				return userRepository.save(user);
				
			}
		});
		session.setAttribute("loginUser", new LoginUser(userEntity));
		
		return userEntity;
	}
}











