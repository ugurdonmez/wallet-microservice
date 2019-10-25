package com.ugurdonmez.wallet.domain.transaction.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class TransactionAmountLessThanZeroAdvice  {
    @ResponseBody
    @ExceptionHandler(TransactionAmountLessThanZeroException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    String transactionAmountLessThanZeroHandler(TransactionAmountLessThanZeroException ex) {
        return ex.getMessage();
    }
}
