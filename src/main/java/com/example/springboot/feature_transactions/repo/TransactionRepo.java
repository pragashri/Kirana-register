package com.example.springboot.feature_transactions.repo;

import com.example.springboot.feature_transactions.entities.Transaction;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepo extends MongoRepository<Transaction, ObjectId> {

    @Query("{ 'createdAt' : { $gte: ?0, $lt: ?1 } }")
    List<Transaction> findTransactionsBetween(LocalDateTime start, LocalDateTime end);

    Transaction findById(String id);
}
