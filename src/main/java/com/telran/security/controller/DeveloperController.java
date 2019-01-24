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
     *      - add roles to response
     *
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

        Set<String> allDeveloperLanguages = developerLanguageRepository.findAllByDeveloper(developer).stream()
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

//        List<String> programmingLanguages = developerLanguageRepository.findAllByDeveloper(developer)
//                .stream()
//                .map(developerLanguage ->
//                        developerLanguage
//                                .getProgrammingLanguage()
//                                .getLanguageName())
//                .collect(Collectors.toList());

        List<DeveloperLanguage> developerLanguages = developerLanguageRepository.findAllByDeveloper(developer);

        List<String> programmingLanguages = new ArrayList<>(); //Java, C#, Scala, JavaScript...

        if (developerLanguages.size() > 0) {

            for (DeveloperLanguage developerLanguage : developerLanguages) {
                ProgrammingLanguage programmingLanguage = developerLanguage.getProgrammingLanguage();

                programmingLanguages.add(programmingLanguage.getLanguageName());
            }
        }

        return MyProfileResponse.builder()
                .name(developer.getName())
                .programmingLanguages(programmingLanguages)
                .build();

    }

}
