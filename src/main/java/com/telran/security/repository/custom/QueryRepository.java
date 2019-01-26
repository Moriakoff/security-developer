package com.telran.security.repository.custom;

import java.util.List;

public interface QueryRepository {

    List <Object> findAllDevelopersByLanguage(String language);
}
