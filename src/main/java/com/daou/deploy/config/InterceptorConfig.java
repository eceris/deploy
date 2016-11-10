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
        registry.addInterceptor(projectInterceptor).addPathPatterns("/project/*");
    }
}
