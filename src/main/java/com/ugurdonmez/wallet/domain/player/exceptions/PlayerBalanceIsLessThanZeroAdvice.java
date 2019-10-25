package com.ugurdonmez.wallet.domain.player.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class PlayerBalanceIsLessThanZeroAdvice {
    @ResponseBody
    @ExceptionHandler(PlayerBalanceIsLessThanZeroException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    String playerBalanceIsLessThanZeroHandler(PlayerBalanceIsLessThanZeroException ex) {
        return ex.getMessage();
    }
}
