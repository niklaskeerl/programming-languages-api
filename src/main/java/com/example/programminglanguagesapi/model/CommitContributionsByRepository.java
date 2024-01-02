package com.example.programminglanguagesapi.model;

import lombok.Getter;

@Getter
public class CommitContributionsByRepository {
    public RepositoryElement repository;

    public CommitContributionsByRepository(RepositoryElement repository) {
        this.repository = repository;
    }
}
