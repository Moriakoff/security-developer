package com.telran.security.controller;

import com.telran.security.dto.MyProfileResponse;
import com.telran.security.entity.*;
import com.telran.security.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class DeveloperController {
    /**
     * 1. Create a new developer
     * 2. Add programming language to the developer
     * 3. Search all developers by programming language name
     * 4. ADMIN_ACCESS: add new programming languages
     * 5. Get MY profile
     * - add roles to response
     * <p>
     * 6. Get all users
     * 7. Promote DEVELOPERs to ADMIN_DEVELOPERS
     * 8. Demote ADMIN_DEVELOPERs to DEVELOPERs
     */

    @Autowired
    private DeveloperRepository developerRepository;

    @Autowired
    private DeveloperLanguageRepository developerLanguageRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DeveloperRoleRepository developerRoleRepository;

    @Autowired
    private ProgrammingLanguageRepository programmingLanguageRepository;


    @PostMapping("/new")
    @Transactional
    public String createNewDeveloper(@RequestParam("name") String name) {
        Developer developer = Developer.builder()
                .name(name)
                .build();

        developerRepository.save(developer);

        Role role = roleRepository.getDefaultRole();
        DeveloperRole developerRole = DeveloperRole.builder()
                .developer(developer)
                .role(role)
                .build();

        developerRoleRepository.save(developerRole);

        return developer.getName();
    }

    @Transactional
    @PostMapping("/create-language")
    public ProgrammingLanguage createProgrammingLanguage(@RequestParam("name") String plName) {

        ProgrammingLanguage programmingLanguage = programmingLanguageRepository.findByLanguageName(plName);

        if (programmingLanguage == null) {
            programmingLanguage = ProgrammingLanguage.builder()
                    .languageName(plName)
                    .build();

            programmingLanguageRepository.save(programmingLanguage);
        }

        return programmingLanguage;
    }

    @Transactional
    @PutMapping("/add-language")
    public MyProfileResponse addProgrammingLanguage(Principal principal, @RequestParam("name") String languageName) {

        ProgrammingLanguage programmingLanguage = programmingLanguageRepository.findByLanguageName(languageName);

        if (programmingLanguage == null) {
            throw new RuntimeException("Language does not exist");
        }

        Developer developer = developerRepository.findByName(principal.getName());

        Set <String> allDeveloperLanguages = developerLanguageRepository.findAllByDeveloper(developer).stream()
                .map(x -> x.getProgrammingLanguage().getLanguageName())
                .collect(Collectors.toSet());

        boolean isNewLanguageForDeveloper = allDeveloperLanguages.add(languageName);

        if (isNewLanguageForDeveloper) {

            DeveloperLanguage developerLanguage = DeveloperLanguage.builder()
                    .developer(developer)
                    .programmingLanguage(programmingLanguage)
                    .build();

            developerLanguageRepository.save(developerLanguage);
        }

        return getMyProfile(principal);

    }

    @Transactional
    @GetMapping("/my-profile")
    public MyProfileResponse getMyProfile(Principal principal) {
        Developer developer = developerRepository.findByName(principal.getName());

        return getMyProfileResponse(developer);

    }

    private MyProfileResponse getMyProfileResponse(Developer developer) {
        Set <String> programmingLanguages = developerLanguageRepository.findAllByDeveloper(developer)
                .stream()
                .map(developerLanguage ->
                        developerLanguage
                                .getProgrammingLanguage()
                                .getLanguageName())
                .collect(Collectors.toSet());

        Set <String> roles = developerRoleRepository.findAllByDeveloper(developer)
                .stream()
                .map(developerRole -> developerRole.getRole().getRoleName())
                .collect(Collectors.toSet());

        return MyProfileResponse.builder()
                .name(developer.getName())
                .programmingLanguages(programmingLanguages)
                .roles(roles)
                .build();
    }

    @GetMapping("get-by-language/{name}")
    public List <MyProfileResponse> getAllDevelopersByLanguage(@RequestHeader("Authorization") String token,
                                                               @PathVariable("name") String language) {

        return developerRepository.findAllDevelopersByLanguage(language);
    }

    @PutMapping("/promote")
    @Transactional
    public MyProfileResponse promoteAdmin(@RequestParam("role") String role,
                                          @RequestParam("name") String name) {

        Developer developer = developerRepository.findByName(name);

        List <String> developerRole = developerRoleRepository.findAllByDeveloper(developer)
                .stream()
                .map(devRole -> devRole.getRole().getRoleName())
                .collect(Collectors.toList());

        if (!developerRole.contains("ADMIN_DEVELOPER")) {

            DeveloperRole devRole = DeveloperRole.builder()
                    .developer(developer)
                    .role(roleRepository.findRoleByRoleName(role))
                    .build();

            developerRoleRepository.save(devRole);
        }

        return getMyProfileResponse(developer);
    }

    @PutMapping("/demote")
    @Transactional
    public MyProfileResponse demoteAdmin(@RequestParam("name") String name) {

        Developer developer = developerRepository.findByName(name);

        Role role = roleRepository.findRoleByRoleName("ADMIN_DEVELOPER");

        List <String> developerRole = developerRoleRepository.findAllByDeveloper(developer)
                .stream()
                .map(devRole -> devRole.getRole().getRoleName())
                .collect(Collectors.toList());

        if (developerRole.contains("ADMIN_DEVELOPER")) {

            DeveloperRole devRole = developerRoleRepository.findByDeveloperAndAndRole(developer, role);

            developerRoleRepository.delete(devRole);
        }

        return getMyProfileResponse(developer);

    }

    @GetMapping("/users")
    @Transactional(readOnly = true)
    public List <MyProfileResponse> getAllUsers() {

        List <Developer> developersList = developerRepository.findAll();

        List <MyProfileResponse> myProfileResponses = new ArrayList <>();

        for (Developer developer : developersList) {
            myProfileResponses.add(getMyProfileResponse(developer));
        }

        return myProfileResponses;
    }

}
