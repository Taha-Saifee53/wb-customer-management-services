package com.wb.assignment;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @Info(
                title = "Customer Management Service",
                version = "1.0.0",
                description = "REST APIs for managing bank customers"
        ),
        tags = {
                @Tag(
                        name = "Customer APIs",
                        description = "Operations related to customer"
                )
        }
)
@SpringBootApplication
public class CustomerManagementServicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerManagementServicesApplication.class, args);
    }
}
