package com.bcu.amisafe.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "crime_data")
public class Crime {

    @Id
    @JsonProperty("id")
    private String crimeId;

    private String category;

    @JsonProperty("persistent_id")
    private String persistentId;

    @JsonProperty("location_subtype")
    private String locationSubtype;

    private String context;

    private String month;

    @JsonProperty("location_type")
    private String locationType;

    @JsonProperty("outcome_status")
    private OutcomeStatus outcomeStatus;

    private Location location;
}
