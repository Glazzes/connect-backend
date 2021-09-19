package com.connect.session.infrastructure.controller;

import com.connect.authentication.infrastructure.security.contract.SecurityUserAdapter;
import com.connect.session.application.dto.SessionDto;
import com.connect.session.application.service.PostgresSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
public class SessionController {
    private final PostgresSessionService postgresSessionService;

    @GetMapping("/me")
    public ResponseEntity<?> findAllAuthenticatedUserSessions(
            Authentication authentication,
            @CookieValue("RefreshToken") String currentSessionRefreshToken
    ){
        SecurityUserAdapter authenticatedUser = (SecurityUserAdapter) authentication.getPrincipal();
        List<SessionDto> allSessions = postgresSessionService.findAllSessionsByAuthenticatedUser(
                authenticatedUser.getUser(), currentSessionRefreshToken
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(allSessions);
    }

    @DeleteMapping("/{sessionId}/invalidate")
    public ResponseEntity<?> invalidateSession(@PathVariable String sessionId){
        postgresSessionService.invalidateSession(sessionId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

}
