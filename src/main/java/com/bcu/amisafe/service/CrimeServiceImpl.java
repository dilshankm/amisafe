package com.bcu.amisafe.service;

import com.bcu.amisafe.entity.Crime;
import com.bcu.amisafe.exception.ApiClientException;
import com.bcu.amisafe.exception.ApiServerException;
import com.bcu.amisafe.repository.CrimeRepository;
import com.bcu.amisafe.utils.GeoUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CrimeServiceImpl implements CrimeService {

    private final CrimeRepository crimeRepository;
    private final CrimeCacheService crimeCacheService;
    private final RestClient restClient;

    @Value("${police.api.url.crime}")
    private String policeApiCrimeURL;

    private static final Logger logger = LoggerFactory.getLogger(CrimeServiceImpl.class);

    public CrimeServiceImpl(CrimeRepository crimeRepository, CrimeCacheService crimeCacheService,
                            @Value("${police.api.url.crime}") String policeApiCrimeURL,
                            RestClient restClient) {
        this.crimeRepository = crimeRepository;
        this.crimeCacheService = crimeCacheService;
        this.policeApiCrimeURL = policeApiCrimeURL;
        this.restClient = restClient;
    }

    @Override
    public List<Crime> getCrimesByLatitudeAndLongitudeAndRadius(String latitude, String longitude, String radius) {
        final double userLat = Double.parseDouble(latitude);
        final double userLng = Double.parseDouble(longitude);
        final double radiusKm = Double.parseDouble(radius) * GeoUtils.MILES_TO_KILOMETERS;
        List<Crime> crimes = fetchOrCacheCrimes(latitude, longitude);
        if (CollectionUtils.isEmpty(crimes)) {
            log.warn("No crime data available from both cache and API for coordinates: ({}, {})", latitude, longitude);
            return List.of();
        }
        return crimes.stream()
                .filter(crime -> {
                    if (crime.getLocation() == null) {
                        return false;
                    }
                    double crimeLat = Double.parseDouble(crime.getLocation().getLatitude());
                    double crimeLng = Double.parseDouble(crime.getLocation().getLongitude());
                    double distance = GeoUtils.calculateDistance(userLat, userLng, crimeLat, crimeLng);
                    return distance <= radiusKm;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public boolean reloadCrimeData(String latitude, String longitude) {
        List<Crime> crimesFromApi = fetchCrimesFromApi(latitude, longitude);
        boolean isReloaded = false;
        if (!CollectionUtils.isEmpty(crimesFromApi)) {
            crimeRepository.saveAll(crimesFromApi);
            isReloaded = true;
            log.info("Inserted {} new crime records.", crimesFromApi.size());
        } else {
            log.warn("No crime data fetched from the police API for coordinates: ({}, {})", latitude, longitude);
        }
        return isReloaded;
    }

    private List<Crime> fetchOrCacheCrimes(String latitude, String longitude) {
        List<Crime> crimes = crimeRepository.findByLocationLatitudeAndLocationLongitude(latitude, longitude);
        if (CollectionUtils.isEmpty(crimes)) {
            crimes = fetchCrimesFromApi(latitude, longitude);
        }
        if (!CollectionUtils.isEmpty(crimes)) {
            crimeCacheService.saveCrimesAsync(crimes);
        }
        return crimes;
    }

    private List<Crime> fetchCrimesFromApi(String latitude, String longitude) {
        String formattedUrl = String.format(policeApiCrimeURL, latitude, longitude);
        logger.info("Fetching crimes from URL: {}", formattedUrl);
        long startTime = System.currentTimeMillis();
        List<Crime> crimes = restClient.get()
                .uri(formattedUrl)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (_, res) -> {
                    logger.error("Client error fetching crimes: {}", res.getStatusCode());
                    throw new ApiClientException("Client error fetching crimes: " + res.getStatusCode());
                })
                .onStatus(HttpStatusCode::is5xxServerError, (_, res) -> {
                    logger.error("Server error fetching crimes: {}", res.getStatusCode());
                    throw new ApiServerException("Server error fetching crimes: " + res.getStatusCode());
                })
                .body(new ParameterizedTypeReference<>() {
                });
        long endTime = System.currentTimeMillis();
        logger.info("Fetched {} crimes in {} ms", Objects.requireNonNull(crimes).size(), endTime - startTime);
        return crimes;
    }

}
