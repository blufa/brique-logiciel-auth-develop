package net.atos.zepe.auth.services.impl;

import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import net.atos.zepe.auth.beans.CustomKeycloak;
import net.atos.zepe.auth.beans.KeycloakProperties;
import net.atos.zepe.auth.exceptions.ResourceNotFoundException;
import net.atos.zepe.auth.exceptions.UnAuthorizedException;
import net.atos.zepe.auth.models.User;
import net.atos.zepe.auth.payload.request.UpdatePwdRequest;
import net.atos.zepe.auth.beans.jwt.JwtUtils;
import net.atos.zepe.auth.services.KeycloakService;
import net.sf.aspect4log.Log;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotAuthorizedException;
import java.util.*;

@Log
@Slf4j
@Service("keycloakService")
public class KeycloakServiceImpl implements KeycloakService {

    private final JwtUtils jwtUtils;
    private final Keycloak keycloak;
    private final CustomKeycloak keycloakConfig;
    private final KeycloakProperties properties;

    public KeycloakServiceImpl(JwtUtils jwtUtils, Keycloak keycloak, CustomKeycloak keycloakConfig, KeycloakProperties properties) {
        this.jwtUtils = jwtUtils;
        this.keycloak = keycloak;
        this.keycloakConfig = keycloakConfig;
        this.properties = properties;
    }

