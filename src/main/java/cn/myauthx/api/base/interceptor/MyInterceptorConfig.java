package cn.myauthx.api.base.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置类
 * @author DaenMax
 */
@Configuration
public class MyInterceptorConfig implements WebMvcConfigurer {
    @Bean
    public HandlerInterceptor getMyInterceptor(){
        return new MyInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        //注册拦截器
        InterceptorRegistration registration = registry.addInterceptor(getMyInterceptor());

        //添加拦截请求
        registration.addPathPatterns("/soft/**");
        registration.addPathPatterns("/web/**");

        //添加不拦截的请求
        registration.excludePathPatterns("/test/**");
    }

}
