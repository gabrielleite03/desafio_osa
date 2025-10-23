package br.com.kenjix.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("REST API's RESTfull")
                        .version("v1")
                        .description("REST API's RESTfull")
                        .termsOfService("https://pub.erudio.com.br/meus-cursos")
                );
    }
}
