package com.hang.myselfcommunity.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    private SessionInterceptor sessionInterceptor;

    @Autowired
    public void setSessionInterceptor(SessionInterceptor sessionInterceptor) {
        this.sessionInterceptor = sessionInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor).addPathPatterns("/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**", "/static/**", "/js/**", "/css/**")
                .addResourceLocations("/public", "classpath:/static/", "classpath:/static/js/", "classpath:/static/css/")
                .setCacheControl(CacheControl.maxAge(Duration.ofDays(365)));
        /* 这些资源的使用期限为一年，以确保最大程度地利用浏览器缓存并减少浏览器发出的HTTP请求。 */
        /* Last-Modified从中推导出该信息，Resource#lastModified 以便HTTP条件请求与"Last-Modified"标头一起支持 */
    }
}
