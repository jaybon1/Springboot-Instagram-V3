package com.cos.instagram.config.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // 메서드의 파라메터로서만 사용, 여러개 넣을 경우에는 중괄호 이용
@Retention(RetentionPolicy.RUNTIME) // 런타임시 작동
public @interface LoginUserAnnotation {

}
