package com.connect.session.infrastructure.controller;

import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/session")
public class SessionController {

    @DeleteMapping("/{sessionId}/invalidate")
    public void invalidateSession(@PathVariable Long sessionId){
        // TODO
    }

    @GetMapping("/all/me")
    public void getAllAuthenticatedUserSessions(Principal principal){
        // TODO
    }

}
