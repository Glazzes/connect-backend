package com.connect.configurations.security;

import com.connect.configurations.security.authenticationproviders.QRCodeAuthenticationProvider;
import com.connect.configurations.security.authenticationproviders.UsernamePasswordAuthenticationProvider;
import com.connect.configurations.security.filters.GlobalAuthenticationFilter;
import com.connect.configurations.security.filters.QrCodeAuthenticationFilter;
import com.connect.configurations.security.filters.UsernamePasswordAuthenticationFilter;
import com.connect.configurations.security.services.SessionAuthenticationService;
import com.connect.repositories.redis.RedisQrLoginRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.filter.CorsFilter;
import java.time.Duration;

import java.util.List;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final RedisQrLoginRequestRepository redisQrLoginRequestRepository;
    private final SessionAuthenticationService sessionAuthenticationService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        AuthenticationProvider usernamePasswordAuthenticationProvider = new UsernamePasswordAuthenticationProvider(
                passwordEncoderBean(),
                userDetailsService
        );

        AuthenticationProvider qrCodeAuthenticationProvider = new QRCodeAuthenticationProvider(
                redisQrLoginRequestRepository, userDetailsService
        );

        auth.authenticationProvider(qrCodeAuthenticationProvider)
            .authenticationProvider(usernamePasswordAuthenticationProvider)
            .userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().configurationSource(httpServletRequest -> {
            CorsConfiguration corsConfiguration = new CorsConfiguration();
            corsConfiguration.setAllowCredentials(true);
            corsConfiguration.setAllowedOrigins(List.of("http://localhost:8080", "http://localhost:19006"));
            corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE", "OPTIONS"));
            corsConfiguration.setMaxAge(Duration.ofMinutes(60));
            corsConfiguration.setAllowedHeaders(List.of("*"));

            return corsConfiguration;
        })
        .and()
        .csrf()
        .disable();

        http.authorizeRequests()
            .antMatchers( "/user").permitAll()
            .antMatchers(HttpMethod.POST, "/auth/qr/login").permitAll()
            .antMatchers(HttpMethod.GET, "/auth/sse/*/listen").permitAll()
            .antMatchers(HttpMethod.POST, "/auth/sse/*/qr-scan").permitAll()
            .antMatchers(HttpMethod.GET, "/ws/**").permitAll()
            .anyRequest()
            .authenticated();

        http.addFilterAfter(new UsernamePasswordAuthenticationFilter(
                authenticationManager(), sessionAuthenticationService
            ), CorsFilter.class)
            .addFilterAfter(new QrCodeAuthenticationFilter(
                    authenticationManager(), sessionAuthenticationService
            ), UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(new GlobalAuthenticationFilter(
                    sessionAuthenticationService
            ), QrCodeAuthenticationFilter.class);

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public PasswordEncoder passwordEncoderBean(){
        return new BCryptPasswordEncoder(10);
    }

}
