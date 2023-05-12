package com.example.sbdataperformancepostgresdemo.repository;

import com.example.sbdataperformancepostgresdemo.entity.Person;

import java.util.List;

public interface PersonCustomRepository {
    List<Person> findAllInRangeFetchNotesAndTasks(Long startId, Long endId);
}
