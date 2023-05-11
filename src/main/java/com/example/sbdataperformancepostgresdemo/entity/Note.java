package com.example.sbdataperformancepostgresdemo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


@Entity
@Table(name = "notes")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"person"})
@EqualsAndHashCode(exclude = {"person"})
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JsonBackReference // only for test purpose when we work in controller with entities (usually we use Dto)
    @ManyToOne(optional = false) // it means that the relationship is mandatory and the foreign key cannot be null
    @JoinColumn(name = "person_id")
    private Person person;

}

/*
    In a bidirectional OneToMany/ManyToOne relationship, the `optional` attribute should be used on the child side
    (i.e., the ManyToOne side).

    The `optional` attribute specifies whether the association is optional (i.e., whether the foreign key column can
    contain null values). If `optional` is set to `false`, it means that the association is required and the foreign key
    column cannot contain null values. If `optional` is set to `true` (= that is default), it means that the association
    is optional and the foreign key column can contain null values.

    In general, you should set `optional` to `false` on the ManyToOne side if the association is required
    (i.e., every child entity must have a parent entity), and to `true` if the association is optional
    (i.e., some child entities may not have a parent entity).

    On the OneToMany side, you do not need to specify the `optional` attribute, as it is inferred from the ManyToOne side.
    If the ManyToOne side has `optional` set to `false`, it means that the OneToMany side is required
    (i.e., every parent entity must have at least one child entity). If the ManyToOne side has `optional` set to `true`,
    it means that the OneToMany side is optional (i.e., some parent entities may not have any child entities).

    So, you should use `optional` on the ManyToOne side to specify whether the association is required or optional.

 */