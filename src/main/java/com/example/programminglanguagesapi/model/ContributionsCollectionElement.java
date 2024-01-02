package com.example.programminglanguagesapi.model;

import lombok.Getter;

import java.util.List;

@Getter
public class ContributionsCollectionElement {

    public List<CommitContributionsByRepository> commitContributionsByRepository;

    public ContributionsCollectionElement(List<CommitContributionsByRepository> commitContributionsByRepository) {
        this.commitContributionsByRepository = commitContributionsByRepository;
    }
}
