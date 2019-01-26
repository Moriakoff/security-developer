package com.telran.security.repository;

import com.telran.security.entity.ProgrammingLanguage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgrammingLanguageRepository extends JpaRepository <ProgrammingLanguage, Integer> {

    boolean existsByLanguageName(String languageName);

    ProgrammingLanguage findByLanguageName(String name);
}
