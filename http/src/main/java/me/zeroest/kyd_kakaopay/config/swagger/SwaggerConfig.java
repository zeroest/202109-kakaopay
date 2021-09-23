package me.zeroest.kyd_kakaopay.config.swagger;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
@RequiredArgsConstructor
public class SwaggerConfig {

    private final TypeResolver typeResolver;

    private String version;
    private String title;

    @Bean
    public Docket apiAll() {
        version = "All";
        title = "Kakao Pay Task";

        return new Docket(DocumentationType.SWAGGER_2)
                .alternateTypeRules(
                        AlternateTypeRules.newRule(
                                typeResolver.resolve(Pageable.class),
                                typeResolver.resolve(SwaggerPage.class)
                        )
                )
                .useDefaultResponseMessages(false)
                .groupName(version)
                .select()
                .apis(
                        Predicates.or(
                                RequestHandlerSelectors.basePackage("me.zeroest.kyd_kakaopay.controller")
                        )
                )
                .paths(PathSelectors.ant("/**"))
                .build()
                .apiInfo(apiInfo(title, version))
                .securityContexts(Arrays.asList(securityContext()));

    }

    private springfox.documentation.spi.service.contexts.SecurityContext securityContext() {
        return springfox.documentation.spi.service.contexts.SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.any()).build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(new SecurityReference("JWT", authorizationScopes));
    }

    private ApiInfo apiInfo(String title, String version) {
        return new ApiInfo(
                title,
                "Kako pay Task",
                version,
                "zeroest.me",
                new Contact("Kim YoungDong", "zeroest.me", "husheart@naver.com"),
                "Licenses",

                "zeroest.me",

                new ArrayList<>());
    }

}
