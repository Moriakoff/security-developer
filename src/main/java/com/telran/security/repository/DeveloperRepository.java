package com.telran.security.repository;

import com.telran.security.entity.Developer;
import com.telran.security.repository.custom.QueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeveloperRepository extends JpaRepository <Developer, Integer>, QueryRepository {

    Developer findByName(String name);
}
