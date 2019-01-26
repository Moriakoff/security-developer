package com.telran.security.repository.custom;

import com.telran.security.dto.MyProfileResponse;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class QueryRepositoryImpl implements QueryRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List <MyProfileResponse> findAllDevelopersByLanguage(String language) {

        String query = "  SELECT d.name ,role_name ,language_name FROM developer d " +
                "INNER JOIN developer_language dl ON dl.developer_id = d.id " +
                "INNER JOIN developer_role dr on dr.developer_id = d.id " +
                "INNER JOIN programming_language pl on dl.programming_language_id = pl.id " +
                "INNER JOIN role r on dr.role_id = r.id " +
                "WHERE pl.language_name=:language";

        Map <String, MyProfileResponse> responses = new HashMap <>();

        Query nativeQuery = entityManager.createNativeQuery(query, Tuple.class);
        nativeQuery.setParameter("language", language);

        List <Tuple> result = nativeQuery.getResultList();

        return tupleToMyProfileResponse(result);
    }

    private List <MyProfileResponse> tupleToMyProfileResponse(List <Tuple> result) {
        Map <String, MyProfileResponse> responses = new HashMap <>();

        String name = "";
        String role = "";
        String prLanguage = "";

        for (Tuple tuple : result) {

            name = tuple.get("name").toString();
            prLanguage = tuple.get("language_name").toString();
            role = tuple.get("role_name").toString();

            if (responses.containsKey(name)) {
                responses.get(name).getRoles().add(role);
                responses.get(name).getProgrammingLanguages().add(prLanguage);
            }

            responses.putIfAbsent(name, new MyProfileResponse(name, prLanguage, role));
        }

        return new ArrayList <>(responses.values());
    }
}
