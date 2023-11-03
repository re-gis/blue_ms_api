package com.merci.blue.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "exams")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String examname;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courses_id")
    private Course course;

    private String examdoc;

    private String answersheet;
}
