package com.example.programminglanguagesapi.model;

import lombok.Getter;

@Getter
public class RepositoryElement {
    public LanguagesElement languages;

    public RepositoryElement(LanguagesElement languages) {
        this.languages = languages;
    }
}
