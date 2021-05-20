package com.connect.entities;

import com.connect.entities.embedables.EmbeddableDeviceInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "associated_refresh_token", nullable = false, updatable = false)
    private String associatedRefreshToken;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "is_not_blacklisted")
    private boolean isNotBlacklisted;

    @ManyToOne
    @JoinColumn(name = "fk_user_id", nullable = false, updatable = false)
    private User associatedUser;

    @Embedded
    private EmbeddableDeviceInfo deviceInfo;

    public Session(
            String associatedRefreshToken,
            boolean isActive,
            boolean isNotBlacklisted,
            User associatedUser,
            EmbeddableDeviceInfo deviceInfo
    ){
        this.associatedRefreshToken = associatedRefreshToken;
        this.isActive = isActive;
        this.isNotBlacklisted = isNotBlacklisted;
        this.associatedUser = associatedUser;
        this.deviceInfo = deviceInfo;
    }
}
