package com.connect.entities;

import com.connect.entities.embedables.DeviceInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "sessions")
public class Session {

    @Id
    private String refreshToken;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "is_not_blacklisted")
    private boolean isNotBlacklisted;

    @ManyToOne
    @JoinColumn(name = "fk_user_id", nullable = false, updatable = false)
    private User associatedUser;

    @Embedded
    private DeviceInfo deviceInfo;

    public Session(
            String refreshToken,
            boolean isActive,
            boolean isNotBlacklisted,
            User associatedUser,
            DeviceInfo deviceInfo
    ){
        this.refreshToken = refreshToken;
        this.isActive = isActive;
        this.isNotBlacklisted = isNotBlacklisted;
        this.associatedUser = associatedUser;
        this.deviceInfo = deviceInfo;
    }
}
