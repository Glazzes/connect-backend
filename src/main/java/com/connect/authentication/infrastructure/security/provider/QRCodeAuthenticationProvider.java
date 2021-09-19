package com.connect.authentication.infrastructure.security.provider;

import com.connect.authentication.infrastructure.security.token.QrCodeAuthenticationToken;
import com.connect.authentication.infrastructure.security.token.SuccessfulAuthenticationToken;
import com.connect.authentication.domain.entities.RedisQrLoginRequest;
import com.connect.authentication.infrastructure.security.contract.SecurityUserAdapter;
import com.connect.authentication.domain.exception.QrCodeRequestAuthenticationException;
import com.connect.authentication.domain.exception.QrCodeRequestNotFoundException;
import com.connect.authentication.domain.model.QrLoginRequest;
import com.connect.authentication.infrastructure.repository.RedisQrLoginRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;

@Slf4j
@RequiredArgsConstructor
public class QRCodeAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService detailsService;
    private final RedisQrLoginRequestRepository redisQrLoginRequestRepository;

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
            SecurityUserAdapter userDetails = (SecurityUserAdapter) detailsService
                    .loadUserByUsername(currentRequest.getUsername());

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
