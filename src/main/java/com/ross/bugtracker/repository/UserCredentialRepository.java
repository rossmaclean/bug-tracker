package com.ross.bugtracker.repository;

import com.ross.bugtracker.model.UserCredentials;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCredentialRepository extends MongoRepository<UserCredentials, String> {
}
