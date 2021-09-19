package com.connect.authentication.infrastructure.security.filter;

import com.connect.authentication.infrastructure.security.token.SuccessfulAuthenticationToken;
import com.connect.authentication.infrastructure.security.service.SessionAuthenticationService;
import com.connect.authentication.infrastructure.security.cookie.CookieUtil;
import com.connect.authentication.infrastructure.security.cookie.CookieType;
import com.connect.session.domain.entities.RedisSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RequiredArgsConstructor
public class AuthenticationVerifierFilter extends OncePerRequestFilter {
    private final SessionAuthenticationService sessionAuthenticationService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        List<RequestMatcher> pathsThatMustNotBeFiltered = List.of(
                new AntPathRequestMatcher("/user", "POST"),
                new AntPathRequestMatcher("/auth/sse/*/listen", "GET"),
                new AntPathRequestMatcher("/auth/login", "POST"),
                new AntPathRequestMatcher("/auth/qr/login", "POST")
        );

        return pathsThatMustNotBeFiltered.stream()
                .anyMatch(matcher -> matcher.matches(request));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getCookies() == null){
            filterChain.doFilter(request, response);
            log.error("""
            Authentication error on GlobalAuthenticationFilter.doFilterInternal, no cookies were present
            with this request
            """);

            return;
        }

        Cookie refreshToken = CookieUtil.getCookieByName(
                CookieType.REFRESH_TOKEN.getName(), request.getCookies()
        );

        RedisSession currentSession = sessionAuthenticationService.
                findRedisSessionByRefreshToken(refreshToken.getValue());

        System.out.println(refreshToken);

        log.info(currentSession.toString());

        if(!currentSession.isNotBlacklisted()){
            filterChain.doFilter(request, response);
            log.error("""
            Authentication error on GlobalAuthenticationFilter.doFilterInternal, this session has
            been blacklisted
            """);

            return;
        }

        /*
        if(!currentSession.getAccessToken().equals(accessToken.getValue())){
            filterChain.doFilter(request, response);
            log.error("""
            Authentication error on GlobalAuthenticationFilter.doFilterInternal, Accestoken in 
            saved request differs from the one store in the http cookie
            """);
            return;
        }
         */

        UserDetails details = sessionAuthenticationService.getUserDetailsByRefreshToken(refreshToken.getValue());
        if(!currentSession.getOwnerUsername().equals(details.getUsername())){
            filterChain.doFilter(request, response);
            log.error("""
            Authentication error on GlobalAuthenticationFilter.doFilterInternal, saved request username
            and details username differ""");
            return;
        }

        SuccessfulAuthenticationToken successfulAuthentication = new SuccessfulAuthenticationToken(
                details,
                details.getPassword(),
                details.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(successfulAuthentication);

        CookieUtil.restartMaxAge(CookieType.REFRESH_TOKEN.getDuration(), refreshToken);
        response.addCookie(refreshToken);
        filterChain.doFilter(request, response);

        log.info("Authentication successful on AuthenticationVerifierFilter");
    }

}
