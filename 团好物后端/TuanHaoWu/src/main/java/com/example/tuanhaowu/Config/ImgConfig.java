package com.example.tuanhaowu.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ImgConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // addResourceHandler: （ 存储图片的虚拟路径，在 static 目录下的 picture 文件夹，用于存储上传图片）
        // addResourceLocations: （ file: + 存储图片的路径）
        registry.addResourceHandler("/tourist/picture/**").addResourceLocations("file:" +  "/home/resource/");
    }
}

