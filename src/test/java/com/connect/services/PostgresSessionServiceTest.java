package com.connect.services;

import com.connect.session.application.dto.DeviceInfoDto;
import com.connect.session.application.service.PostgresSessionService;
import com.connect.session.domain.entities.PostgresSession;
import com.connect.user.domain.entities.PostgresUser;
import com.connect.shared.exception.application.SessionUserAssociationException;
import com.connect.shared.enums.DeviceType;
import com.connect.session.domain.repository.PostgresSessionRepository;
import com.connect.user.application.service.PostgresUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PostgresSessionServiceTest {
    private PostgresSessionService sessionService;
    @Mock private PostgresUserService postgresUserService;
    @Mock private PostgresSessionRepository postgresSessionRepository;

    private DeviceInfoDto deviceInfo;

    @BeforeEach
    void setUp(){
        sessionService = new PostgresSessionService(
                postgresUserService, postgresSessionRepository
        );

        deviceInfo = new DeviceInfoDto();
        deviceInfo.setType(DeviceType.MOBILE);
        deviceInfo.setDeviceOperatingSystem("Android");
        deviceInfo.setDeviceVersion("10");
        deviceInfo.setDeviceVersion("Samsung a12");
    }

    @Test
    @DisplayName("Given a valid username should save session")
    void givenAValidUsernameShouldSaveSession(){
        PostgresUser user = new PostgresUser("Glaze", "Glaze@demo.com", "GlazePassowd");
        String refreshToken = "some-random-refresh-token";

        given(postgresUserService.findByUsername(anyString()))
                .willReturn(Optional.ofNullable(user));

        sessionService.saveSession(refreshToken, user.getUsername(), deviceInfo);

        verify(postgresSessionRepository).save(any(PostgresSession.class));
    }

    @Test
    @DisplayName("Given an invalid username should throw exception")
    void givenAnInValidUsernameShouldThrowException(){
        String username = "NotGlaze";
        String refreshToken = "some-random-refresh-token";

        given(postgresUserService.findByUsername(anyString()))
                .willReturn(Optional.ofNullable(null));

        assertThatThrownBy(() -> {
            sessionService.saveSession(
                    refreshToken, username, deviceInfo
            );
        })
        .isInstanceOf(SessionUserAssociationException.class)
        .hasMessageContaining(String.format("""
         User with username %s does not exist therefore it can not be associated with
         session %s       
         """, username, refreshToken));

        verify(postgresSessionRepository, never()).save(any(PostgresSession.class));
    }

}
