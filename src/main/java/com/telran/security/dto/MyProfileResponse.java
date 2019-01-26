package com.telran.security.dto;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MyProfileResponse {

    private String name;
    private Set <String> programmingLanguages = new HashSet <>();
    private Set <String> roles = new HashSet <>();

    public MyProfileResponse(String name, String plLanguage, String role) {
        this.name = name;
        this.programmingLanguages.add(plLanguage);
        this.roles.add(role);
    }
}
