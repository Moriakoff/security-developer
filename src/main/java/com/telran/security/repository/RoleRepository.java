package com.telran.security.repository;

import com.telran.security.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query("from Role r where r.roleName = 'DEVELOPER'")
    Role getDefaultRole();
}
