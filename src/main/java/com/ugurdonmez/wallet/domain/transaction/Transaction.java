package com.ugurdonmez.wallet.domain.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transaction {
    @Id
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;
    private BigDecimal amount;
    private TransactionType type;
}
