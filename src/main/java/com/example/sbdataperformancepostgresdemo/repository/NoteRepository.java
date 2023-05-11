package com.example.sbdataperformancepostgresdemo.repository;

import com.example.sbdataperformancepostgresdemo.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
}

/*
it is often necessary to use the DISTINCT keyword when using a LEFT JOIN FETCH query in JPA to avoid duplicate results.

 */