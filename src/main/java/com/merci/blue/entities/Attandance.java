package com.merci.blue.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "attandances")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attandance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "class_id")
    private Class aClass;

    private LocalDate date;
    private boolean present;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "student_id")
    private Student student;
}
