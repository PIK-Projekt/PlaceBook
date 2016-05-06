package com.placebook.users;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface PersonRepository extends MongoRepository<Person, String> {
    Person findByFbId(String id);

}