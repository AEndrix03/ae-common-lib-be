package it.aredegalli.commons.security;

import it.aredegalli.commons.security.jwt.JwtRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtRequestInterceptor jwtRequestInterceptor;

    @Autowired
    public WebConfig(JwtRequestInterceptor jwtRequestInterceptor) {
        this.jwtRequestInterceptor = jwtRequestInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtRequestInterceptor).addPathPatterns("/**");
    }
}
