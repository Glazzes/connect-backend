package com.connect.services;

import com.connect.session.domain.model.DeviceInfoModel;
import com.connect.session.application.service.PostgresSessionService;
import com.connect.session.domain.entities.PostgresSession;
import com.connect.user.domain.entities.PostgresUser;
import com.connect.authentication.domain.exception.SessionUserAssociationException;
import com.connect.shared.enums.DeviceType;
import com.connect.session.infrastructure.repository.PostgresSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PostgresSessionServiceTest {
    private DeviceInfoModel deviceInfo;
    private PostgresSessionService sessionService;
    private PostgresUser authenticatedUser;

    @Mock private PostgresSessionRepository postgresSessionRepository;

    @BeforeEach
    void setUp(){
        sessionService = new PostgresSessionService(postgresSessionRepository);
        authenticatedUser = new PostgresUser("Glaze", "Glaze@demo.com", "GlazePassowd");

        deviceInfo = new DeviceInfoModel();
        deviceInfo.setType(DeviceType.MOBILE_APP);
        deviceInfo.setAppDetails("Android Firefox v91.01");
        deviceInfo.setDeviceDetails("Samsung galaxy s7");
        deviceInfo.setIpAddress("127.0.01");
    }

    @Test
    @DisplayName("Given a valid username should save session")
    void givenAValidUsernameShouldSaveSession(){
        String refreshToken = "some-random-refresh-token";
        sessionService.save(refreshToken, authenticatedUser, deviceInfo);
        verify(postgresSessionRepository).save(any(PostgresSession.class));
    }

}
