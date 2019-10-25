package com.ugurdonmez.wallet.domain.player.exceptions;

import java.util.UUID;

public class PlayerBalanceIsLessThanZeroException extends RuntimeException {
    public PlayerBalanceIsLessThanZeroException(UUID id) {
        super("Player does not have enough amount of money " + id);
    }
}
