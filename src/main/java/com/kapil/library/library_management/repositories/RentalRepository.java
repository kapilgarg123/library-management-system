package com.kapil.library.library_management.repositories;

import com.kapil.library.library_management.entities.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

    @Query("select r from Rental r where r.student.id = :studentId and r.book.id = :bookId and r.active = true order by r.rentDate limit 1")
    Optional<Rental> findTopByStudentIdAndBookIdAndActiveTrueOrderByRentDateAsc(Long studentId, Long bookId);

}
