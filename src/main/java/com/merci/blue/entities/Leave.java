package com.merci.blue.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.merci.blue.enums.EStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "teachers_leaves")
public class Leave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String reason;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    private String resignationLetter;

    @Enumerated(EnumType.STRING)
    private EStatus status;
}
