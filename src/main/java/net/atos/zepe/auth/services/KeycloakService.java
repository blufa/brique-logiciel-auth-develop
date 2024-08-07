package net.atos.zepe.auth.services;

import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.http.ResponseEntity;
import net.atos.zepe.auth.models.User;
import net.atos.zepe.auth.payload.request.UpdatePwdRequest;

import java.util.List;
import java.util.Set;

public interface KeycloakService {
    String createKeycloakUser(String keycloakRealm, User user);

    void updateKeycloakUser(User user);

    void resetPassword(String keycloakUuid);

    void deleteKeycloakUser(String keycloakUserIdentifier);

    ResponseEntity<String> setKeycloakUserPassword(UpdatePwdRequest updatePwdRequest, boolean updatePasswordAction);

    void sendEmailActions(String keycloakRealm, String keycloakClientId, String keycloakUserIdentifier, List<String> emailActionsList);

    List<RoleRepresentation> getRoleRepresentationsByRealmAndNames(String realm, Set<String> names);

    void saveRoleRepresentationsInUser(String keyCloakUserId, List<RoleRepresentation> roleRepresentations);

    List<RoleRepresentation> getRoleRepresentationsByRealmAndKeyCloakUserId(String realm, String keyCloakUserId);

    RealmResource realResource();
    UsersResource usersResource();

    UserResource userResource(String userUuid);
}
