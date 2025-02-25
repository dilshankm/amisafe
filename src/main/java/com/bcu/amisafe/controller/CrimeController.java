package com.bcu.amisafe.controller;

import com.bcu.amisafe.constants.Constants;
import com.bcu.amisafe.entity.Crime;
import com.bcu.amisafe.exception.ErrorResponse;
import com.bcu.amisafe.service.CrimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/crimes")
@RequiredArgsConstructor
public class CrimeController {

    private final CrimeService crimeService;

    @PostMapping("/reload")
    public ResponseEntity<String> reloadCrimeData(@RequestParam String latitude,
                                                  @RequestParam String longitude,
                                                  @RequestParam String date) {
        crimeService.reloadCrimeData(latitude, longitude, date);
        return ResponseEntity.ok(Constants.CRIME_DATA_RELOAD_SUCCESS);
    }

    @GetMapping("/nearby")
    public ResponseEntity<?> getCrimesByLatitudeAndLongitudeAndRadius(
            @RequestParam String latitude,
            @RequestParam String longitude,
            @RequestParam String radius) {
        List<Crime> crimes = crimeService.getCrimesByLatitudeAndLongitudeAndRadius(latitude, longitude, radius);
        if (CollectionUtils.isEmpty(crimes)) {
            ErrorResponse error = new ErrorResponse(Constants.ERROR, Constants.NO_CRIME_DATA);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        return ResponseEntity.ok(crimes);
    }
}
