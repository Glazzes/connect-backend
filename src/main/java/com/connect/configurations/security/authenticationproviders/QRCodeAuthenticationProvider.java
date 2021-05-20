package com.connect.configurations.security.authenticationproviders;

import com.connect.configurations.security.authenticationtokens.QrCodeAuthenticationToken;
import com.connect.configurations.security.authenticationtokens.SuccessfulAuthenticationToken;
import com.connect.exceptions.applicationexceptions.QrCodeRequestParseException;
import com.connect.exceptions.securityexceptions.QrCodeRequestAuthenticationException;
import com.connect.exceptions.securityexceptions.QrCodeRequestNotFoundException;
import com.connect.models.QrLoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class QRCodeAuthenticationProvider implements AuthenticationProvider {
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserDetailsService detailsService;
    private final ObjectMapper mapper = new ObjectMapper();

    public QRCodeAuthenticationProvider(
            RedisTemplate<String, Object> redisTemplate,
            UserDetailsService detailsService
    ){
        this.redisTemplate = redisTemplate;
        this.detailsService = detailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String issuedCode = (String) authentication.getPrincipal();

        try{

            String loginInfo = Optional.ofNullable(
                    (String) redisTemplate.opsForValue()
                            .get(issuedCode)
            ).orElseThrow(() -> {
                String errorMessage = "Qr code authentication unsuccessful";
                return new QrCodeRequestNotFoundException(errorMessage);
            });

            QrLoginRequest currentRequest = (QrLoginRequest) authentication.getCredentials();
            QrLoginRequest savedRequest = mapper.readValue(loginInfo, QrLoginRequest.class);

            if(currentRequest.equals(savedRequest)){
                UserDetails userDetails = detailsService.loadUserByUsername(
                        currentRequest.getIssuedFor()
                );

                SuccessfulAuthenticationToken successfulAuthenticationToken = new SuccessfulAuthenticationToken(
                        userDetails, userDetails.getPassword(), userDetails.getAuthorities()
                );
                successfulAuthenticationToken.setDetails(currentRequest.getDeviceInfo());
                return  successfulAuthenticationToken;
            }else {
                String errorMessage = "Qr code authentication unsuccessful";
                throw new QrCodeRequestAuthenticationException(errorMessage);
            }
        }catch (QrCodeRequestNotFoundException e){
            e.printStackTrace();
            throw e;
        }catch (IOException e){
            e.printStackTrace();
            String errorMessage = "Qr code request parsing failed";
            throw new QrCodeRequestParseException(errorMessage, e);
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(QrCodeAuthenticationToken.class);
    }
}
