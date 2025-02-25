package com.bcu.amisafe.service;

import com.bcu.amisafe.entity.Crime;
import java.util.List;

public interface CrimeService {

    List<Crime> getCrimesByLatitudeAndLongitudeAndRadius(String latitude, String longitude, String radius);
    boolean reloadCrimeData(String latitude, String longitude);

}
