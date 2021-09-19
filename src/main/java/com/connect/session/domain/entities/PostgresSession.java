package com.connect.session.domain.entities;

import com.connect.session.domain.entities.embedable.DeviceInfo;
import com.connect.user.domain.entities.PostgresUser;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "sessions")
public class PostgresSession {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(
            name = "uuid",
            strategy = "org.hibernate.id.UUIDHexGenerator"
    )
    private String id;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user_id", nullable = false, updatable = false)
    private PostgresUser owner;

    @Embedded
    private DeviceInfo deviceInfo;

    public PostgresSession(
            String refreshToken,
            PostgresUser owner,
            DeviceInfo deviceInfo
    ){
        this.refreshToken = refreshToken;
        this.owner = owner;
        this.deviceInfo = deviceInfo;
    }
}
