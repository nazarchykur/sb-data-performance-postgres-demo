package com.example.sbdataperformancepostgresdemo.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
public class NoteCustomRepositoryImpl implements NoteCustomRepository {

    private final EntityManager entityManager;

    public <T> List<T> findAllBetweenIdsUsingGeneric(Long startId, Long endId, Class<T> type) {
        String jpqlQuery = "SELECT new " + type.getName() + "(n.title, n.person.email) FROM Note n WHERE n.id BETWEEN :startId AND :endId";
        TypedQuery<T> query = entityManager.createQuery(jpqlQuery, type);
        query.setParameter("startId", startId);
        query.setParameter("endId", endId);

        return query.getResultList();
    }
}
