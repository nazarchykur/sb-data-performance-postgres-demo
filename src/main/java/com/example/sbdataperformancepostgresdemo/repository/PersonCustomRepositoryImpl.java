package com.example.sbdataperformancepostgresdemo.repository;

import com.example.sbdataperformancepostgresdemo.entity.Person;
import jakarta.persistence.EntityManager;
import org.hibernate.jpa.QueryHints;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PersonCustomRepositoryImpl implements PersonCustomRepository {

    private final EntityManager entityManager;

    /*
        перше ми створюємо ту саму query з 'left join fetch p.notes', як ми би і робили з використанням @Query над
        методом у репозиторію і де передаємо рендж від 'startId' до 'endId'

        друге ми створюємо подібну query з 'left join fetch p.tasks'
        але тут тепер можна передати 'p IN ?1' - бо ми вже взяли були ренджи від 'startId' до 'endId' у першій query,
        тому тут передаємо як параметр 'persons'

        ми отримаємо 2 запити з left join, у яких вже буде вигружено все, що потрбіно

        звернуть увагу, що ми тут для Hibernate вказуємо distinct, щоб він поскладав всі потрбні ентіті по зв'язкам до
        нашої ентіті, але у БД не потрбіно робити distinct, execute plan такого запиту покаже, що БД спершу має відсорутвати
        по порядку, а потім робити всі інші дії


        Hibernate 6 auto-deduplication
        Hibernate 6 can deduplicate parent entity references automatically, so you don’t need to use the DISTINCT keyword,
        as it were the case with Hibernate 5:
            because we need use something like: .setHint("hibernate.query.passDistinctThrough", false)
                                                .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
     */
    @Transactional(readOnly = true)
    @Override
    public List<Person> findAllInRangeFetchNotesAndTasks(Long startId, Long endId) {
        List<Person> persons = entityManager.createQuery("SELECT p FROM Person p left join fetch p.notes WHERE p.id >= ?1 AND p.id < ?2", Person.class)
                .setParameter(1, startId)
                .setParameter(2, endId)
                .getResultList();

        persons = entityManager.createQuery("SELECT p FROM Person p left join fetch p.tasks WHERE p IN ?1", Person.class)
                .setParameter(1, persons)
                .getResultList();

        return persons;
    }
}
