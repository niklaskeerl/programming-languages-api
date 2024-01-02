package com.example.programminglanguagesapi.task;

import com.example.programminglanguagesapi.model.*;
import com.example.programminglanguagesapi.monitoring.CustomHealthIndicator;
import com.example.programminglanguagesapi.service.ProgrammingLanguageService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class GitHubAPIMirrorTask {

    @Autowired
    CustomHealthIndicator customHealthIndicator;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    @Value("${github.access-token}")
    String githubAccessToken;

    @Value("${github.organization.login}")
    String githubOrganizationLoginName;

    @Value("${github.organization.id}")
    String githubOrganizationId;

    @Value("${github.organization.members.maximum}")
    String githubOrganizationMemberMaximum;

    @Value("${github.repositories.maximum}")
    String maxRepositories;

    @Value("${github.repositories.languages.maximum}")
    String maxLanguages;

    @Autowired
    ProgrammingLanguageService programmingLanguageService;


    /**
     * This method runs once at the startup of the application in a background task and retrieves data about members,
     * repositories and used languages from the GitHub GraphQL endpoint. After data retrieval, the JSON Object is parsed
     * and the member list is updated through a Service class. After this, the application becomes healthy.
     * @throws IOException when Request to GitHub GraphQL endpoint is unsuccessful.
     */
    @Scheduled(fixedDelay = Long.MAX_VALUE, timeUnit = TimeUnit.NANOSECONDS)
    public void updateDatabase() throws IOException {
        OkHttpClient client = new OkHttpClient();
        String query = "{\"query\":\"{organization(login:\\\"" + githubOrganizationLoginName + "\\\")" +
                "{membersWithRole(first:" + githubOrganizationMemberMaximum + ")" +
                "{nodes{login,contributionsCollection(organizationID:\\\"" + githubOrganizationId + "\\\")" +
                "{commitContributionsByRepository(maxRepositories:" + maxRepositories + "){repository{" +
                "languages(first:" + maxLanguages + ",orderBy:{field:SIZE,direction:DESC}){nodes{name}}}}}}}}}\"}";

        RequestBody body = RequestBody.create(query, JSON);

        Request request = new Request.Builder()
                .url("https://api.github.com/graphql")
                .post(body)
                .header("Authorization", "Bearer " + githubAccessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            if (response.body() != null) {
                JsonObject jsonObject = new Gson().fromJson(response.body().string(), JsonObject.class);
                String answer = jsonObject.getAsJsonObject("data")
                        .getAsJsonObject("organization").getAsJsonObject("membersWithRole")
                        .getAsJsonArray("nodes").toString();
                List<GraphQLDataElement> graphQLDataElements = new Gson().fromJson(answer, new TypeToken<List<GraphQLDataElement>>() {
                }.getType());
                graphQLDataElements.removeIf(element -> element.getContributionsCollection().getCommitContributionsByRepository().isEmpty());
                List<OrganizationMember> memberList = new ArrayList<>();
                for (GraphQLDataElement member : graphQLDataElements) {
                    HashMap<String, Integer> programmingLanguages = new HashMap<>();
                    List<CommitContributionsByRepository> commitContributionsByRepositories =
                            member.getContributionsCollection().getCommitContributionsByRepository();
                    for (CommitContributionsByRepository commitContributionsByRepository : commitContributionsByRepositories) {
                        commitContributionsByRepository.getRepository().getLanguages().getNodes().stream()
                                .map(Language::getName)
                                .forEach(languageName -> programmingLanguages.put(
                                        languageName, programmingLanguages.getOrDefault(languageName, 0) + 1)
                                );
                    }
                    OrganizationMember orgMember = new OrganizationMember(member.getLogin(), programmingLanguages);
                    memberList.add(orgMember);
                }
                programmingLanguageService.setMemberList(memberList);
                customHealthIndicator.setHealthUp();
            }
        }
    }
}
