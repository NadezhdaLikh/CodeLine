package ru.webency.compiler.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.stereotype.Service;

@OpenAPIDefinition(
        info = @Info(
                description = "OpenAPI documentation for programming compiler",
                version = "1.0.0",
                title = "Programming Compiler",
                contact = @Contact(
                        name = "Webency Project",
                        url = "https://github.com/webency-projects"
        )),
        servers = {
                @Server(
                        description = "Local server",
                        url = "http://localhost:5000"
                )
        }
)
public class OpenApiConfig {
}
