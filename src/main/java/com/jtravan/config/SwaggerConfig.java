package com.jtravan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableWebMvc
@EnableOpenApi
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(getApiInfo())
                .tags(new Tag("FHIR v2 (or 1.0.2)", "Rest API for validating version 2.0 (or 1.0.2) of the FHIR schemas"))
                .tags(new Tag("FHIR v2.1 (or 1.4.0)", "Rest API for validating version 2.1 (or 1.4.0) of the FHIR schemas"))
                .tags(new Tag("FHIR v3 (or 3.0.2)", "Rest API for validating version 3.0.2 of the FHIR schemas"))
                .tags(new Tag("FHIR r4 (or 4.0.1)", "Rest API for validating release 4.0.1 of the FHIR schemas"))
                .tags(new Tag("FHIR r5 (or 4.4.0)", "Rest API for validating release 4.4.0 of the FHIR schemas"))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.jtravan.api"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
                .docExpansion(DocExpansion.LIST) // or DocExpansion.NONE or DocExpansion.FULL
                .build();
    }

    private ApiInfo getApiInfo() {
        Contact contact = new Contact("John Ravan", "https://theravans.com/", "john@jtravan.com");
        return new ApiInfoBuilder()
                .title("HAPI FHIR Validator API")
                .description("This API is used to validate all FHIR (http://www.hl7.org/fhir/) schemas")
                .version("1.6.0")
                .license("MIT License")
                .licenseUrl("https://www.mit.edu/")
                .contact(contact)
                .build();
    }

}
