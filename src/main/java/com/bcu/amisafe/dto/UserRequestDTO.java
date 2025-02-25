package com.bcu.amisafe.dto;

import com.bcu.amisafe.entity.Preferences;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
    private String email;
    private String name;
    private String mobile;
    private LocalDate dob;
    private Preferences preferences;
}
