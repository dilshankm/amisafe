package com.bcu.amisafe.repository;

import com.bcu.amisafe.entity.Crime;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrimeRepository extends MongoRepository<Crime, String> {

    List<Crime> findByLocationLatitudeAndLocationLongitude(String latitude, String longitude);

}
