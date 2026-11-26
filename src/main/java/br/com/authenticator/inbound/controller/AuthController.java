package br.com.authenticator.inbound.controller;

import br.com.authenticator.core.application.usecase.Login;
import br.com.authenticator.core.application.usecase.Logout;
import br.com.authenticator.core.application.usecase.RefreshToken;
import br.com.authenticator.core.application.usecase.RegisterUser;
import br.com.authenticator.inbound.dto.request.LoginRequest;
import br.com.authenticator.inbound.dto.request.LogoutRequest;
import br.com.authenticator.inbound.dto.request.RefreshRequest;
import br.com.authenticator.inbound.dto.request.RegisterRequest;
import br.com.authenticator.inbound.dto.response.TokenResponse;
import br.com.authenticator.inbound.dto.response.UserResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final RegisterUser registerUser;
    private final Login login;
    private final RefreshToken refreshToken;
    private final Logout logout;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterRequest request) {
        return registerUser.execute(request);
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        return login.execute(request);
    }

    @PostMapping("/refresh")
    public TokenResponse refresh(@Valid @RequestBody RefreshRequest request) {
        return refreshToken.execute(request);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@Valid @RequestBody LogoutRequest request) {
        logout.execute(request);
    }

}
