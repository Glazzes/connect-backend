package com.connect.session.application.service;

import com.connect.session.application.dto.DeviceInfoDto;
import com.connect.session.application.mapper.DeviceInfoMapper;
import com.connect.session.domain.entities.embedable.DeviceInfo;
import com.connect.session.domain.entities.PostgresSession;
import com.connect.shared.exception.application.SessionUserAssociationException;
import com.connect.session.domain.repository.PostgresSessionRepository;
import com.connect.user.application.service.PostgresUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PostgresSessionService {
    private final PostgresUserService postgresUserService;
    private final PostgresSessionRepository postgresSessionRepository;

    @Transactional
    public void saveSession(String refreshToken, String username, DeviceInfoDto deviceInfo){
        DeviceInfo embeddableDeviceInfo = DeviceInfoMapper.INSTANCE
                .deviceInfoDtoToDeviceInfo(deviceInfo);

        postgresUserService.findByUsername(username)
            .ifPresentOrElse(user -> {
                PostgresSession newSession = new PostgresSession(
                        refreshToken,
                        true,
                        true,
                        user,
                        embeddableDeviceInfo
                );

                postgresSessionRepository.save(newSession);
            },() -> {
                String errorMessage = String.format("""
                User with username %s does not exist therefore it can not be associated with
                session %s
                """,username, refreshToken);
                throw new SessionUserAssociationException(errorMessage);
            });
    }

}
