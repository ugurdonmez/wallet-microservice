package com.ugurdonmez.wallet.domain.transaction.exception;

import com.ugurdonmez.wallet.domain.transaction.Transaction;

public class TransactionAmountLessThanZeroException extends RuntimeException {

    public TransactionAmountLessThanZeroException(Transaction t) {
        super("Transaction amount is less than zero + " +
                t.getId() + " " +
                t.getAmount());
    }

}
