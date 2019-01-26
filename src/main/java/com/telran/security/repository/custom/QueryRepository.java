package com.telran.security.repository.custom;

import com.telran.security.dto.MyProfileResponse;

import java.util.List;

public interface QueryRepository {

    List <MyProfileResponse> findAllDevelopersByLanguage(String language);
}
