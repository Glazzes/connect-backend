package com.connect.session.application.dto;

import com.connect.session.domain.entities.embedable.DeviceInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SessionDto {
    private String id;
    private DeviceInfo deviceInfo;
    private boolean isCurrentSession;
}
