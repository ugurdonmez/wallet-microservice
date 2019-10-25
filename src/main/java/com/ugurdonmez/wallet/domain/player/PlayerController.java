package com.ugurdonmez.wallet.domain.player;

import com.ugurdonmez.wallet.domain.transaction.Transaction;
import com.ugurdonmez.wallet.service.WalletService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class PlayerController {

    private final WalletService walletService;

    @GetMapping("/wallet/player/{id}")
    BigDecimal one(@PathVariable UUID id) {
        return walletService.getPlayerBalance(id);
    }

    @PostMapping("/wallet/player/{id}")
    Player transaction(@PathVariable UUID id, @RequestBody Transaction newTransaction) {
        return walletService.transaction(id, newTransaction);
    }

    @GetMapping("/wallet/player/{id}/transactions")
    List<Transaction> getTransactions(@PathVariable UUID id) {
        return walletService.getTransactions(id);
    }
}
