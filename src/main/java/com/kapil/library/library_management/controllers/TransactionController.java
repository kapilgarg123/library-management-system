package com.kapil.library.library_management.controllers;


import com.kapil.library.library_management.dtos.TransactionResponse;
import com.kapil.library.library_management.entities.Transaction;
import com.kapil.library.library_management.services.TransactionService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Data
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/{studentId}")
    public ResponseEntity<List<Transaction>> getTransactionsByStudentId(@PathVariable Long studentId) {
        List<Transaction> transactions = transactionService.getTransactionsByStudentId(studentId);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/buy")
    public ResponseEntity<TransactionResponse> buyBook(@RequestParam Long studentId, @RequestParam Long bookId) {
        TransactionResponse response = transactionService.buyBook(studentId, bookId);
        if (response.getTransactionAmount() == 0) {
            return ResponseEntity
                    .badRequest()
                    .body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/rent")
    public ResponseEntity<TransactionResponse> rentBook(@RequestParam Long studentId, @RequestParam Long bookId) {
        TransactionResponse response = transactionService.rentBook(studentId, bookId);
        if (response.getTransactionAmount() == 0) {
            return ResponseEntity
                    .badRequest()
                    .body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/return")
    public ResponseEntity<TransactionResponse> returnBook(@RequestParam Long studentId, @RequestParam Long bookId) {
        TransactionResponse response = transactionService.returnBook(studentId, bookId);
        if (response.getRequiredRechargeAmount() > 0) {
            return ResponseEntity
                    .badRequest()
                    .body(response);
        }
        return ResponseEntity.ok(response);
    }
}