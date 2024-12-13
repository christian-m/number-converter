package dev.matzat.numberconverter.controller.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.swagger.v3.core.jackson.ModelResolver.enumsAsRef;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Value Conversion API",
        description = "This is a web service to convert a value into another format."
    )
)
@SuppressWarnings("DesignForExtension")
public class OpenApiConfiguration {

    static {
        enumsAsRef = true;
    }

    @Bean
    public GlobalOpenApiCustomizer openApiBuildPropertiesVersionCustomizer(@Autowired(required = false) final BuildProperties buildProperties) {
        return openApi -> {
            if (buildProperties != null) {
                openApi.getInfo().setVersion(buildProperties.getVersion());
            } else {
                // application is running locally
                openApi.getInfo().setVersion("dev-local");
            }
        };
    }
}
