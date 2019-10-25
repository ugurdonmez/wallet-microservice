package com.ugurdonmez.wallet.service;

import com.ugurdonmez.wallet.domain.player.Player;
import com.ugurdonmez.wallet.domain.player.PlayerRepository;
import com.ugurdonmez.wallet.domain.player.exceptions.PlayerNotFoundException;
import com.ugurdonmez.wallet.domain.transaction.Transaction;
import com.ugurdonmez.wallet.domain.transaction.TransactionRepository;
import com.ugurdonmez.wallet.domain.transaction.TransactionType;
import com.ugurdonmez.wallet.domain.transaction.exception.TransactionAmountLessThanZeroException;
import com.ugurdonmez.wallet.domain.transaction.exception.TransactionIdIsNotUniqueException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WalletServiceTest {

    @Autowired
    private WalletService walletService;

    @MockBean
    private PlayerRepository playerRepository;

    @MockBean
    private TransactionRepository transactionRepository;

    @Test
    void testGetPlayerBalance() {
        Player player = new Player(UUID.randomUUID(), BigDecimal.TEN, new LinkedList<>());

        when(playerRepository.findById(player.getId()))
                .thenReturn(Optional.of(player));

        assertEquals(walletService.getPlayerBalance(player.getId()), BigDecimal.TEN);
    }

    @Test
    void testGetPlayerBalanceTestPlayerNotFound() {
        when(playerRepository.findById(UUID.fromString("7d8d3558-ed6a-4da0-adb3-bcc61bc9c5c2")))
                .thenReturn(Optional.empty());

        assertThrows(
                PlayerNotFoundException.class,
                () -> walletService.getPlayerBalance(UUID.fromString("7d8d3558-ed6a-4da0-adb3-bcc61bc9c5c2")),
                "Should throw PlayerNotFoundException"
        );
    }

    @Test
    void testTransactionTxIdIsNotUnique() {
        when(transactionRepository.existsById(UUID.fromString("7d8d3558-ed6a-4da0-adb3-bcc61bc9c5c2")))
                .thenReturn(true);

        Transaction tx = new Transaction(UUID.fromString("7d8d3558-ed6a-4da0-adb3-bcc61bc9c5c2"), BigDecimal.ONE, TransactionType.DEBIT);

        assertThrows(TransactionIdIsNotUniqueException.class,
                () -> walletService.transaction(UUID.randomUUID(), tx),
                "Should throw TransactionIdIsNotUniqueException");
    }

    @Test
    void testTransactionAmountLessThanZero() {
        Transaction tx = new Transaction(UUID.fromString("7d8d3558-ed6a-4da0-adb3-bcc61bc9c5c2"), BigDecimal.valueOf(-1), TransactionType.DEBIT);

        assertThrows(TransactionAmountLessThanZeroException.class,
                () -> walletService.transaction(UUID.randomUUID(), tx),
                "Should throw TransactionAmountLessThanZeroException");
    }

    @Test
    void testTransactionPlayerNotFound() {
        Transaction tx = new Transaction(UUID.fromString("7d8d3558-ed6a-4da0-adb3-bcc61bc9c5c2"), BigDecimal.TEN, TransactionType.DEBIT);

        when(playerRepository.findById(UUID.fromString("7d8d3558-ed6a-4da0-adb3-bcc61bc9c5c2")))
                .thenReturn(Optional.empty());

        assertThrows(
                PlayerNotFoundException.class,
                () -> walletService.transaction(UUID.fromString("7d8d3558-ed6a-4da0-adb3-bcc61bc9c5c2"), tx),
                "Should throw PlayerNotFoundException");
    }

    @Test
    void testTransactionCredit() {
        Player player = new Player(UUID.randomUUID(), BigDecimal.TEN, new LinkedList<>());
        Transaction tx = new Transaction(UUID.randomUUID(), BigDecimal.ONE, TransactionType.CREDIT);

        when(playerRepository.findById(player.getId()))
                .thenReturn(Optional.of(player));

        when(playerRepository.saveAndFlush(player))
                .thenReturn(player);

        assertEquals(walletService.transaction(player.getId(), tx).getBalance(), BigDecimal.valueOf(11));
    }

    @Test
    void testTransactionDebit() {
        Transaction tx = new Transaction(UUID.randomUUID(), BigDecimal.ONE, TransactionType.DEBIT);
        Player player = new Player(UUID.randomUUID(), BigDecimal.TEN, new LinkedList<>());

        when(playerRepository.findById(player.getId()))
                .thenReturn(Optional.of(player));

        when(playerRepository.saveAndFlush(player))
                .thenReturn(player);

        assertEquals(walletService.transaction(player.getId(), tx).getBalance(), BigDecimal.valueOf(9));
    }

    @Test
    void testGetTransactionPlayerNotFound() {
        when(playerRepository.findById(UUID.fromString("7d8d3558-ed6a-4da0-adb3-bcc61bc9c5c2")))
                .thenReturn(Optional.empty());

        assertThrows(
                PlayerNotFoundException.class,
                () -> walletService.getTransactions(UUID.fromString("7d8d3558-ed6a-4da0-adb3-bcc61bc9c5c2")),
                "Should throw PlayerNotFoundException");
    }

    @Test
    void testGetTransactionEmpty() {
        Player player = new Player(UUID.randomUUID(), BigDecimal.TEN, new LinkedList<>());

        when(playerRepository.findById(player.getId()))
                .thenReturn(Optional.of(player));

        assertEquals(walletService.getTransactions(player.getId()).size(), 0);
    }

    @Test
    void testGetTransactions() {
        Transaction tx1 = new Transaction(UUID.randomUUID(), BigDecimal.ONE, TransactionType.DEBIT);
        Transaction tx2 = new Transaction(UUID.randomUUID(), BigDecimal.ONE, TransactionType.DEBIT);
        Player player = new Player(UUID.randomUUID(), BigDecimal.TEN, new LinkedList<>(Arrays.asList(tx1, tx2)));

        when(playerRepository.findById(player.getId()))
                .thenReturn(Optional.of(player));

        assertEquals(walletService.getTransactions(player.getId()).size(), 2);
    }


}