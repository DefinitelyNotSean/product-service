package com.definitelynotsean.products.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicate;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;
import springfox.documentation.builders.PathSelectors;
import static com.google.common.base.Predicates.or;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {

	@Bean
	public Docket postsApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("public-api")
				.apiInfo(apiInfo()).select().paths(PathSelectors.any()).build();
	}
	
	private Predicate<String> postPaths() {
		return or(regex("/save/product"), regex("/greeting"), regex("/find/products"), regex("/find/product/{*}"));
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Products Service REST API")
				.description("Firebase hosted REST API that returns and updates product information")
				.termsOfServiceUrl("http://javainuse.com")
				.contact("seanoneill.inbox@gmail.com").license("Product Service License")
				.licenseUrl("seanoneill.inbox@gmail.com").version("1.0").build();
	}

}
