package com.connect.configurations.security.filters;

import com.connect.configurations.security.authenticationtokens.SuccessfulAuthenticationToken;
import com.connect.configurations.security.services.SecurityAuthenticationService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class GlobalAuthenticationFilter extends OncePerRequestFilter {
    private final SecurityAuthenticationService securityAuthenticationService;

    public GlobalAuthenticationFilter(SecurityAuthenticationService securityAuthenticationService){
        this.securityAuthenticationService = securityAuthenticationService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        List<RequestMatcher> pathsMustNotBeFiltered = List.of(
                new AntPathRequestMatcher("/user", "POST"),
                new AntPathRequestMatcher("/auth/sse/*/listen", "GET"),
                new AntPathRequestMatcher("/auth/sse/*/qr-scan", "POST")
        );

        return pathsMustNotBeFiltered.stream()
                .anyMatch(matcher -> matcher.matches(request));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie refreshTokenCookie = request.getCookies()[0];

        if(refreshTokenCookie != null){
            String refreshToken = refreshTokenCookie.getValue();
            UserDetails details = securityAuthenticationService.returnUserDetailsByRefreshToken(refreshToken);

            SuccessfulAuthenticationToken successfulAuthentication = new SuccessfulAuthenticationToken(
                    details,
                    details.getPassword(),
                    details.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(successfulAuthentication);
            filterChain.doFilter(request, response);
        }
    }

}
