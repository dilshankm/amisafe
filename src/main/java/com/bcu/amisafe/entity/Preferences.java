package com.bcu.amisafe.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Preferences {
    private int alertRadius;
    private boolean emailNotifications;
    private boolean pushNotifications;
}

