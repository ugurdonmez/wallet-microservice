package com.ugurdonmez.wallet;

import com.ugurdonmez.wallet.domain.player.Player;
import com.ugurdonmez.wallet.domain.player.PlayerRepository;
import com.ugurdonmez.wallet.domain.transaction.Transaction;
import com.ugurdonmez.wallet.domain.transaction.TransactionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Configuration
@Slf4j
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(PlayerRepository playerRepository) {
        return args -> {
            List<Transaction> list = new LinkedList<>();
            list.add(new Transaction(UUID.randomUUID(), BigDecimal.ONE, TransactionType.DEBIT));

            log.info("Preloading " + playerRepository.saveAndFlush(new Player(UUID.randomUUID(), BigDecimal.TEN, new LinkedList<>())));
            log.info("Preloading " + playerRepository.saveAndFlush(new Player(UUID.randomUUID(), BigDecimal.TEN, list)));
            log.info("Preloading " + playerRepository.saveAndFlush(new Player(UUID.fromString("e2f1b602-d7b3-4eb2-984d-2d8f9e289511"), BigDecimal.TEN, new LinkedList<>())));
        };
    }
}
