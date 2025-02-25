package com.bcu.amisafe.service;

import com.bcu.amisafe.entity.Crime;
import com.bcu.amisafe.entity.CrimeDataMetadata;
import com.bcu.amisafe.exception.ApiClientException;
import com.bcu.amisafe.exception.ApiServerException;
import com.bcu.amisafe.exception.NoMetadataFoundException;
import com.bcu.amisafe.repository.CrimeDataMetadataRepository;
import com.bcu.amisafe.repository.CrimeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Objects;

@Service
public class CrimeCacheServiceImpl implements CrimeCacheService {

    private final CrimeRepository crimeRepository;
    private final CrimeDataMetadataRepository crimeDataMetadataRepository;
    private final RestClient restClient;
    private final String policeApiLastUpdatedURL;

    public CrimeCacheServiceImpl(
            CrimeRepository crimeRepository,
            CrimeDataMetadataRepository crimeDataMetadataRepository,
            @Value("${police.api.url.last-updated}") String policeApiLastUpdatedURL,
            RestClient restClient
    ) {
        this.crimeRepository = crimeRepository;
        this.crimeDataMetadataRepository = crimeDataMetadataRepository;
        this.policeApiLastUpdatedURL = policeApiLastUpdatedURL;
        this.restClient = restClient;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean reloadCrimeData() {
        CrimeDataMetadata apiMetadata = restClient.get()
                .uri(policeApiLastUpdatedURL)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    throw new ApiClientException("Client error fetching metadata: " + res.getStatusCode());
                })
                .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                    throw new ApiServerException("Server error fetching metadata: " + res.getStatusCode());
                })
                .body(CrimeDataMetadata.class);
        CrimeDataMetadata dbMetadata = crimeDataMetadataRepository.findFirstByOrderByLastUpdatedDesc()
                .orElseThrow(() -> new NoMetadataFoundException("No crime metadata found in the database"));
        return Objects.equals(apiMetadata.getLastUpdated(), dbMetadata.getLastUpdated());
    }

    @Async
    public void saveCrimesAsync(List<Crime> crimes) {
        if (!CollectionUtils.isEmpty(crimes)) {
            crimeRepository.saveAll(crimes);
        }
    }
}

