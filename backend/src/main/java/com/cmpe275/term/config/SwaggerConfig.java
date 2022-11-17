package com.cmpe275.term.config;

import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableSwagger2
public class SwaggerConfig {
	
	@Bean
	public Docket swaggerConfig() {
		return  new Docket(DocumentationType.SWAGGER_2)
				.select()
				.paths(PathSelectors.ant("/user/*"))
				.apis(RequestHandlerSelectors.basePackage("com.cmpe.275.term"))
				.build();
	}

}
