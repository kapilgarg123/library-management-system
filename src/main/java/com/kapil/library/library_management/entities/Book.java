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
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title too long")
    private String title;

    @NotBlank(message = "Author is required")
    @Size(max = 255, message = "Author name too long")
    private String author;

    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price must be zero or positive")
    private Double price;

    @NotNull(message = "Rent price is required")
    @PositiveOrZero(message = "Rent price must be zero or positive")
    private Double rentPrice;

    @Min(value = 0, message = "Available copies cannot be negative")
    private int availableCopies;

    @OneToMany(mappedBy = "book")
    @JsonManagedReference("book-transactions")
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "book")
    @JsonManagedReference("book-rentals")
    private List<Rental> rentals;
}
