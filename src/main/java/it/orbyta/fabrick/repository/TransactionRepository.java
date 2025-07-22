package it.orbyta.fabrick.repository;

import it.orbyta.fabrick.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, String> {
}
