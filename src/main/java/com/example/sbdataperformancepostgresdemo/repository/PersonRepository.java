package com.example.sbdataperformancepostgresdemo.repository;

import com.example.sbdataperformancepostgresdemo.entity.Person;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long>, PersonCustomRepository {

    List<Person> findAllByIdBetween(Long startId, Long endId);

    /*
        Hibernate 6 auto-deduplication
        Hibernate 6 can deduplicate parent entity references automatically, so you don’t need to use the DISTINCT keyword,
        as it were the case with Hibernate 5:
            because we need use something like: setHint("hibernate.query.passDistinctThrough", false)
     */

//    @Query("SELECT distinct p FROM Person p left join fetch p.notes WHERE p.id BETWEEN ?1 AND ?2")
    @Query("SELECT p FROM Person p left join fetch p.notes WHERE p.id BETWEEN ?1 AND ?2")
    List<Person> findAllByIdBetweenFetchNotes(Long startId, Long endId);

    /*
        Hibernate не дозволяє робити ще один fetch, бо впаде з помилкою
            org.hibernate.loader.MultipleBagFetchException: cannot simultaneously fetch multiple bags:

     */
//    @Query("SELECT distinct p FROM Person p left join fetch p.notes  left join fetch p.tasks WHERE p.id BETWEEN ?1 AND ?2")
//    List<Person> findAllByIdBetweenFetchNotesAndTasks(Long startId, Long endId);

    /*
    The `findAllByIdBetween(startId, endId)` method will return all `Person` entities whose `id` is between `startId`
    and `endId`, inclusive.

    The `findAllByIdGreaterThanEqualAndIdLessThan(startId, endId)` method will also return all `Person` entities whose
    `id` is between `startId` and `endId`, inclusive.
     */
}