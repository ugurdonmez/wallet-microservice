package com.ugurdonmez.wallet.domain.player;

import com.ugurdonmez.wallet.domain.transaction.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Player {
    @Id
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    private BigDecimal balance;

    @OneToMany(targetEntity = Transaction.class, cascade = {CascadeType.ALL})
    private List<Transaction> transactions;
}
