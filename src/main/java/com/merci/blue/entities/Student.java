package com.merci.blue.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.merci.blue.enums.EGender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "students")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Student {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false)
    private Date dob;

    @Column(nullable = false)
    private String contact;

    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    private EGender gender;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "parent_id")
    private Parent parent = null;
}
