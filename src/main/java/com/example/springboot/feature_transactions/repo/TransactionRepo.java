package com.example.springboot.feature_transactions.repo;

import com.example.springboot.feature_transactions.entities.Transaction;
import java.time.LocalDateTime;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * Repository interface for performing CRUD operations on {@link Transaction} entities. Extends
 * {@link MongoRepository} to leverage MongoDB's built-in methods for data access.
 */
public interface TransactionRepo extends MongoRepository<Transaction, ObjectId> {

    /**
     * Retrieves a list of transactions that were created within the specified time range.
     *
     * @param start The start date/time for the transaction search.
     * @param end The end date/time for the transaction search.
     * @return A list of {@link Transaction} objects created within the specified time range.
     */
    @Query("{ 'createdAt' : { $gte: ?0, $lt: ?1 } }")
    List<Transaction> findTransactionsBetween(LocalDateTime start, LocalDateTime end);

    /**
     * Retrieves a transaction by its unique identifier.
     *
     * @param id The ID of the transaction to retrieve.
     * @return The {@link Transaction} entity corresponding to the given ID.
     */
    Transaction findById(String id);
}
