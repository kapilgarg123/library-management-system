package com.kapil.library.library_management.services;

import com.kapil.library.library_management.dtos.TransactionResponse;
import com.kapil.library.library_management.entities.Book;
import com.kapil.library.library_management.entities.Rental;
import com.kapil.library.library_management.entities.Student;
import com.kapil.library.library_management.entities.Transaction;
import com.kapil.library.library_management.exceptions.ResourceNotFoundException;
import com.kapil.library.library_management.repositories.BookRepository;
import com.kapil.library.library_management.repositories.RentalRepository;
import com.kapil.library.library_management.repositories.StudentRepository;
import com.kapil.library.library_management.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Data
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final StudentRepository studentRepository;
    private final BookRepository bookRepository;
    private final RentalRepository rentalRepository;

    public List<Transaction> getTransactionsByStudentId(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student with id " + studentId + " not found"));

        return transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getStudent().getId().equals(studentId))
                .toList();
    }

    @Transactional
    public TransactionResponse buyBook(Long studentId, Long bookId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        if (student.getWalletBalance() < book.getPrice()) {
            return new TransactionResponse(book.getTitle(), "BUY", 0,
                    student.getWalletBalance(), 0, 0, book.getPrice() - student.getWalletBalance(),
                    "Insufficient balance to buy book.");
        }

        if (book.getAvailableCopies() <= 0) {
            return new TransactionResponse(book.getTitle(), "BUY", 0,
                    student.getWalletBalance(), 0, 0, 0,
                    "Book is out of stock.");
        }

        student.setWalletBalance(student.getWalletBalance() - book.getPrice());
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        studentRepository.save(student);
        bookRepository.save(book);

        Transaction transaction = new Transaction();
        transaction.setStudent(student);
        transaction.setBook(book);
        transaction.setTransactionType("BUY");
        transaction.setAmount(book.getPrice());
        transactionRepository.save(transaction);

        return new TransactionResponse(book.getTitle(), "BUY", book.getPrice(),
                student.getWalletBalance(), 0, 0, 0,
                "Book purchased successfully.");
    }

    @Transactional
    public TransactionResponse rentBook(Long studentId, Long bookId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        if (student.getWalletBalance() < book.getRentPrice()) {
            return new TransactionResponse(book.getTitle(), "RENT", 0,
                    student.getWalletBalance(), 0, 0, book.getRentPrice() - student.getWalletBalance(),
                    "Insufficient balance to rent book.");
        }

        if (book.getAvailableCopies() <= 0) {
            return new TransactionResponse(book.getTitle(), "RENT", 0,
                    student.getWalletBalance(), 0, 0, 0,
                    "Book is out of stock.");
        }

        student.setWalletBalance(student.getWalletBalance() - book.getRentPrice());
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        studentRepository.save(student);
        bookRepository.save(book);

        Rental rental = new Rental();
        rental.setStudent(student);
        rental.setBook(book);
        rental.setActive(true);
        rentalRepository.save(rental);

        Transaction transaction = new Transaction();
        transaction.setStudent(student);
        transaction.setBook(book);
        transaction.setTransactionType("RENT");
        transaction.setAmount(book.getRentPrice());
        transactionRepository.save(transaction);

        return new TransactionResponse(book.getTitle(), "RENT", book.getRentPrice(),
                student.getWalletBalance(), 0, 0, 0,
                "Book rented successfully.");
    }

    @Transactional
    public TransactionResponse returnBook(Long studentId, Long bookId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        Rental rental = rentalRepository.findTopByStudentIdAndBookIdAndActiveTrueOrderByRentDateAsc(studentId, bookId)
                .orElseThrow(() -> new ResourceNotFoundException("No active rental found"));

        LocalDateTime rentDate = rental.getRentDate();
        LocalDateTime returnDate = LocalDateTime.now().plusDays(10);
        long days = Duration.between(rentDate, returnDate).toDays();

        double fine = 0.0;
        double minimumRecharge = 0.0;

        if (days > 7) {
            fine = (days - 7) * book.getRentPrice();
            if (student.getWalletBalance() < fine) {
                minimumRecharge = fine - student.getWalletBalance();
                return new TransactionResponse(book.getTitle(), "RETURN", 0,
                        student.getWalletBalance(), days, fine, minimumRecharge, "Insufficient balance to pay fine. Please recharge your wallet.");
            } else {
                student.setWalletBalance(student.getWalletBalance() - fine);
            }
        }

        rental.setReturnDate(returnDate);
        rental.setActive(false);
        rentalRepository.save(rental);

        book.setAvailableCopies(book.getAvailableCopies() + 1);
        studentRepository.save(student);
        bookRepository.save(book);

        Transaction returnTransaction = new Transaction();
        returnTransaction.setStudent(student);
        returnTransaction.setBook(book);
        returnTransaction.setTransactionType("RETURN");
        returnTransaction.setAmount(fine);
        transactionRepository.save(returnTransaction);

        return new TransactionResponse(book.getTitle(), "RETURN", fine,
                student.getWalletBalance(), days, fine, minimumRecharge, "Book returned successfully.");
    }
}