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
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.*;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    //jwt 토큰 인증을 위한 버튼까지 포함

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private List<SwaggerResourcesProvider> resourcesProviders;

    @Bean
    public Docket api() {
        Server serverLocal = new Server("local", "http://localhost:9000", "for local usages", Collections.emptyList(), Collections.emptyList());
        Server UbuntuServer = new Server("server", "https://dev.runwayserver.shop", "for prod server", Collections.emptyList(), Collections.emptyList());
        Server ProdServer = new Server("server", "https://prod.runwayserver.shop", "for dev server", Collections.emptyList(), Collections.emptyList());
        return new Docket(DocumentationType.OAS_30)
                .servers(serverLocal,UbuntuServer,ProdServer)
                .consumes(getConsumeContentTypes())
                .produces(getProduceContentTypes())
                .securityContexts(Arrays.asList(securityContext())) // 추가
                .securitySchemes(Arrays.asList(apiKey())) // 추가
                .ignoredParameterTypes(User.class)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build().apiInfo(apiInfo());
    }

    private Set<String> getConsumeContentTypes() {
        Set<String> consumes = new HashSet<>();
        consumes.add("application/json;charset=UTF-8");
        consumes.add("application/x-www-form-urlencoded");
        return consumes;
    }

    private Set<String> getProduceContentTypes() {
        Set<String> produces;
        produces = new HashSet<>();
        produces.add("application/json;charset=UTF-8");
        produces.add("application/x-www-form-urlencoded");
        return produces;
    }



    private ApiInfo apiInfo() {
        String description = "Runway";
        return new ApiInfoBuilder()
                .title("Runway Rest API 명세서")
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
        return Arrays.asList(new SecurityReference("X-AUTH-TOKEN", authorizationScopes));
    }


    private ApiKey apiKey() {
        //return new ApiKey("Authorization", "Authorization", "header");
        return new ApiKey("X-AUTH-TOKEN", "Bearer", "header");
    }
    /*
    @Bean
    public SwaggerConfiguration swaggerConfiguration() {
        return new SwaggerConfiguration() {
            public SwaggerResourcesProvider swaggerResourcesProvider() {
                return new CompositeSwaggerResourcesProvider(resourcesProviders);
            }
        };
    }

     */




}