package com.example.runway.config;


import com.example.runway.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    //jwt 토큰 인증을 위한 버튼까지 포함

    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Bean
    public Docket api() {
        Server serverLocal = new Server("local", "http://localhost:8081", "for local usages", Collections.emptyList(), Collections.emptyList());
        Server UbuntuServer = new Server("test", "https://www.lifolio.shop", "for testing", Collections.emptyList(), Collections.emptyList());
        return new Docket(DocumentationType.OAS_30)
                .securityContexts(Arrays.asList(securityContext())) // 추가
                .securitySchemes(Arrays.asList(apiKey())) // 추가
                .ignoredParameterTypes(User.class)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build().apiInfo(apiInfo());
    }



    private ApiInfo apiInfo() {
        String description = "LIFOLIO";
        return new ApiInfoBuilder()
                .title("LIFOLIO API Documentation")
                .description(description)
                .version("1.0")
                .build();
    }



    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }


    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
    }


    private ApiKey apiKey() {
        //return new ApiKey("Authorization", "Authorization", "header");
        return new ApiKey("Authorization", "Bearer", "header");
    }


}