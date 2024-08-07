package net.atos.zepe.auth.services;

import net.atos.zepe.auth.models.User;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AccountService {

    User createUser(User user);

    AccessTokenResponse login(String username, String password);

    ResponseEntity<String> disableEnableKeycloakUser(String keycloakRealm, String keycloakUserId, boolean enable);

    User readUserByUserId(Long userId);

    User readUserByKeycloakRealmAndUserKeycloakId(String keycloakRealm, String userKeycloakId);

    User readUserByUserKeycloakId(String userKeycloakId);

    User readByUserEmailAddress(String email);
    User readUserByUserName(String userName);

    User readUserByKeycloakRealmAndUsername(String keycloakRealm, String username);

    //Page<User> readUsers(String realm, Optional<String> email, Optional<String> firstName, Optional<String> lastName, List<String> roles, Pageable pageable);

    void deleteKeycloakUser(String keycloakRealm, String keycloakUserIdentifier);
    void disableKeycloakUser(String keycloakRealm, String keycloakUserIdentifier);
    void enableKeycloakUser(String keycloakRealm, String keycloakUserIdentifier);
    User updateUser(User user);
    ResponseEntity<String> deleteUser(String keycloakId);

    void sendEmailActions(String keycloakClientId, Long userId, List<String> emailActionsList);

}