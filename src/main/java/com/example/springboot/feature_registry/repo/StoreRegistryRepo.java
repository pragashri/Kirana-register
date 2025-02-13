package com.example.springboot.feature_registry.repo;

import com.example.springboot.feature_registry.entities.StoreRegistry;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StoreRegistryRepo extends MongoRepository<StoreRegistry, String> {

    /**
     * Fetch the most recent (or first) record, as there's only one record
     *
     * @return
     */
    StoreRegistry findFirstByOrderByIdDesc();
}
