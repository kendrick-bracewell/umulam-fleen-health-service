package com.umulam.fleen.health.configuration.openapi;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
  info = @Info(
            title = "Fleen Health - Your Online Therapy",
            description = "Fleen Health is a Telehealth application that enables you to improve your mental health",
            contact = @Contact(
              email = "info@fleenhealth.com",
              url = "fleenhealth.com",
              name = "Fleen Health"
            ),
            license = @License(
              name = "MIT License",
              url = "https://github.com/volunux/umulam-fleen-health-service"
            )
        ),
        servers = @Server(url = "/")
)
public class OpenApiConfig {

}
