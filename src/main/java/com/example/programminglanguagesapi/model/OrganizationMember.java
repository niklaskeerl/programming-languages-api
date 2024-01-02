package com.example.programminglanguagesapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class OrganizationMember {
    @Schema(example = "example-user", description = "GitHub username")
    public String username;

    @Schema(example = "{\"TypeScript\":1,\"Java\":2,\"Dockerfile\":1,\"Shell\":1,\"Vue\":1}", description = "Map of programming languages and amount of projects using this language")
    public HashMap<String, Integer> programmingLanguages;

    public OrganizationMember(String username, HashMap<String, Integer> programmingLanguages) {
        this.username = username;
        this.programmingLanguages = programmingLanguages;
    }
}
