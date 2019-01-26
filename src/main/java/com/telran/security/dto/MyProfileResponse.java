package com.telran.security.dto;

import com.telran.security.entity.Role;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MyProfileResponse {

    private String name;
    private List<String> programmingLanguages;
    private List <Role> roles;
}
