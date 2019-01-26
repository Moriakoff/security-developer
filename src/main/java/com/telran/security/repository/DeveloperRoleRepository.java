package com.telran.security.repository;

import com.telran.security.entity.Developer;
import com.telran.security.entity.DeveloperRole;
import com.telran.security.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeveloperRoleRepository extends JpaRepository <DeveloperRole, Integer> {

    List <DeveloperRole> findAllByDeveloper(Developer developer);

    DeveloperRole findByDeveloperAndAndRole(Developer developer, Role role);
}
