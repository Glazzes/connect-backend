package com.connect.authentication.infrastructure.controller;

import com.connect.authentication.domain.model.QrScannedEvent;
import com.connect.authentication.application.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
public class SSEAuthenticationController {
    private final AuthenticationService authenticationService;
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @GetMapping(path = "/auth/sse/{browserId}/listen", produces = "text/event-stream")
    public SseEmitter eventEmitterForQrLogin(@PathVariable String browserId){
        SseEmitter eventEmitter = new SseEmitter(Long.MAX_VALUE);

        Runnable callback = () -> emitters.remove(browserId);
        eventEmitter.onCompletion(callback);
        eventEmitter.onTimeout(callback);

        emitters.put(browserId, eventEmitter);
        return eventEmitter;
    }

    @PostMapping("/auth/sse/{emitterId}/qr-scan")
    public ResponseEntity<?> sentUserInfoToBrowserSseEmitter(
            @PathVariable String emitterId,
            @Valid @RequestBody QrScannedEvent qrScannedEvent
    ){
        SseEmitter eventEmitter = emitters.get(emitterId);
         return Optional.ofNullable(eventEmitter)
                 .map(emitter -> {
                     authenticationService.sendQrScannedEventToBrowser(emitter, qrScannedEvent);
                     return ResponseEntity.status(HttpStatus.NO_CONTENT)
                             .build();
                 })
                 .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/auth/sse/{emitterId}/qr-login")
    public ResponseEntity<?> sentQrLoginEvent(@PathVariable String emitterId){
        SseEmitter eventEmitter = emitters.get(emitterId);
        return Optional.ofNullable(eventEmitter)
                .map(emitter -> {
                    authenticationService.sendQrLoginEvent(emitter);
                    return ResponseEntity.status(HttpStatus.NO_CONTENT)
                            .build();
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/auth/sse/{emitterId}/qr-cancel")
    public ResponseEntity<?> sendQrCancelEvent(@PathVariable String emitterId){
        SseEmitter eventEmitter = emitters.get(emitterId);
        return Optional.ofNullable(eventEmitter)
                .map(emitter -> {
                    authenticationService.sendQrCancelEvent(emitter);
                    return ResponseEntity.status(HttpStatus.NO_CONTENT)
                            .build();
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
