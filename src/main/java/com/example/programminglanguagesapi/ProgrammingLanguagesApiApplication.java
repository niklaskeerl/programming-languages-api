package com.example.programminglanguagesapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@OpenAPIDefinition(info = @Info(version = "1.0.0", title = "Programming Languages API", description = "Get knowledge about the programming language of members of a GitHub organization."))
public class ProgrammingLanguagesApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProgrammingLanguagesApiApplication.class, args);
	}

}
