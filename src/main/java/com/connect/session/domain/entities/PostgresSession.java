package com.connect.session.domain.entities;

import com.connect.session.domain.entities.embedable.DeviceInfo;
import com.connect.user.domain.entities.PostgresUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "sessions")
public class PostgresSession {

    @Id
    private String refreshToken;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "is_not_blacklisted")
    private boolean isNotBlacklisted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user_id", nullable = false, updatable = false)
    private PostgresUser associatedUser;

    @Embedded
    private DeviceInfo deviceInfo;

    public PostgresSession(
            String refreshToken,
            boolean isActive,
            boolean isNotBlacklisted,
            PostgresUser associatedUser,
            DeviceInfo deviceInfo
    ){
        this.refreshToken = refreshToken;
        this.isActive = isActive;
        this.isNotBlacklisted = isNotBlacklisted;
        this.associatedUser = associatedUser;
        this.deviceInfo = deviceInfo;
    }
}
