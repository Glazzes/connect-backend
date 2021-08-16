package com.connect.authentication.infrastructure.controller;

import com.connect.authentication.domain.model.QrLoginRequest;
import com.connect.authentication.application.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth/qr")
@RequiredArgsConstructor
public class QrCodeController {
    private final AuthenticationService authenticationService;

    @PostMapping(path = "/register")
    public ResponseEntity<?> registerQrCodeLoginRequest(@Valid @RequestBody QrLoginRequest qrLoginRequest){
        authenticationService.registerQrCodeRequest(qrLoginRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

}
