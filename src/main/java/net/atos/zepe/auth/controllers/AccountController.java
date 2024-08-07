package net.atos.zepe.auth.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.atos.zepe.auth.services.KeycloakService;
import net.atos.zepe.auth.services.impl.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import net.atos.zepe.auth.models.User;
import net.atos.zepe.auth.payload.request.UpdatePwdRequest;
import net.atos.zepe.auth.models.UserDetailsImpl;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/account")
@Tag(name = "Account Management", description = "Manage users accounts APIs")
public class AccountController {

    private final AccountServiceImpl accountService;
    private final KeycloakService keycloakService;

    @Value("${keycloak.client}")
    private String keycloakClientId;

    public AccountController(AccountServiceImpl accountService, KeycloakService keycloakService) {
        this.accountService = accountService;
        this.keycloakService = keycloakService;
    }

    @GetMapping("/me")
    public User getLoggedUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return accountService.readUserByUserId(userDetails.getId());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "user/{realm}/{userKeycloakId}", produces = "application/json")
    @PreAuthorize("hasAuthority('ADMIN_ENTREPRISE')")
    public User getAccountByUserId(@PathVariable("realm") String userKeycloakRealm, @PathVariable("userKeycloakId") String userKeycloakId) {
		return accountService.readUserByKeycloakRealmAndUserKeycloakId(userKeycloakRealm, userKeycloakId);
	}

    @ResponseStatus(HttpStatus.CREATED)
    //@PreAuthorize("hasAuthority('ADMIN_ENTREPRISE')")
    @PostMapping(value = "/realms/{realm}/clients/{clientId}/signup", consumes = "application/json", produces = "application/json")
    public User createUser(@Valid @RequestBody User user) {
        return accountService.createUser(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/user", consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasAuthority('ADMIN_ENTREPRISE')")
    public User updateUser(@Valid @RequestBody User user) {
		return accountService.updateUser(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/password", consumes = "application/json", produces = "application/json")
    //@PreAuthorize("hasAuthority('ADMIN_ENTREPRISE')")
    @PreAuthorize("hasAuthority('ADMIN_ENTREPRISE') or hasAuthority('CLIENT')")
    public ResponseEntity<String> updatePassword(@Valid @RequestBody UpdatePwdRequest updatePwdRequest) {
        return keycloakService.setKeycloakUserPassword(updatePwdRequest, true);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/password/{userId}", produces = "application/json")
    public void resetPassword(
            @PathVariable("userId") Long userId,
            @Valid @RequestBody List<String> emailActionList
    ) {
        accountService.sendEmailActions(keycloakClientId, userId, emailActionList);
    }

    @DeleteMapping(value = "/user/{KeycloakId}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN_ENTREPRISE')")
    public ResponseEntity<String> deleteAccount(@PathVariable("KeycloakId") String keycloakId) {
        return accountService.deleteUser(keycloakId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN_ENTREPRISE')")
    @PutMapping(value = "/user/{realm}/{keycloakUserId}/{action}", produces = "application/json")
    public ResponseEntity<String> enableDisableAccount(
            @PathVariable("realm") String keycloakRealm,
            @PathVariable("keycloakUserId") String keycloakUserId,
            @PathVariable("action") boolean action) {
        return accountService.disableEnableKeycloakUser(keycloakRealm, keycloakUserId, action);
    }

}
