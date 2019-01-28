package com.telran.security.repository.custom;

import com.telran.security.dto.MyProfileResponse;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class QueryRepositoryImpl implements QueryRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List <MyProfileResponse> findAllDevelopersByLanguage(String language) {

        String query = "SELECT new com.telran.security.dto.MyProfileResponse(d.name ,r.roleName ,pl.languageName)" +
                "FROM Developer d " +
                "JOIN DeveloperLanguage dl on dl.developer.id = d.id " +
                "JOIN DeveloperRole dr on dr.developer.id = d.id " +
                "JOIN ProgrammingLanguage pl on pl.id = dl.programmingLanguage.id " +
                "JOIN Role r on dr.role.id = r.id " +
                "WHERE pl.languageName=:language";

        Query jpqlQuery = entityManager.createQuery(query);
        jpqlQuery.setParameter("language", language);

        List <MyProfileResponse> result = jpqlQuery.getResultList();

        return new ArrayList <>(
                result.stream()
                .collect(Collectors.toMap(
                        MyProfileResponse::getName,
                        myProfileResponse -> myProfileResponse,
                        (oldValue, newValue) -> {
                            newValue.getRoles().addAll(oldValue.getRoles());
                            newValue.getProgrammingLanguages().addAll(oldValue.getProgrammingLanguages());
                            return newValue;
                        }))
                .values());
    }
}
