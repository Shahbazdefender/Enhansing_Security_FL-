package com.zeroinfinity.federatedserver.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.OperationsSorter;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@ConditionalOnProperty(name = "swagger.enabled", havingValue = "true", matchIfMissing = false)
@EnableSwagger2
@EnableEncryptableProperties
public class SwaggerConfig {

	@Value("${api.title}")
	private String title;

	@Value("${api.version}")
	private String version;

	@Value("${api.description}")
	private String description;

	@Value("${api.contact.name}")
	private String name;

	@Value("${api.contact.link}")
	private String link;

	@Value("${api.contact.email}")
	private String email;

	// Docket provides sensible defaults and convenience methods for swagger
	// configuration.
	@Bean
	public Docket swaggerConfiguration() {
		// return a prepared docket instance
		return new Docket(DocumentationType.SWAGGER_2).select()
				// .paths(PathSelectors.ant("/api/*"))
				.apis(RequestHandlerSelectors.basePackage("com.zeroinfinity.federatedserver")).build().apiInfo(apiDetails())
				.securitySchemes(Arrays.asList(apiKey()));
	}

	// provides detail about Api
	private ApiInfo apiDetails() {
		return new ApiInfoBuilder().title(title).description(description).version(version)
				.contact(new Contact(name, link, email)).build();
	}

	// takes key for authorization purpose
	private ApiKey apiKey() {
		return new ApiKey("jwtToken", "Authorization", "header");
	}

	@Bean
	public UiConfiguration uiConfig() {
		return UiConfigurationBuilder.builder().operationsSorter(OperationsSorter.METHOD).build();
	}

}
