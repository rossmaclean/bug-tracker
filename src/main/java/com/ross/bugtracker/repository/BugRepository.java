package com.ross.bugtracker.repository;

import com.ross.bugtracker.model.Bug;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BugRepository extends MongoRepository<Bug, String> {
}
