package com.placebook.places;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlaceRepository extends MongoRepository<Place, String> {
    Place findByFbId(String id);
}