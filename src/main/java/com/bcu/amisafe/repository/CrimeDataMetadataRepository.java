package com.bcu.amisafe.repository;
import com.bcu.amisafe.entity.CrimeDataMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CrimeDataMetadataRepository extends MongoRepository<CrimeDataMetadata, String> {

    Optional<CrimeDataMetadata> findFirstByOrderByLastUpdatedDesc();

}
