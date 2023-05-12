package com.example.sbdataperformancepostgresdemo.controller;

import com.example.sbdataperformancepostgresdemo.dto.NoteDto;
import com.example.sbdataperformancepostgresdemo.repository.NoteRepository;
import com.example.sbdataperformancepostgresdemo.service.NoteService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;


    /*
        map in service entity to dto
        but remember that this query getAllNotes works not efficient because it creates 1 query to get
        all notes in ids range + 1 query for each note = well know problem N+1

        http://localhost:9001/notes/1/1000    390ms first time, 140 ms second time
        http://localhost:9001/notes/1/10000   1.50s
     */
    @GetMapping("/{startId}/{endId}")
    public List<NoteDto> getAllNotes(@PathVariable Long startId, @PathVariable Long endId) {
        return noteService.getAllNotes(startId, endId);
    }



    /*
        map in service entity to dto
        findAllByIdBetweenWithPerson should create only one query to get all notes in ids range with person

        http://localhost:9001/notes/1/1000    50ms first time, 25 ms second time
        http://localhost:9001/notes/1/10000   110ms
        http://localhost:9001/notes/1/100000   1.40s
     */
    @GetMapping("/using-query/{startId}/{endId}")
    public List<NoteDto> findAllByIdBetweenWithPerson(@PathVariable Long startId, @PathVariable Long endId) {
        return noteService.findAllByIdBetweenWithPerson(startId, endId);
    }

    /*
        get already ready dto from DB, and we do not need to map entity to dto in service layer

        http://localhost:9001/notes/1/1000     15ms
        http://localhost:9001/notes/1/10000    50ms
        http://localhost:9001/notes/1/100000   800ms
     */
    @GetMapping("/dto/using-query/{startId}/{endId}")
    public List<NoteDto> getAllNoteDtosUsingQuery(@PathVariable Long startId, @PathVariable Long endId) {
        return noteService.getAllNoteDtosUsingQuery(startId, endId);
    }

    /*
        get already ready dto from DB, and we do not need to map entity to dto in service layer
        but in this case we just pass needed Dto in service layer and use CustomRepository for it

        http://localhost:9001/notes/1/1000     15ms
        http://localhost:9001/notes/1/10000    30ms
        http://localhost:9001/notes/1/100000   800ms
     */
    @GetMapping("/dto/using-generic/{startId}/{endId}")
    public List<NoteDto> getAllNoteDtosUsingGeneric(@PathVariable Long startId, @PathVariable Long endId) {
        return noteService.getAllNoteDtosUsingGeneric(startId, endId);
    }

}
