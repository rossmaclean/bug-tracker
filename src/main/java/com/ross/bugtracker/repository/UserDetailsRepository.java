package com.ross.bugtracker.repository;

import com.ross.bugtracker.model.UserDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsRepository extends MongoRepository<UserDetails, String> {
}
