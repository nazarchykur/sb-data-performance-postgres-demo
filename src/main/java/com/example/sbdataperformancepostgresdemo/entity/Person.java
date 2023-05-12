package com.example.sbdataperformancepostgresdemo.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "persons",
        uniqueConstraints = @UniqueConstraint(columnNames = "email", name = "persons_email_uq"))
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "age")
    private Integer age;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JsonManagedReference // only for test purpose when we work in controller with entities (usually we use Dto)
    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<Note> notes = new ArrayList<>();


    @JsonManagedReference // only for test purpose when we work in controller with entities (usually we use Dto)
    @Setter(AccessLevel.PRIVATE)
//    @BatchSize(size = 100)
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<Task> tasks = new ArrayList<>();

    public void addNote(Note note) {
        note.setPerson(this);
        this.notes.add(note);
    }

    public void removeNote(Note note) {
        this.notes.remove(note);
        note.setPerson(null);
    }

    public void addTask(Task task) {
        task.setPerson(this);
        this.tasks.add(task);
    }

    public void removeTask(Task task) {
        this.tasks.remove(task);
        task.setPerson(null);
    }

}