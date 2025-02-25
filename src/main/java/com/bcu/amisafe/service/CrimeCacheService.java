package com.bcu.amisafe.service;

import com.bcu.amisafe.entity.Crime;
import java.util.List;

public interface CrimeCacheService {

    boolean reloadCrimeData() ;
    void saveCrimesAsync(List<Crime> crimes);

}
