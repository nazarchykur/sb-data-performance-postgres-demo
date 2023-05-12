package com.example.sbdataperformancepostgresdemo.repository;

import com.example.sbdataperformancepostgresdemo.dto.NoteDto;
import com.example.sbdataperformancepostgresdemo.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long>, NoteCustomRepository {
    /*
        the method List<Note> findAllByIdBetween(Long startId, Long endId) will actually generate two queries:
            1. A query to fetch all the IDs between startId and endId.
            2. A query to fetch all the Note objects with those IDs.

        This is because the findAllByIdBetween() method uses the findAllById() method internally, which fetches the IDs
        first and then uses them to fetch the Note objects.


        So using this method we will get 1 query for notes in ids range + 1 query for each person
        So if we want to get 2 notes we will have 1 query for notes in ids range + 2 queries to get each person for its note
     */

    List<Note> findAllByIdBetween(Long startId, Long endId);



    @Query("SELECT n FROM Note n inner join fetch n.person WHERE n.id BETWEEN ?1 AND ?2")
    List<Note> findAllByIdBetweenWithPerson(Long startId, Long endId);

    // These 3 query are equivalent
//    @Query("""
//                SELECT new com.example.sbdataperformancepostgresdemo.dto.NoteDto(n.title, p.email)
//                FROM Note n inner join n.person p WHERE n.id BETWEEN ?1 AND ?2 """)
//    @Query("""
//                SELECT new com.example.sbdataperformancepostgresdemo.dto.NoteDto(n.title, p.email)
//                FROM Note n inner join n.person p WHERE n.id >= ?1 AND n.id < ?2 """)
    @Query("""
            SELECT new com.example.sbdataperformancepostgresdemo.dto.NoteDto(n.title, n.person.email)
            FROM Note n WHERE n.id BETWEEN ?1 AND ?2 """)
    List<NoteDto> findAllByIdBetweenUsingQuery(Long startId, Long endId);

}

/*
it is often necessary to use the DISTINCT keyword when using a LEFT JOIN FETCH query in JPA to avoid duplicate results.

 */

/*
    Ви можете використовувати параметри в анотації `@Query` за допомогою іменованих параметрів або позиційних параметрів.
    Ось приклади обох способів:

    1. Іменовані параметри:

        @Query("SELECT p FROM Person p WHERE p.name = :name AND p.age = :age")
        List<Person> findByNameAndAge(@Param("name") String name, @Param("age") int age);

    У цьому прикладі ми використовуємо іменовані параметри `:name` та `:age` для передачі значень до запиту.
    Ми також використовуємо анотацію `@Param` для вказівки імен параметрів.

    2. Позиційні параметри:

        @Query("SELECT p FROM Person p WHERE p.name = ?1 AND p.age = ?2")
        List<Person> findByNameAndAge(String name, int age);

    У цьому прикладі ми використовуємо позиційні параметри `?1` та `?2` для передачі значень до запиту.
    Перший параметр відповідає `name`, а другий - `age`.

    Обидва способи є еквівалентними і можуть бути використані в залежності від вашого вибору. Іменовані параметри можуть
    бути корисні, якщо ви маєте багато параметрів, оскільки вони дозволяють вам явно вказати, який параметр
    використовується для якої умови. Позиційні параметри можуть бути корисні, якщо ви маєте менше параметрів, оскільки
    вони дозволяють вам просто вказати порядок параметрів.

*/