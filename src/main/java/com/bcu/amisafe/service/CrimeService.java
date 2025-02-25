package com.bcu.amisafe.service;

import com.bcu.amisafe.entity.Crime;
import java.util.List;

public interface CrimeService {

    void reloadCrimeData(String latitude, String longitude, String date);

    List<Crime> getCrimesByLatitudeAndLongitudeAndRadius(String latitude, String longitude, String radius);

}
