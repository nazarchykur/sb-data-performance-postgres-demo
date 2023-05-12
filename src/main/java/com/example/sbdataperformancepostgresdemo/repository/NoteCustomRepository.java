package com.example.sbdataperformancepostgresdemo.repository;

import java.util.List;

public interface NoteCustomRepository {
    <T> List<T> findAllBetweenIdsUsingGeneric(Long startId, Long endId, Class<T> type);
}