    @Override
    public String createKeycloakUser(String keycloakRealm, User user) {

        /* Initializing KEYCLOAK "user" representation */
        UserRepresentation keycloakUser = new UserRepresentation();
        /* Enabling "user" */
        keycloakUser.setEnabled(true);
        keycloakUser.setEmailVerified(true);

        /* Setting KEYCLOAK "user" Username */
        keycloakUser.setUsername(user.getUserName());
        /* Setting KEYCLOAK "user" Last Name */
        keycloakUser.setLastName(user.getUserLastName());
        /* Setting KEYCLOAK "user" First Name */
        keycloakUser.setFirstName(user.getUserFirstName());
        /* Setting KEYCLOAK "user" Email */
        keycloakUser.setEmail(user.getUserEmailAddress());
        /* Setting KEYCLOAK "user" Locale */
        keycloakUser.singleAttribute("locale", user.getUserLocale());

        UsersResource usersResource = usersResource();
        /* Creating KEYCLOAK "user" */
        try (Response response = usersResource.create(keycloakUser)) {
            log.info("keycloak Response URI: {}", response.getLocation());

            /* Checking response status */
            if (response.getStatus() != 201) {
                log.error("createKeycloakUser - error for - keycloakRealm: {} - user: {} - User not created : {}", keycloakRealm, user, response.getStatusInfo());
                throw new UnAuthorizedException("Unable to create user with status: " + response.getStatus(), response);
            }

            /* Getting KEYCLOAK "user" identifier */
            String keycloakUserIdentifier = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

            log.info("createKeycloakUser end ok - keycloakRealm: {} - keycloakUserIdentifier: {}", keycloakRealm, keycloakUserIdentifier);
            log.trace("createKeycloakUser end ok - response: {}", response);

            return keycloakUserIdentifier;
        } catch (Exception ex) {
            throw new UnAuthorizedException("Unable to create user with message: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void updateKeycloakUser(User user) {
        if (Objects.nonNull(user)) {
            /* Getting realm */
            String keycloakRealm = user.getUserRealm();

            /* Getting "user" KEYCLOAK ID */
            String keycloakUserIdentifier = user.getUserKeycloakId();

            /* Getting KEYCLOAK "user" representation */
            UserRepresentation keycloakUser = keycloak.realm(keycloakRealm).users().get(keycloakUserIdentifier).toRepresentation();


            /* Setting KEYCLOAK "user" Last Name */
            keycloakUser.setLastName(user.getUserLastName());

            /* Setting KEYCLOAK "user" First Name */
            keycloakUser.setFirstName(user.getUserFirstName());

            /* Getting userEmailAddress */
            String userEmailAddress = user.getUserEmailAddress();

            /* Checking userEmailAddress is valued */
            if (userEmailAddress != null) {

                /* Setting KEYCLOAK "user" Email */
                keycloakUser.setEmail(userEmailAddress);
            }

            /* Getting userLocale */
            String userLocale = user.getUserLocale();

            /* Checking userLocale is valued */
            if (userLocale != null) {

                /* Setting KEYCLOAK "user" Locale */
                keycloakUser.singleAttribute("locale", userLocale);

            }


            /* Getting KEYCLOAK "user" resource */
            UserResource keycloakUserResource = keycloak.realm(keycloakRealm).users().get(keycloakUserIdentifier);

            /* Updating KEYCLOAK "user" */
            keycloakUserResource.update(keycloakUser);

            /* If user password valued */
            if (user.getUserPassword() != null) {
                /* Resetting password */
                UpdatePwdRequest updatePwdRequest = new UpdatePwdRequest();
                updatePwdRequest.setRealm(keycloakRealm);
                updatePwdRequest.setKeycloakIdentifier(keycloakUserIdentifier);
                updatePwdRequest.setNewPassword(user.getUserPassword());
                setKeycloakUserPassword(updatePwdRequest, true);
            }
            log.info("updateKeycloakUser end ok - keycloakRealm: {} - keycloakUserIdentifier: {}", keycloakRealm, keycloakUserIdentifier);
            log.trace("updateKeycloakUser end ok - user: {}", user);
        }
    }

    @Async
    @Override
    public void resetPassword(String keycloakUuid) {
        userResource(keycloakUuid).executeActionsEmail(Arrays.asList("UPDATE_PASSWORD"));
    }

    @Override
    public void deleteKeycloakUser(String keycloakUserIdentifier) {

        /* Deleting KEYCLOAK "user" resource */
        userResource(keycloakUserIdentifier).remove();

        log.info("deleteKeycloakUser end ok - keycloakUserIdentifier: {}", keycloakUserIdentifier);

    }

    /**
     * @param updatePwdRequest
     * @param updatePasswordAction
     * @return
     */
    @Override
    public ResponseEntity<String> setKeycloakUserPassword(UpdatePwdRequest updatePwdRequest, boolean updatePasswordAction) {
        if (updatePasswordAction) {
            try (Keycloak kc = keycloakConfig.keycloakWith(updatePwdRequest.getUserName(), updatePwdRequest.getOldPassword())) {
                AccessTokenResponse accessToken = kc.tokenManager().getAccessToken();
                jwtUtils.validateJwtToken(accessToken.getToken());
            } catch (NotAuthorizedException notAuthorizedException) {
                log.info("setKeycloakUserPassword fail ! - The combination of keycloakUserName: {} and the old password is not correct for the - keycloakRealm: {} !", updatePwdRequest.getUserName(), updatePwdRequest.getRealm());
                throw new UnAuthorizedException("Invalid Credentials username/password");
            } catch (Exception exception) {
                log.info("setKeycloakUserPassword fail ! " + exception.getMessage());
                throw new UnAuthorizedException("Invalid Credentials username/password");
            }
        }

        /* Getting KEYCLOAK "user" resource */
        UserResource keycloakUserResource = userResource(updatePwdRequest.getKeycloakIdentifier());

        /* Creating KEYCLOAK "user" credentials */
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(updatePwdRequest.getNewPassword());

        /* Resetting KEYCLOAK "user" password */
        keycloakUserResource.resetPassword(passwordCred);
        log.info("setKeycloakUserPassword end ok - keycloakRealm: {} - keycloakUserIdentifier: {} - keycloakUserPassword: ####", updatePwdRequest.getRealm(), updatePwdRequest.getKeycloakIdentifier());
        return ResponseEntity.ok("Password updated successfully - keycloakRealm: " + updatePwdRequest.getRealm() + " - keycloakUserIdentifier: " + updatePwdRequest.getKeycloakIdentifier());
    }


    @Override
    public void sendEmailActions(String keycloakRealm, String keycloakClientId, String keycloakUserIdentifier, List<String> emailActionsList) {

        log.info(keycloakClientId);

        /* Getting client resource list */
        List<ClientRepresentation> keycloakClientRepresentationsList = realResource().clients().findByClientId(keycloakClientId);

        /* Checking if client resource list is not empty */
        if (!keycloakClientRepresentationsList.isEmpty()) {

            /* Setting client resource */
            ClientRepresentation keycloakClientRepresentation = keycloakClientRepresentationsList.get(0);

            /* Getting KEYCLOAK "user" resource */
            UserResource keycloakUserResource = keycloak.realm(keycloakRealm).users().get(keycloakUserIdentifier);

            /* Sending email actions */
            keycloakUserResource.executeActionsEmail(keycloakClientRepresentation.getClientId(), keycloakClientRepresentation.getBaseUrl(), emailActionsList);

        } else {

            log.error("Keycloak client representation not found for client ID  {}.", keycloakClientId);

        }

        log.info("sendEmailActions end ok - keycloakRealm: {}, keycloakClientId: {}, keycloakUserIdentifier: {}", keycloakRealm, keycloakClientId, keycloakUserIdentifier);
        log.trace("sendEmailActions end ok - emailActionsList: {}", emailActionsList);

    }

    @Override
    public List<RoleRepresentation> getRoleRepresentationsByRealmAndNames(String realm, Set<String> names) {
        RealmResource realmResource = keycloak.realm(realm);
        List<RoleRepresentation> realmRoles = realmResource.roles().list();

        List<RoleRepresentation> keyCloakRoles = new ArrayList<>();

        for (String role : names) {
            final RoleRepresentation roleRepresentation = realmRoles.stream()
                    .filter(rp -> rp.getName().equals(role))
                    .findAny()
                    .orElseThrow(() -> new ResourceNotFoundException("role", role));

            keyCloakRoles.add(roleRepresentation);
        }

        return keyCloakRoles;
    }

    @Override
    public void saveRoleRepresentationsInUser(String keyCloakUserId, List<RoleRepresentation> roleRepresentations) {
        RoleMappingResource roleMappingResource = userResource(keyCloakUserId).roles();
        List<RoleRepresentation> currentRoles = roleMappingResource.realmLevel().listAll();
        roleMappingResource.realmLevel().remove(currentRoles);
        roleMappingResource.realmLevel().add(roleRepresentations);
    }

    @Override
    public List<RoleRepresentation> getRoleRepresentationsByRealmAndKeyCloakUserId(String realm, String keyCloakUserId) {
        return userResource(keyCloakUserId).roles().realmLevel().listEffective();
    }


    @Override
    public RealmResource realResource() {
        return keycloak.realm(properties.getRealm());
    }

    @Override
    public UsersResource usersResource() {
        return realResource().users();
    }

    @Override
    public UserResource userResource(String userUuid) {
        return usersResource().get(userUuid);
    }
}
