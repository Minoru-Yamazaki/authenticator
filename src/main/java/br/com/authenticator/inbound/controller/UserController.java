package br.com.authenticator.inbound.controller;

import br.com.authenticator.core.application.usecase.GetCurrentUser;
import br.com.authenticator.inbound.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final GetCurrentUser getCurrentUser;

    @GetMapping("/user")
    public UserResponse me(@AuthenticationPrincipal UUID userId) {
        return getCurrentUser.execute(userId);
    }

}
