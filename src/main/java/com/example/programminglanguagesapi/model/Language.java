package com.example.programminglanguagesapi.model;

import lombok.Getter;

@Getter
public class Language {
    public String name;

    public Language(String name) {
        this.name = name;
    }
}
