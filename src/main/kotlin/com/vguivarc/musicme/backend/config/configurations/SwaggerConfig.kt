package com.vguivarc.musicme.backend.config.configurations

import com.fasterxml.classmate.TypeResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.Ordered
import org.springframework.data.domain.Pageable
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import springfox.documentation.builders.AlternateTypeBuilder
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.oas.annotations.EnableOpenApi
import springfox.documentation.schema.AlternateTypeRule
import springfox.documentation.schema.AlternateTypeRuleConvention
import springfox.documentation.schema.AlternateTypeRules.newRule
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import java.lang.reflect.Type

@EnableOpenApi
@Configuration
@Profile(value = [ "local", "dev"])
class SwaggerConfig : WebMvcConfigurer {

    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.vguivarc.musicme.backend.web.api"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(apiInfo())
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/")

        registry.addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/")
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
            .title("Swag REST API")
            .version("1.0.0")
            .build()
    }

    @Bean
    fun pageableConvention(resolver: TypeResolver): AlternateTypeRuleConvention {
        return object : AlternateTypeRuleConvention {
            override fun getOrder(): Int {
                return Ordered.HIGHEST_PRECEDENCE
            }

            override fun rules(): List<AlternateTypeRule> {
                return listOf(
                    newRule(resolver.resolve(Pageable::class.java), resolver.resolve(pageableMixin()))
                )
            }
        }
    }

    private fun pageableMixin(): Type {
        return AlternateTypeBuilder()
            .fullyQualifiedClassName(
                String.format(
                    "%s.generated.%s",
                    Pageable::class.java.getPackage().name,
                    Pageable::class.java.simpleName
                )
            )
            .property { p ->
                p.name("page")
                p.canRead(true)
                p.canWrite(true)
                p.type(Int::class.java)
            }
            .property { p ->
                p.name("size")
                p.canRead(true)
                p.canWrite(true)
                p.type(Int::class.java)
            }
            .property { p ->
                p.name("sort")
                p.canRead(true)
                p.canWrite(true)
                p.type(String::class.java)
            }
            .build()
    }
}
