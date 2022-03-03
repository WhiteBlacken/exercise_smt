package com.cisl.smt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 配置静态资源访问路径
 * @Author qxy
 * @Date 2022/3/3 14:46
 * @Version 1.0
 */
@Configuration
public class MyConfiguration extends WebMvcConfigurerAdapter {
    @Value("${upload.fileDir}")
    private String fileDirMatcher;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        //上传的文件路径
        registry.addResourceHandler("/file/**").addResourceLocations("file:"+fileDirMatcher);

    }
}
