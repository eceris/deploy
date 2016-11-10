package com.daou.deploy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.daou.deploy.interceptor.PackageMoveInterceptor;

@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {

    @Autowired
    PackageMoveInterceptor projectInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // /project/*/packages 가 호출될 때에 만들어진 패키지 파일들을 엔티티로 만들기 위한 인터셉터 등록
        registry.addInterceptor(projectInterceptor).addPathPatterns("/project/*/packages");
    }
}
