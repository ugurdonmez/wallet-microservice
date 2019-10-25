package com.ugurdonmez.wallet.service;

import com.ugurdonmez.wallet.domain.player.Player;
import com.ugurdonmez.wallet.domain.player.PlayerRepository;
import com.ugurdonmez.wallet.domain.player.exceptions.PlayerBalanceIsLessThanZeroException;
import com.ugurdonmez.wallet.domain.player.exceptions.PlayerNotFoundException;
import com.ugurdonmez.wallet.domain.transaction.Transaction;
import com.ugurdonmez.wallet.domain.transaction.TransactionRepository;
import com.ugurdonmez.wallet.domain.transaction.TransactionType;
import com.ugurdonmez.wallet.domain.transaction.exception.TransactionAmountLessThanZeroException;
import com.ugurdonmez.wallet.domain.transaction.exception.TransactionIdIsNotUniqueException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WalletService {

    private final PlayerRepository playerRepository;
    private final TransactionRepository transactionRepository;

    public BigDecimal getPlayerBalance(UUID playerId) {
        return playerRepository.findById(playerId)
                .map(Player::getBalance)
                .orElseThrow(() -> new PlayerNotFoundException(playerId));
    }

    public Player transaction(UUID playerId, Transaction transaction) {
        if (transactionRepository.existsById(transaction.getId())) {
            throw new TransactionIdIsNotUniqueException(transaction);
        }

        if (transaction.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new TransactionAmountLessThanZeroException(transaction);
        }

        return playerRepository.findById(playerId)
                .map(player -> {
                    BigDecimal balance = transaction.getType() == TransactionType.CREDIT ?
                            player.getBalance().add(transaction.getAmount()) :
                            player.getBalance().subtract(transaction.getAmount());

                    if (balance.compareTo(BigDecimal.ZERO) < 0) {
                        throw new PlayerBalanceIsLessThanZeroException(playerId);
                    }

                    player.getTransactions().add(transaction);
                    player.setBalance(balance);

                    return playerRepository.saveAndFlush(player);
                })
                .orElseThrow(() -> new PlayerNotFoundException(playerId));
    }

    public List<Transaction> getTransactions(UUID playerId) {
        return playerRepository.findById(playerId)
                .map(Player::getTransactions)
                .orElseThrow(() -> new PlayerNotFoundException(playerId));
    }
}
