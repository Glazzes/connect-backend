package com.connect.authentication.application.service;

import com.connect.authentication.domain.entities.RedisQrLoginRequest;
import com.connect.user.domain.entities.PostgresUser;
import com.connect.shared.exception.application.QrScannedEventSendException;
import com.connect.authentication.domain.model.QrLoginRequest;
import com.connect.authentication.domain.model.QrScannedEvent;
import com.connect.authentication.infrastructure.repository.RedisQrLoginRequestRepository;
import com.connect.user.application.service.PostgresUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final RedisQrLoginRequestRepository redisQrLoginRequestRepository;

    public void registerQrCodeRequest(QrLoginRequest request){
        String qrId = String.format(
                "Qr-%s-%s",
                request.getMobileId(),
                request.getBrowserId()
        );

        RedisQrLoginRequest newQrRequest = new RedisQrLoginRequest(qrId, request);
        redisQrLoginRequestRepository.save(newQrRequest);
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
    Any kind of data is required to trigger the callback on js side
    otherwise no code will be executed by the event listener on js.
     */
    public void sendQrLoginEvent(SseEmitter emitter){
        try{
            emitter.send(
                SseEmitter.event()
                    .name("onQrLogin")
                    .data("Useless dummy data")
            );

            emitter.complete();
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
                            .data("Useless dummy data")
            );

            emitter.complete();
        }catch (IOException e){
            String errorMessage = "QrLogin event could not be sent to it's respective emitter";
            throw new QrScannedEventSendException(errorMessage, e);
        }
    }

}
