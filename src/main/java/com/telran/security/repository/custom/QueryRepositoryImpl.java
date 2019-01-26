package com.telran.security.repository.custom;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class QueryRepositoryImpl implements QueryRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List <Object> findAllDevelopersByLanguage(String language) {
        String query = "SELECT d.name,programming_language_id,dr.role_id FROM developer d " +
                "INNER JOIN developer_language dl ON dl.developer_id = d.id " +
                "INNER JOIN developer_role dr on dr.developer_id = d.id " +
                "WHERE dl.programming_language_id=:language";

        Query jpaQuery = entityManager.createQuery(query);
        jpaQuery.setParameter("language", language);

        return jpaQuery.getResultList();
    }
}
