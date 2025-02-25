package com.bcu.amisafe.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "crime_data_metadata")
public class CrimeDataMetadata {

    @Id
    private String id;

    @JsonProperty("date")
    @Field("date")
    private String lastUpdated;

}
