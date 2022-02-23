package com.webproject.chatservice.config;

import com.webproject.chatservice.utils.CORSFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    @Bean
    public FilterRegistrationBean getFilterRegistrationBean(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new CORSFilter());
        registrationBean.addUrlPatterns("/**");
        return registrationBean;
    }

    // CORS 추가
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://hyunee.shop.s3-website.ap-northeast-2.amazonaws.com")
                .allowedHeaders("*")
                .allowedMethods("*")
                .exposedHeaders("*")
                .maxAge(3600);
    }

}
