package com.example.Phone.Pay.management.config;


import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.info.License;


/**
 * @Author ➤➤➤ Rajeswari
 * @Date ➤➤➤ 18/01/24
 * @Time ➤➤➤ 10:55 am
 * @Project ➤➤➤ Phone-Pay-management
 */

@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public OpenAPI phonePayApi() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList( "Authorization"))
                .components(new Components()
                        .addSecuritySchemes( "Authorization",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info().title("PhonePayManagement API")
                .description("PhonePayManagementApplication")
                .version("v.1")
                .license(new License().name("Apache").url(""));
    }
}
