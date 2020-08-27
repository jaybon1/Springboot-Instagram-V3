package com.cos.instagram.config;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.cos.instagram.config.auth.CosAnnotation;
import com.cos.instagram.config.auth.LoginUserAnnotation;
import com.cos.instagram.config.auth.dto.LoginUser;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer{

	private final HttpSession httpSession;
	
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
