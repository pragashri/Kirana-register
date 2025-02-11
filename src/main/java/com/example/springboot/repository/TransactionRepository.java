package com.example.springboot.repository;

import com.example.springboot.model.Transaction;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, ObjectId> {

    @Query("{ 'employeeId': ?0 }")
    List<Transaction> findByEmployeeId(ObjectId employeeId);

    @Query("{ 'ownerId': ?0 }")
    List<Transaction> findByOwnerId(ObjectId ownerId);

    @Query("{ 'transactionStatus': ?0 }")
    List<Transaction> findByTransactionStatus(String transactionStatus);
}
