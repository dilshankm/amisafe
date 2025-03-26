    package com.bcu.amisafe.controller;

    import com.bcu.amisafe.constants.Constants;
    import com.bcu.amisafe.entity.Crime;
    import com.bcu.amisafe.exception.ErrorResponse;
    import com.bcu.amisafe.service.CrimeCacheService;
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
        private final CrimeCacheService crimeCacheService;

        @PostMapping("/reload")
        public ResponseEntity<String> reloadCrimeData() {
            crimeCacheService.reloadCrimeData();
            return ResponseEntity.ok(Constants.CRIME_DATA_RELOAD_SUCCESS);
        }

        @GetMapping("/nearby")
        public ResponseEntity<?> getCrimesByLocation(@RequestParam String latitude, @RequestParam String longitude, @RequestParam String radius) {
            List<Crime> crimes = crimeService.getCrimesByLatitudeAndLongitudeAndRadius(latitude, longitude, radius);
            if (!CollectionUtils.isEmpty(crimes)) {
                return ResponseEntity.ok(crimes);
            }
            return crimeService.reloadCrimeData(latitude, longitude) ? ResponseEntity.ok(crimeService.getCrimesByLatitudeAndLongitudeAndRadius(latitude, longitude, radius)) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(Constants.ERROR, Constants.NO_CRIME_DATA));
        }
    }
