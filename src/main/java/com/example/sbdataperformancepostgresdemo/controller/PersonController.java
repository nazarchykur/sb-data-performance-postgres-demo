package com.example.sbdataperformancepostgresdemo.controller;

import com.example.sbdataperformancepostgresdemo.entity.Note;
import com.example.sbdataperformancepostgresdemo.entity.Person;
import com.example.sbdataperformancepostgresdemo.entity.Task;
import com.example.sbdataperformancepostgresdemo.repository.PersonRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Random;

@RestController
@RequiredArgsConstructor
public class PersonController {

    private final PersonRepository personRepository;

//    @Transactional
//    @PostConstruct
//    public void generateData() {
//
//        /*
//        Generate 5,000,000 records of `Person` and add a random number of notes to each person (between 1 and 5) (60% chance),
//        and add a random number of tasks between 1 and 4 to each person.
//         */
//        Random random = new Random();
//
//        for (int i = 1; i <= 5_000_000; i++) {
//            Person person = new Person();
//            person.setFirstName("First Name " + i);
//            person.setLastName("Last Name " + i);
//            person.setAge(random.nextInt(100));
//            person.setEmail("Email " + i + "@gmail.com");
//            person.setCreatedAt(LocalDateTime.now());
//
//            int numNotes = random.nextInt(5) + 1; // generate a random number of notes between 1 and 5
//            for (int j = 1; j <= numNotes; j++) {
//                if (random.nextDouble() < 0.6) { // add note with 60% chance
//                    Note note = new Note();
//                    note.setTitle("Note " + j);
//                    note.setDescription("Description for Note " + j);
//                    note.setCreatedAt(LocalDateTime.now());
//                    person.addNote(note);
//                }
//            }
//            int numTasks = random.nextInt(4) + 1; // generate a random number of tasks between 1 and 4
//            for (int j = 1; j <= numTasks; j++) {
//                Task task = new Task();
//                task.setTitle("Task " + j);
//                task.setDescription("Description for Task " + j);
//                task.setCreatedAt(LocalDateTime.now());
//                person.addTask(task);
//            }
//
//            personRepository.save(person);
//        }
//    }
}
