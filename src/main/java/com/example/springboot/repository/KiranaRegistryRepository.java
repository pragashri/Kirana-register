package com.example.springboot.repository;

import com.example.springboot.model.KiranaRegistry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.bson.types.ObjectId;

public interface KiranaRegistryRepository extends MongoRepository<KiranaRegistry, ObjectId> {
    KiranaRegistry findFirstByOrderByIdDesc();  // Fetch the most recent (or first) record, as there's only one record
}
