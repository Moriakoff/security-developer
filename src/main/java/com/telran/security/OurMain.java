package com.telran.security;

import com.telran.security.entity.Developer;
import com.telran.security.entity.DeveloperRole;
import com.telran.security.entity.Role;
import com.telran.security.repository.DeveloperRepository;
import com.telran.security.repository.DeveloperRoleRepository;
import com.telran.security.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Component
public class OurMain implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DeveloperRepository developerRepository;

    @Autowired
    private DeveloperRoleRepository developerRoleRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception { //... - var args
        initializeAll();

    }


    private void initializeAll() {
        Role developerRole = Role.builder()
                .roleName("DEVELOPER")
                .build();

        Role adminRole = Role.builder()
                .roleName("ADMIN_DEVELOPER")
                .build();

        roleRepository.saveAll(Arrays.asList(developerRole, adminRole));

        Developer developer = Developer.builder()
                .name("John Smith")
                .build();

        developerRepository.save(developer);


        DeveloperRole developerRoleBinding = DeveloperRole.builder()
                .role(developerRole)
                .developer(developer)
                .build();

        DeveloperRole adminRoleBinding = DeveloperRole.builder()
                .role(adminRole)
                .developer(developer)
                .build();



//        developerRoleRepository.save(developerRoleBinding);
//        developerRoleRepository.save(adminRoleBinding);

        developerRoleRepository.saveAll(Arrays.asList(
                developerRoleBinding,
                adminRoleBinding
        ));
    }


}
