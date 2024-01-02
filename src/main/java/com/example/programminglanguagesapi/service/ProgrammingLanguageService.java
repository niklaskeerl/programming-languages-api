package com.example.programminglanguagesapi.service;

import com.example.programminglanguagesapi.model.OrganizationMember;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgrammingLanguageService {
    @Setter
    private List<OrganizationMember> memberList;

    public List<OrganizationMember> getAllMembers(){
        return memberList;
    }

    public List<OrganizationMember> filterByLanguage(String language){
        return memberList.stream().filter(member -> member.getProgrammingLanguages().containsKey(language))
                .sorted(Comparator.comparingInt(member -> ((OrganizationMember) member).getProgrammingLanguages().get(language)).reversed())
                .collect(Collectors.toList());
    }

}
