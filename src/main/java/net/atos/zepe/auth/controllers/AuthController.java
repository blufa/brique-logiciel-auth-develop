package net.atos.zepe.auth.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.atos.zepe.auth.models.User;
import net.atos.zepe.auth.payload.request.KeycloakLoginRequest;
import net.atos.zepe.auth.payload.request.ResetPasswordDto;
import net.atos.zepe.auth.services.AccountService;
import net.atos.zepe.auth.services.KeycloakService;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Authentication APIs")
public class AuthController {

    private final AccountService accountService;
    private final KeycloakService keycloakService;

    public AuthController(AccountService accountService, KeycloakService keycloakService) {
        this.accountService = accountService;
        this.keycloakService = keycloakService;
    }

    @PostMapping(value = "/login")
    public AccessTokenResponse loginToAccount(@Valid @RequestBody KeycloakLoginRequest request) {

        return accountService.login(request.getUsername(), request.getPassword());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/signup", consumes = "application/json", produces = "application/json")
    public User registerUser(@Valid @RequestBody User user) {
        return accountService.createUser(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/reset-password", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordDto dto) {
        User user = accountService.readByUserEmailAddress(dto.getEmail());
        keycloakService.resetPassword(user.getUserKeycloakId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
