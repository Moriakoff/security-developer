package com.telran.security.repository;

import com.telran.security.entity.Developer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeveloperRepository extends JpaRepository<Developer, Integer> {

    Developer findByName(String name);
}
