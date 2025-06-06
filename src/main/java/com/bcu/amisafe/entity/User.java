package com.bcu.amisafe.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class User {
    @Id
    @JsonProperty("id")
    private String id;
    private String email;
    private String name;
    private String mobile;
    private Location currentLocation;
    private LocalDate dob;
    private Preferences preferences;
}

