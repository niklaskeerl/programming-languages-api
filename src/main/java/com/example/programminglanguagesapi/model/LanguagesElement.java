package com.example.programminglanguagesapi.model;

import lombok.Getter;

import java.util.List;

@Getter
public class LanguagesElement {
    public List<Language> nodes;

    public LanguagesElement(List<Language> nodes) {
        this.nodes = nodes;
    }
}
