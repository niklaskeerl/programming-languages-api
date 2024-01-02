package com.example.programminglanguagesapi.model;

import lombok.Getter;

@Getter
public class GraphQLDataElement {
    public String login;

    public ContributionsCollectionElement contributionsCollection;

    public GraphQLDataElement(String login, ContributionsCollectionElement contributionsCollection) {
        this.login = login;
        this.contributionsCollection = contributionsCollection;
    }
}
