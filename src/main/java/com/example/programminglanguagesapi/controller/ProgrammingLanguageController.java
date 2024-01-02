package com.example.programminglanguagesapi.controller;

import com.example.programminglanguagesapi.model.OrganizationMember;
import com.example.programminglanguagesapi.monitoring.CustomHealthIndicator;
import com.example.programminglanguagesapi.service.ProgrammingLanguageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Get Members")
public class ProgrammingLanguageController {

    @Autowired
    ProgrammingLanguageService programmingLanguageService;

    @Autowired
    CustomHealthIndicator customHealthIndicator;

    /**
     * @param language String (optional), the programming language to search for.
     * @return member list created from the GitHub GraphQL API
     */
    @GetMapping("/")
    @Operation(summary = "Get all members in the organization or filter by a programming language.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "503", description = "API is in health Status down"),
            @ApiResponse(responseCode = "200", description = "successfully got members")
    })
    public ResponseEntity<List<OrganizationMember>> getMembers(
            @RequestParam @Schema(type = "string", allowableValues = {"Java", "JavaScript", "TypeScript", "Dockerfile", "Kotlin", "Shell", "HTML"}) Optional<String> language
    ){
        if(customHealthIndicator.health().getStatus() == Status.DOWN){
            return new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE);
        }
        if(language.isPresent()){
            return new ResponseEntity<>(programmingLanguageService.filterByLanguage(language.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(programmingLanguageService.getAllMembers(), HttpStatus.OK);
    }
}
