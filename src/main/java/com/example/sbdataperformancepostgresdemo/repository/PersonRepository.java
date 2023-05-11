package com.example.sbdataperformancepostgresdemo.repository;

import com.example.sbdataperformancepostgresdemo.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findAllByIdBetween(Long startId, Long endId);

    @Query("SELECT distinct p FROM Person p left join fetch p.notes WHERE p.id BETWEEN ?1 AND ?2")
    List<Person> findAllByIdBetweenFetchNotes(Long startId, Long endId);

    /*
    The `findAllByIdBetween(startId, endId)` method will return all `Person` entities whose `id` is between `startId`
    and `endId`, inclusive.

    The `findAllByIdGreaterThanEqualAndIdLessThan(startId, endId)` method will also return all `Person` entities whose
    `id` is between `startId` and `endId`, inclusive.
     */
}