package com.ugurdonmez.wallet.domain.transaction.exception;


import com.ugurdonmez.wallet.domain.transaction.Transaction;

public class TransactionIdIsNotUniqueException extends RuntimeException {
    public TransactionIdIsNotUniqueException(Transaction t) {
        super("Transaction is not unique + " + t.getId());
    }
}
