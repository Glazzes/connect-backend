package com.connect.services;

import com.connect.dtos.DeviceInfoDto;
import com.connect.dtos.mappers.DeviceInfoMapper;
import com.connect.entities.embedables.DeviceInfo;
import com.connect.entities.Session;
import com.connect.exceptions.applicationexceptions.SessionUserAssociationException;
import com.connect.repositories.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final UserService userService;
    private final SessionRepository sessionRepository;

    @Transactional
    public void saveSession(String refreshToken, String username, DeviceInfoDto deviceInfo){
        DeviceInfo embeddableDeviceInfo = DeviceInfoMapper.INSTANCE
                .deviceInfoDtoToDeviceInfo(deviceInfo);

        userService.findByUsername(username)
            .ifPresentOrElse(user -> {
                Session newSession = new Session(
                        refreshToken,
                        true,
                        true,
                        user,
                        embeddableDeviceInfo
                );

                sessionRepository.save(newSession);
            },() -> {
                String errorMessage = String.format("""
                User with username %s does not exist therefore it can not be associated with
                session %s
                """,username, refreshToken);
                throw new SessionUserAssociationException(errorMessage);
            });
    }



}
