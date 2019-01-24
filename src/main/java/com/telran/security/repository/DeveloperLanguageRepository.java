package com.telran.security.repository;

import com.telran.security.entity.Developer;
import com.telran.security.entity.DeveloperLanguage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeveloperLanguageRepository extends JpaRepository<DeveloperLanguage, Integer> {

    List<DeveloperLanguage> findAllByDeveloper(Developer developer);
}
