package com.example.ecommerce_backend.repository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.ecommerce_backend.entity.User;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByUsername(String username);
}
