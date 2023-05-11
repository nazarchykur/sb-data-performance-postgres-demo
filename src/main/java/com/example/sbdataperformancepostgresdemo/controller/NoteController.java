package com.example.sbdataperformancepostgresdemo.controller;

import com.example.sbdataperformancepostgresdemo.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NoteController {
    private final NoteRepository noteRepository;
}
