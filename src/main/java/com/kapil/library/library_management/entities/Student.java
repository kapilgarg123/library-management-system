package com.kapil.library.library_management.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"transactions", "rentals"})
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name too long")
    private String name;

    @PositiveOrZero(message = "Wallet balance must be zero or positive")
    private Double walletBalance;

    @OneToMany(mappedBy = "student")
    @JsonManagedReference("student-transactions")
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "student")
    @JsonManagedReference("student-rentals")
    private List<Rental> rentals;
}
