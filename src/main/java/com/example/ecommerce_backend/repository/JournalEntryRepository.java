package com.example.ecommerce_backend.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.ecommerce_backend.entity.JournalEntry;


public interface JournalEntryRepository extends MongoRepository<JournalEntry, ObjectId> {
    
}
