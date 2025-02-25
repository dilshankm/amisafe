package com.bcu.amisafe.service;

import com.bcu.amisafe.entity.Crime;
import com.bcu.amisafe.repository.CrimeRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrimeCacheService {

    private final CrimeRepository crimeRepository;

    public CrimeCacheService(CrimeRepository crimeRepository) {
        this.crimeRepository = crimeRepository;
    }

    @Async
    public void saveCrimesAsync(List<Crime> crimes) {
        crimeRepository.saveAll(crimes);
    }
}
