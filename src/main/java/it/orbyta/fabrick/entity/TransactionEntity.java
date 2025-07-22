package it.orbyta.fabrick.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity(name = "money_transfer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String transactionId;

    String operationId;
    LocalDate accountingDate;
    LocalDate valueDate;
    BigDecimal amount;
    String currency;
    String description;


}
