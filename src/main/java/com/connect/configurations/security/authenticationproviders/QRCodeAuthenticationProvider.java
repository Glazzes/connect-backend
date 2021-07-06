package com.connect.configurations.security.authenticationproviders;

import com.connect.configurations.security.authenticationtokens.QrCodeAuthenticationToken;
import com.connect.configurations.security.authenticationtokens.SuccessfulAuthenticationToken;
import com.connect.entities.redis.RedisQrLoginRequest;
import com.connect.exceptions.securityexceptions.QrCodeRequestAuthenticationException;
import com.connect.exceptions.securityexceptions.QrCodeRequestNotFoundException;
import com.connect.models.QrLoginRequest;
import com.connect.repositories.redis.RedisQrLoginRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@Slf4j
public class QRCodeAuthenticationProvider implements AuthenticationProvider {
    private final RedisQrLoginRequestRepository redisQrLoginRequestRepository;
    private final UserDetailsService detailsService;

    public QRCodeAuthenticationProvider(
            RedisQrLoginRequestRepository redisQrLoginRequestRepository,
            UserDetailsService detailsService
    ){
        this.redisQrLoginRequestRepository = redisQrLoginRequestRepository;
        this.detailsService = detailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String issuedCode = (String) authentication.getPrincipal();
        RedisQrLoginRequest redisQrLoginRequest = redisQrLoginRequestRepository
                .findById(issuedCode)
                .orElseThrow(() -> {
                    String errorMessage = "Qr code authentication unsuccessful";
                    return new QrCodeRequestNotFoundException(errorMessage);
                });

        QrLoginRequest currentRequest = (QrLoginRequest) authentication.getCredentials();
        QrLoginRequest savedRequest = redisQrLoginRequest.getQrLoginRequest();

        if(currentRequest.equals(savedRequest)){
            UserDetails userDetails = detailsService.loadUserByUsername(currentRequest.getIssuedFor());
            SuccessfulAuthenticationToken successfulAuthenticationToken = new SuccessfulAuthenticationToken(
                    userDetails, userDetails.getPassword(), userDetails.getAuthorities()
            );
            successfulAuthenticationToken.setDetails(currentRequest.getDeviceInfo());

            return  successfulAuthenticationToken;
        }else {
            String errorMessage = "Qr code authentication unsuccessful";
            throw new QrCodeRequestAuthenticationException(errorMessage);
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(QrCodeAuthenticationToken.class);
    }
}
