package com.kapil.library.library_management.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private String bookTitle;
    private String transactionType; // BUY or RENT or RETURN
    private double transactionAmount; // money deducted
    private double currentWalletBalance; // updated wallet balance
    private long rentalDurationDays; // only for return transactions
    private double fineAmount; // only for return transactions
    private double requiredRechargeAmount; // if balance insufficient
    private String responseMessage;
}

