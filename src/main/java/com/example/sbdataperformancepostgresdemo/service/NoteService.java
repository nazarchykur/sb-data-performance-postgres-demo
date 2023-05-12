package com.example.sbdataperformancepostgresdemo.service;

import com.example.sbdataperformancepostgresdemo.dto.NoteDto;
import com.example.sbdataperformancepostgresdemo.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    /*
        @Transactional(readOnly = true) is used to indicate that the method should be executed within a read-only
        transaction. It provides a hint to the underlying infrastructure that the transaction can be optimized for
        reading data, such as by disabling the flush of the persistence context at the end of the transaction.

        However, @Transactional(readOnly = true) does not guarantee that the transaction will be read-only.
        In some cases, such as when using certain database providers or configurations, the transaction may still
        perform write operations, even if the method is marked as read-only. Therefore, it is important to use
        @Transactional(readOnly = true) with caution and to ensure that the code is actually read-only, if that is the
        intended behavior.
     */

    /*
        тут є інше але, яке повязане з вигрузкою даних з БД та опрацюванні на стороні java:
          - 1) вигружаються в ріквесті з БД до java всі колонки
                (тобто Hibernate загрузить всі ці ентіті з усіма полями собі у сесію)
                по факту нам не потрібні всі ці дані з ентіті бо ми їх зрузу тут перемаплюємо у ДТО
          - 2) немає JOIN, а для потрібних даних йде додатковий запит у БД
          - 3) можна поставити @Transactional(readOnly = true) щоб Hibernate не робив додаткових дій для dirty checking
                але тут всеодно є неефективно те, що Hibernate загрузить всі ці ентіті з усіма полями

       тобто так робити НЕ правильно і не ефективно
     */

    @Transactional(readOnly = true)
    public List<NoteDto> getAllNotes(Long startId, Long endId) {
        // тут ми беремо Note з БД і на стороні java перемаплюємо у наш ДТО, що є неефективно, бо Hibernate загрузить
        // собі у сесія всі ці ентіті з всіма полями, а нам потрібно, як видно, тільки 2 поля з 2 ентіті
        // findAllByIdBetween - це метод працює як N+1
        return noteRepository.findAllByIdBetween(startId, endId).stream()
                .map(note -> new NoteDto(note.getTitle(), note.getPerson().getEmail()))
                .toList();
    }


    @Transactional(readOnly = true)
    public List<NoteDto> findAllByIdBetweenWithPerson(Long startId, Long endId) {
        // тут ми беремо Note з БД і на стороні java перемаплюємо у наш ДТО, що є неефективно, бо Hibernate загрузить
        // собі у сесія всі ці ентіті з всіма полями, а нам потрібно, як видно, тільки 2 поля з 2 ентіті
        return noteRepository.findAllByIdBetweenWithPerson(startId, endId).stream()
                .map(note -> new NoteDto(note.getTitle(), note.getPerson().getEmail()))
                .toList();
    }
    /*
        використання ДТО є одним із найкращих способів, який можна використовувати, щоб отримати дані з БД, які нам потрібні
        тільки для читання і ще якщо там є агрегування даних (коли нам потрібні тільки конкретні поля з кількох таблиць)
     */

    @Transactional(readOnly = true)
    public List<NoteDto> getAllNoteDtosUsingQuery(Long startId, Long endId) {
        return noteRepository.findAllByIdBetweenUsingQuery(startId, endId);
    }

    @Transactional(readOnly = true)
    public List<NoteDto> getAllNoteDtosUsingGeneric(Long startId, Long endId) {
        return noteRepository.findAllBetweenIdsUsingGeneric(startId, endId, NoteDto.class);
    }
}
