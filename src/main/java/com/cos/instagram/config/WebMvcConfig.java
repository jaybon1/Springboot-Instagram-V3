package com.cos.instagram.config;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import com.cos.instagram.config.auth.CosAnnotation;
import com.cos.instagram.config.auth.LoginUserAnnotation;
import com.cos.instagram.config.auth.dto.LoginUser;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer{

	private final HttpSession httpSession;
	
	@Value("${file.path}")
	private String uploadFolder;
	
	// 이미지주소 (스태틱에 이미지업로드하면 엑박이 뜨기 때문에 실제주소로 설정)
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// TODO Auto-generated method stub
		WebMvcConfigurer.super.addResourceHandlers(registry);
		
		registry
		.addResourceHandler("/upload/**")
		.addResourceLocations("file:///"+uploadFolder)
		.setCachePeriod(3600) // 사진 캐싱시간 (초)
		.resourceChain(true)
		.addResolver(new PathResourceResolver());
	}
	
	// aop 어라운드 처럼 함수의 매개변수를 분석 할 수 있다(모르면 직접 만들면된다 proceedingJoinPoint 이용)
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		
		// 무슨메서드를 호출하든 항상 동장
		resolvers.add(new HandlerMethodArgumentResolver() {
			
			// 1번 - 메서드의 매개변수를 가져온다
			@Override
			public boolean supportsParameter(MethodParameter parameter) {
				
				// 매개변수가원하는 것인지 필터링
				boolean isCosAnnotation = parameter.getParameterAnnotation(CosAnnotation.class) != null;
//				boolean isUserClass = LoginUser.class.equals(parameter.getParameterType());
				return isCosAnnotation;
			}
			
			// 2번 - 1번이 true가 떨어지면 실행된다 
			// 호출한 함수의 매개변수에 리턴값을 넣어준다
			@Override
			public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
					NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
				
				return "cos";
			}
		});
		
		
		// 무슨메서드를 호출하든 항상 동장
		resolvers.add(new HandlerMethodArgumentResolver() {
			
			// 1번 - 메서드의 매개변수를 가져온다
			@Override
			public boolean supportsParameter(MethodParameter parameter) {
				
				// 매개변수가원하는 것인지 필터링
				boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUserAnnotation.class) != null;
				boolean isUserClass = LoginUser.class.equals(parameter.getParameterType());
				return isLoginUserAnnotation && isUserClass;
			}
			
			// 2번 - 1번이 true가 떨어지면 실행된다 
			// 호출한 함수의 매개변수에 리턴값을 넣어준다
			@Override
			public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
					NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
				
				return httpSession.getAttribute("loginUser");
			}
		});
		
		
//		WebMvcConfigurer.super.addArgumentResolvers(resolvers);
	}
}
