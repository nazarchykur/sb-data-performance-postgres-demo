package com.example.sbdataperformancepostgresdemo.controller;

import com.example.sbdataperformancepostgresdemo.entity.Note;
import com.example.sbdataperformancepostgresdemo.entity.Person;
import com.example.sbdataperformancepostgresdemo.entity.Task;
import com.example.sbdataperformancepostgresdemo.repository.PersonRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonRepository personRepository;

    /*
    отже у нас є поки що дві таблиці Persons + Notes

       1 експеримент без індексу Notes person_id
            запит http://localhost:9001/persons/1/100 може займати аж до 15 секунд

       2 експеримент з індексом Notes person_id
            запит http://localhost:9001/persons/1/100 може займати аж до 20 мілісекунд
            запит http://localhost:9001/persons/1/1000 може займати аж до 450 мілісекунд
            запит http://localhost:9001/persons/1/50000 може займати аж до 1.8 секунд

            тому що тут вже неефективно збоку Hibernate, бо для кожного Note буде додатковий запит до БД
            тобто тут полетить сотні або тисячі запитів до БД

        3 експеримент
            можна виключити
                spring.jpa.open-in-view=false   // по замовчуванню = true

            який виключе цю можливість підгружати ліново зв'язки між таблицями на рівні контролера, бо по замовчуванню
            Spring відкриває цю сесію від початку запиту у контролер, що впринципі є антипатерном.
            тобто щось на рівні сервісів ми вигрузили з БД, а щось ні, а потім чи то мапери, які ми використовуємо з ДТО,
            чи ось як тут просто для прикладу, коли ми використовуємо Entity у контролрері, то під капотом працює jackson
            бібліотека з spring-boot-web dependency, яка бачить що у нас ще є поле по зв'язку між таблицями і пробує
            ще її підгрузити

            тому, щоб сесія працювала так як ми очікуємо, а саме у сервісі з методами, які позначені @Transactional,
            тобто саме так як воно і мало би бути

            Отже, при спробі серіалізувати об'єкт до JSON можлива помилка:
                Could not write JSON: failed to lazily initialize a collection of role ...

        4 експеримент
            і тепер щоб це все працювало добре, можна додати Query + fetch  (distinct щоб не було дублікатів персон)
                    @Query("SELECT distinct p FROM Person p left join fetch p.notes WHERE p.id BETWEEN ?1 AND ?2")
                    List<Person> findAllByIdBetweenFetchNotes(Long startId, Long endId);

            у результаті все праює добре і швидко, бо маємо лише один запит до БД, де ми зразу підгрузили всю потрібну інфо

             запит http://localhost:9001/persons/1/1000 може займати аж до 60 мілісекунд
             запит http://localhost:9001/persons/1/5000 може займати аж до 125 мілісекунд
             запит http://localhost:9001/persons/1/10000 може займати аж до 250 мілісекунд


        УВАГА!
            це для прикладу ми тут використовуємо ентіті, але зазвичай ми використовуємо ДТО

     */
    @GetMapping("/{startId}/{endId}")
    public List<Person> getPersons(@PathVariable Long startId, @PathVariable Long endId) {
        return personRepository.findAllByIdBetween(startId, endId);
    }

    @GetMapping("/using-fetch/{startId}/{endId}")
    public List<Person> getPersonsFetchNotes(@PathVariable Long startId, @PathVariable Long endId) {
        return personRepository.findAllByIdBetweenFetchNotes(startId, endId);
    }


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
