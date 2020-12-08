package com.example.demo.common.swagger;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author fg
 * @date 2020/11/20
 */
@Configuration
@EnableWebMvc
@EnableSwagger2
public class Swagger2 implements WebMvcConfigurer {

    @Bean
    public Docket createRestApi() {
        boolean enabled = true;
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(enabled)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .globalOperationParameters(parameterList())
                ;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("demo接口文档")
                .description("demo接口文档")
                .contact(new Contact("gjt", "", ""))
                .version("1.0")
                .build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }


    private List<Parameter> parameterList() {
        return Collections.singletonList(
                new ParameterBuilder()
                        .parameterType("header")
                        .name("token")
                        .defaultValue("access_token")
                        .description("token")
                        .required(false)
                        .modelRef(new ModelRef("String"))
                        .build()
        );
    }

}
