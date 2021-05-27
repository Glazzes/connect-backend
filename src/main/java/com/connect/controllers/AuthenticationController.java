package com.connect.controllers;

import com.connect.dtos.UserDto;
import com.connect.dtos.mappers.UserMapper;
import com.connect.models.QrLoginRequest;
import com.connect.models.QrScannedEvent;
import com.connect.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @GetMapping(path = "/sse/{browserId}/listen", produces = "text/event-stream")
    public SseEmitter eventEmitterForQrLogin(@PathVariable String browserId){
        SseEmitter eventEmitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(browserId, eventEmitter);
        eventEmitter.onCompletion(() -> emitters.remove(browserId));
        return eventEmitter;
    }

    @PostMapping(path = "/qr/register")
    public ResponseEntity<?> registerQrCode(@Valid @RequestBody QrLoginRequest qrLoginRequest){
        authenticationService.registerQrCodeRequest(qrLoginRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PostMapping("/sse/{emitterId}/qr-scan")
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

    @PostMapping("/sse/{emitterId}/qr-login")
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

    @PostMapping("/sse/{emitterId}/qr-cancel")
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

    @GetMapping(path = "/me")
    private ResponseEntity<UserDto> getLoggedInUser(Principal principal){
        return authenticationService.getAuthenticatedUser(principal)
               .map(user -> {
                   UserDto loggedUser = UserMapper.INSTANCE.userToUserDto(user);
                   return ResponseEntity.status(HttpStatus.OK)
                           .body(loggedUser);
               })
               .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
