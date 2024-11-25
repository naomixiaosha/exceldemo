package com.reminis.exceldemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: Crazer
 * @Date: 2024/11/6 16:10
 * @version: 1.0.0
 * @Description:
 */
@Configuration
public class MyConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //这里是指在url上面打的内容
        registry.addResourceHandler("/**")
                //下面的是指可以对应resources文件下那些内容
                .addResourceLocations("classpath:/")
                .addResourceLocations("classpath:/templates/")
                .addResourceLocations("classpath:/static/");
    }
}
