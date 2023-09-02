package com.example.runway.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.stereotype.Component;
import springfox.documentation.oas.web.OpenApiTransformationContext;
import springfox.documentation.oas.web.WebMvcOpenApiTransformationFilter;
import springfox.documentation.spi.DocumentationType;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Component
public class Workaround implements WebMvcOpenApiTransformationFilter {

    @Override
    public OpenAPI transform(OpenApiTransformationContext<HttpServletRequest> context) {
        OpenAPI openApi = context.getSpecification();

        Server localServer = new Server();
        localServer.setDescription("local");
        localServer.setUrl("http://localhost:9000");

        Server testServer = new Server();
        testServer.setDescription("test");
        testServer.setUrl("http://runway-dev-env.eba-h3xrns2m.ap-northeast-2.elasticbeanstalk.com");

        Server prodServer = new Server();
        prodServer.setDescription("prod");
        prodServer.setUrl("https://prod.runwayserver.shop");

        Server devRealServer = new Server();
        devRealServer.setDescription("devRealServer");
        devRealServer.setUrl("https://dev.runway-api.com");

        Server prodRealServer = new Server();
        prodRealServer.setDescription("prodRealServer");
        prodRealServer.setUrl("https://prod.runway-api.com");

        openApi.setServers(Arrays.asList(localServer, testServer,prodServer,devRealServer,prodRealServer));


        return openApi;
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return documentationType.equals(DocumentationType.OAS_30);
    }
}