package com.connect.authentication.application.service;

import com.connect.authentication.domain.entities.RedisQrLoginRequest;
import com.connect.user.domain.entities.PostgresUser;
import com.connect.shared.exception.application.QrScannedEventSendException;
import com.connect.authentication.domain.model.QrLoginRequest;
import com.connect.authentication.domain.model.QrScannedEvent;
import com.connect.authentication.domain.repository.RedisQrLoginRequestRepository;
import com.connect.user.application.service.PostgresUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PostgresUserService postgresUserService;
    private final RedisQrLoginRequestRepository redisQrLoginRequestRepository;

    public Optional<PostgresUser> getAuthenticatedUser(String username){
        return postgresUserService.findByUsername(username);
    }

    public void registerQrCodeRequest(QrLoginRequest request){
        String qrId = String.format(
                "Qr-%s-%s",
                request.getMobileSignature(),
                request.getWebSignature()
        );

        RedisQrLoginRequest newQrRequest = new RedisQrLoginRequest(qrId, request);
        redisQrLoginRequestRepository.save(newQrRequest);

        /*
        try{
            String qrId = String.format(
                    "Qr-%s-%s",
                    request.getMobileSignature(),
                    request.getWebSignature()
            );
            String stringifyQrCodeRequest = mapper.writeValueAsString(request);

            redisTemplate.opsForValue()
                .set(
                     qrId,
                     stringifyQrCodeRequest,
                     10L,
                     TimeUnit.MINUTES
                );
        }catch (IOException e){
            e.printStackTrace();
            throw new QrCodeRequestParseException("QrCodeRequest conversion to json failed", e);
        }
         */
    }

    public void sendQrScannedEventToBrowser(SseEmitter emitter, QrScannedEvent eventData){
        try{
            emitter.send(
               SseEmitter.event()
                   .name("onQrScanned")
                   .data(eventData)
            );
        }catch (IOException e){
            String errorMessage = "Qr data could not get sent to the the browser";
            throw new QrScannedEventSendException(errorMessage, e);
        }
    }

    /*
    Some data needs to be send otherwise the sse callback listener on Js will not be triggered
     */
    public void sendQrLoginEvent(SseEmitter emitter){
        try{
            emitter.send(
                SseEmitter.event()
                    .name("onQrLogin")
                    .data("Useless dummy data")
            );
        }catch (IOException e){
            e.printStackTrace();
            String errorMessage = "QrLogin event could not be sent to it's respective emitter";
            throw new QrScannedEventSendException(errorMessage, e);
        }
    }

    public void sendQrCancelEvent(SseEmitter emitter){
        try{
            emitter.send(
                    SseEmitter.event()
                            .name("onQrCancel")
                            .data("Placeholder data")
            );
        }catch (IOException e){
            e.printStackTrace();
            String errorMessage = "QrLogin event could not be sent to it's respective emitter";
            throw new QrScannedEventSendException(errorMessage, e);
        }
    }

}
