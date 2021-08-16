package com.connect.authentication.infrastructure.security.filters;

import com.connect.authentication.infrastructure.security.authenticationtokens.SuccessfulAuthenticationToken;
import com.connect.authentication.infrastructure.security.services.SessionAuthenticationService;
import com.connect.authentication.infrastructure.security.utils.cookie.CookieUtil;
import com.connect.authentication.infrastructure.security.utils.cookie.CookieType;
import com.connect.session.domain.entities.RedisSession;
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
    private final SessionAuthenticationService sessionAuthenticationService;

    public GlobalAuthenticationFilter(SessionAuthenticationService sessionAuthenticationService){
        this.sessionAuthenticationService = sessionAuthenticationService;
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
        if(request.getCookies() == null){
            filterChain.doFilter(request, response);
            return;
        }

        if(request.getCookies().length < 2){
            filterChain.doFilter(request, response);
            return;
        }

        Cookie refreshToken = CookieUtil.getCookieByName(
                CookieType.REFRESH_TOKEN.getName(), request.getCookies()
        );

        Cookie authorizationToken = CookieUtil.getCookieByName(
                CookieType.AUTHORIZATION_TOKEN.getName(), request.getCookies()
        );

        RedisSession currentSession = sessionAuthenticationService.
                findRedisSessionByRefreshToken(refreshToken.getValue());

        if(!currentSession.isValid()){
            filterChain.doFilter(request, response);
            return;
        }

        if(!currentSession.isNotBlacklisted()){
            filterChain.doFilter(request, response);
            return;
        }

        if(!currentSession.getAssociatedAuthenticationToken().equals(authorizationToken.getValue())){
            filterChain.doFilter(request, response);
            return;
        }

        UserDetails details = sessionAuthenticationService.getUserDetailsByRefreshToken(refreshToken.getValue());
        if(currentSession.getOwner().equals(details.getUsername())){
            filterChain.doFilter(request, response);
            return;
        }

        SuccessfulAuthenticationToken successfulAuthentication = new SuccessfulAuthenticationToken(
                details,
                details.getPassword(),
                details.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(successfulAuthentication);

        refreshToken = CookieUtil.restartCookieMaxAgeForType(
                CookieType.REFRESH_TOKEN, refreshToken
        );

        response.addCookie(refreshToken);
        filterChain.doFilter(request, response);
    }

}
