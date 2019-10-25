package com.ugurdonmez.wallet.domain.transaction.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class TransactionIdIsNotUniqueAdvice  {

    @ResponseBody
    @ExceptionHandler(TransactionIdIsNotUniqueException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String transactionIdIsNotUniquesHandler(TransactionIdIsNotUniqueException ex) {
        return ex.getMessage();
    }
}