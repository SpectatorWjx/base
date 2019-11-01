package com.wang.base.config;

import com.wang.base.config.apiVersion.CustomRequestMappingHandlerMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.nio.charset.Charset;
import java.util.List;

/***
 * @ClassName:
 * @Description:
 * @Auther: wjx zhijiu
 * @Date: 2019/10/24 10:29
 */
@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    /**
     * swagger访问Handlers
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * apiVersion RequestHandlerMapping
     * @return
     */
    @Override
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        return new CustomRequestMappingHandlerMapping();
    }

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        return converter;
    }
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        converters.add(responseBodyConverter());
    }
    @Override
    public void configureContentNegotiation(
            ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }

}
