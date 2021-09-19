package com.connect.session.application.service;

import com.connect.session.domain.model.DeviceInfoModel;
import com.connect.session.application.dto.SessionDto;
import com.connect.session.application.mapper.DeviceInfoMapper;
import com.connect.session.domain.entities.embedable.DeviceInfo;
import com.connect.session.domain.entities.PostgresSession;
import com.connect.session.infrastructure.repository.PostgresSessionRepository;
import com.connect.user.domain.entities.PostgresUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostgresSessionService {
    private final PostgresSessionRepository postgresSessionRepository;

    public PostgresSession save(String refreshToken, PostgresUser authenticatedUser, DeviceInfoModel deviceInfo){
        DeviceInfo embeddableDeviceInfo = DeviceInfoMapper.INSTANCE
                .deviceInfoModelToDeviceInfo(deviceInfo);

        PostgresSession newSession = new PostgresSession(
                refreshToken,
                authenticatedUser,
                embeddableDeviceInfo
        );

        return postgresSessionRepository.save(newSession);
    }

    public List<SessionDto> findAllSessionsByAuthenticatedUser(
            PostgresUser authenticatedUser,
            String currentSessionRefreshToken
    ){
        return postgresSessionRepository.findAllByOwner(authenticatedUser)
                .stream()
                .map(session -> new SessionDto(
                            session.getId(),
                            session.getDeviceInfo(),
                            session.getRefreshToken().equals(currentSessionRefreshToken)
                            )
                )
                .toList();
    }

    public void invalidateSession(String sessionId){
        postgresSessionRepository.deleteById(sessionId);
    }

}
