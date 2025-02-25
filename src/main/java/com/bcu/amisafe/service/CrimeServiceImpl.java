package com.bcu.amisafe.service;

import com.bcu.amisafe.entity.Crime;
import com.bcu.amisafe.repository.CrimeRepository;
import com.bcu.amisafe.utils.GeoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrimeServiceImpl implements CrimeService {

    private final CrimeRepository crimeRepository;
    private final CrimeCacheService crimeCacheService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${police.api.url}")
    private String policeApiUrlTemplate;

    /**
     * Flushes old crime data and reloads fresh data from the police API.
     *
     * @param latitude  the latitude coordinate for the query
     * @param longitude the longitude coordinate for the query
     * @param date      the date (format: YYYY-MM) for the police data
     */
    @Transactional
    @Override
    public void reloadCrimeData(String latitude, String longitude, String date) {
        // Fetch new crime data from the API first
        List<Crime> crimesFromApi = fetchCrimesFromApi(latitude, longitude, date);
        if (!CollectionUtils.isEmpty(crimesFromApi)) {
            // Delete old records and save new ones
            crimeRepository.deleteAll();
            crimeRepository.saveAll(crimesFromApi);
            log.info("Inserted {} new crime records.", crimesFromApi.size());
        } else {
            log.warn("No crime data fetched from the police API for coordinates: ({}, {})", latitude, longitude);
        }
    }

    /**
     * Retrieves crimes by latitude, longitude, and radius (in miles).
     *
     * @param latitude  User's latitude as a String
     * @param longitude User's longitude as a String
     * @param radius    Radius in miles as a String
     * @return a list of Crime objects within the radius
     */
    @Override
    public List<Crime> getCrimesByLatitudeAndLongitudeAndRadius(String latitude, String longitude, String radius) {
        final double userLat = Double.parseDouble(latitude);
        final double userLng = Double.parseDouble(longitude);
        final double radiusKm = Double.parseDouble(radius) * GeoUtils.MILES_TO_KILOMETERS;
        // Attempt to get cached crimes; if not available, fetch from API
        List<Crime> crimes = fetchOrCacheCrimes(latitude, longitude);
        if (CollectionUtils.isEmpty(crimes)) {
            log.warn("No crime data available from both cache and API for coordinates: ({}, {})", latitude, longitude);
            return List.of();
        }
        // Filter the crimes based on the Haversine formula
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

    /**
     * Attempts to retrieve crimes from the database. If none exist, fetches from the API.
     * Asynchronously updates the cache if data is found.
     */
    private List<Crime> fetchOrCacheCrimes(String latitude, String longitude) {
        List<Crime> crimes = crimeRepository.findByLocationLatitudeAndLocationLongitude(latitude, longitude);
        if (CollectionUtils.isEmpty(crimes)) {
            crimes = fetchCrimesFromApi(latitude, longitude, "2024-01"); // Example date; adjust as needed
        }
        if (!CollectionUtils.isEmpty(crimes)) {
            crimeCacheService.saveCrimesAsync(crimes);
        }
        return crimes;
    }

    /**
     * Fetches crime data from the police API based on latitude, longitude, and date.
     */
    private List<Crime> fetchCrimesFromApi(String latitude, String longitude, String date) {
        String url = String.format(policeApiUrlTemplate, latitude, longitude, date);
        Crime[] fetchedCrimes = restTemplate.getForObject(url, Crime[].class);
        return Arrays.asList(Objects.requireNonNull(fetchedCrimes));
    }
}
