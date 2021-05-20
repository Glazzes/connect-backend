package com.connect.services;

import com.connect.dtos.DeviceInfoDto;
import com.connect.entities.Session;
import com.connect.entities.User;
import com.connect.exceptions.applicationexceptions.SessionUserAssociationException;
import com.connect.models.utils.DeviceType;
import com.connect.repositories.SessionRepository;
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
public class SessionServiceTest {
    private SessionService sessionService;
    @Mock private UserService userService;
    @Mock private SessionRepository sessionRepository;

    private DeviceInfoDto deviceInfo;

    @BeforeEach
    void setUp(){
        sessionService = new SessionService(
                userService, sessionRepository
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
        User user = new User("Glaze", "Glaze@demo.com", "GlazePassowd");
        String refreshToken = "some-random-refresh-token";

        given(userService.findByUsername(anyString()))
                .willReturn(Optional.ofNullable(user));

        sessionService.saveSession(refreshToken, user.getUsername(), deviceInfo);

        verify(sessionRepository).save(any(Session.class));
    }

    @Test
    @DisplayName("Given an invalid username should throw exception")
    void givenAnInValidUsernameShouldThrowException(){
        String username = "NotGlaze";
        String refreshToken = "some-random-refresh-token";

        given(userService.findByUsername(anyString()))
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

        verify(sessionRepository, never()).save(any(Session.class));
    }

}