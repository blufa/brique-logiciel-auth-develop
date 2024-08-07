package net.atos.zepe.auth.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.atos.zepe.auth.beans.CustomKeycloak;
import net.atos.zepe.auth.exceptions.ResourceAlreadyExistException;
import net.atos.zepe.auth.exceptions.ResourceNotFoundException;
import net.atos.zepe.auth.exceptions.UnAuthorizedException;
import net.atos.zepe.auth.mappers.UserMapper;
import net.atos.zepe.auth.models.RoleEntity;
import net.atos.zepe.auth.models.User;
import net.atos.zepe.auth.models.UserEntity;
import net.atos.zepe.auth.models.UserStatus;
import net.atos.zepe.auth.payload.request.UpdatePwdRequest;
import net.atos.zepe.auth.repository.UserRepository;
import net.atos.zepe.auth.services.AccountService;
import net.atos.zepe.auth.services.KeycloakService;
import net.sf.aspect4log.Log;
import org.apache.commons.lang3.StringUtils;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotAuthorizedException;
import java.util.*;
import java.util.stream.Collectors;

@Log
@Slf4j
@Service("AccountService")
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private static final String USER = "user";
    private final UserRepository userRepository;

    private final UserMapper userMapper;
    private final KeycloakService keycloakService;

    private final Keycloak keycloak;
    private final CustomKeycloak keycloakConfig;

    @Override
    public User createUser(User user) {

        /* Checking if resource already exists */
        if (Objects.nonNull(user) && Boolean.TRUE.equals(userRepository.existsByUserName(user.getUserName()))) {
            throw new ResourceAlreadyExistException(USER, user.getUserRealm(), user.getUserName());
        }

        /* Checking if userEmailAddress is valued */
        if (Objects.nonNull(user) && !StringUtils.isEmpty(user.getUserEmailAddress())) {

            /* Checking if userEmailAddress is not already used */
            checkUserEmailAddressUnicity(user.getUserRealm(), user.getUserEmailAddress());
        }

        /* Getting roleRepresentations */
        List<RoleRepresentation> roleRepresentations = keycloakService.getRoleRepresentationsByRealmAndNames(user.getUserRealm(), user.getRoles());

        /* Creating KEYCLOAK user */
        String keycloakUserIdentifier = keycloakService.createKeycloakUser(user.getUserRealm(), user);

        /* Save role into keycloak */
        keycloakService.saveRoleRepresentationsInUser(keycloakUserIdentifier, roleRepresentations);

        /* If KEYCLOAK user password is filled */
        if (StringUtils.isNotBlank(user.getUserPassword())) {
            /* Setting KEYCLOAK user password */
            UpdatePwdRequest updatePwdRequest = new UpdatePwdRequest();
            updatePwdRequest.setRealm(user.getUserRealm());
            updatePwdRequest.setKeycloakIdentifier(keycloakUserIdentifier);
            updatePwdRequest.setNewPassword(user.getUserPassword());
            keycloakService.setKeycloakUserPassword(updatePwdRequest, false);
        }

        /* Setting user KEYCLOAK realm */
        user.setUserRealm(user.getUserRealm());

        /* Setting user KEYCLOAK Identifier */
        user.setUserKeycloakId(keycloakUserIdentifier);
        user.setUserCreationDate(new Date());

        /* Getting related Entity */
        UserEntity userEntity = userMapper.asUserEntity(user);

        userEntity.setUserPassword(null);
        /* Get roles from keycloak (because of composite role) and set them into userEntity */
        List<RoleEntity> roleEntities = keycloakService.getRoleRepresentationsByRealmAndKeyCloakUserId(user.getUserRealm(), keycloakUserIdentifier).stream().map(role -> {
            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setName(role.getName());
            roleEntity.setUser(userEntity);
            return roleEntity;
        }).collect(Collectors.toList());

        userEntity.setRoles(roleEntities);

        /* Saving entity */
        UserEntity savedUserEntity = userRepository.save(userEntity);

        /* Getting related DTO */
        return userMapper.asUser(savedUserEntity);
    }

    @Override
    public AccessTokenResponse login(String username, String password) {
        try (Keycloak kc = keycloakConfig.keycloakWith(username, password)) {
            return kc.tokenManager().getAccessToken();
        } catch (NotAuthorizedException notAuthorizedException) {
            log.info("setKeycloakUserPassword fail ! - The combination of keycloakUserName: {} and the old password is not correct !", username);
        } catch (Exception exception) {
            log.info("setKeycloakUserPassword fail ! " + exception.getMessage());
        }

        throw new UnAuthorizedException("Invalid credentials username/password", 401);
    }

    @Override
    public void deleteKeycloakUser(String keycloakRealm, String keycloakUserIdentifier) {

        /* Getting KEYCLOAK "user" resource */
        UserResource keycloakUserResource = keycloak.realm(keycloakRealm).users().get(keycloakUserIdentifier);

        /* Deleting KEYCLOAK "user" resource */
        keycloakUserResource.remove();

        log.info("deleteKeycloakUser end ok - keycloakRealm: {} - keycloakUserIdentifier: {}", keycloakRealm, keycloakUserIdentifier);

    }

    @Override
    public void disableKeycloakUser(String keycloakRealm, String keycloakUserIdentifier) {

        /* Getting KEYCLOAK "user" resource */
        UserResource keycloakUserResource = keycloak.realm(keycloakRealm).users().get(keycloakUserIdentifier);
        /* Deleting KEYCLOAK "user" resource */
        UserRepresentation userRepresentation = keycloakUserResource.toRepresentation();
        userRepresentation.setEnabled(false);
        keycloakUserResource.update(userRepresentation);
        log.info("disabledKeycloakUser end ok - keycloakRealm: {} - keycloakUserIdentifier: {}", keycloakRealm, keycloakUserIdentifier);

    }

    @Override
    public void enableKeycloakUser(String keycloakRealm, String keycloakUserIdentifier) {

    }

    @Override
    public ResponseEntity<String> disableEnableKeycloakUser(String keycloakRealm, String keycloakUserId, boolean enable) {
        try {
            /* Getting KEYCLOAK "user" resource */
            UserResource keycloakUserResource = keycloak.realm(keycloakRealm).users().get(keycloakUserId);
            /* Deleting KEYCLOAK "user" resource */
            UserRepresentation userRepresentation = keycloakUserResource.toRepresentation();
            userRepresentation.setEnabled(enable);
            User user = userMapper.asUser(userRepository.findByUserKeycloakId(keycloakUserId).orElseThrow(() -> new ResourceNotFoundException(USER, keycloakUserId)));
            user.setUserStatus(enable ? UserStatus.ACTIVATED : UserStatus.BLOCKED);
            updateUser(user);
            keycloakUserResource.update(userRepresentation);
            log.info("enabledKeycloakUser end ok - keycloakRealm: {} - keycloakUserIdentifier: {}", keycloakRealm, keycloakUserId);
            return ResponseEntity.ok("User " + (enable ? "enabled" : "disabled") + " successfully - user id " + user.getId());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Enable user exception " + exception.getMessage());
        }
    }

    @Override
    public User readUserByUserId(Long userId) {

        /* Getting user */
        User user = userMapper.asUser(userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER, userId)));

        log.info("readUserByUserId end ok - userId: {}", userId);

        return user;

    }

    @Override
    public User readByUserEmailAddress(String email) {

        /* Getting user */
        User user = userMapper.asUser(userRepository.findByUserEmailAddress(email).orElseThrow(() -> new ResourceNotFoundException(USER, email)));

        log.info("readByUserEmailAddress end ok - email: {}", email);

        return user;

    }

    @Override
    public User readUserByKeycloakRealmAndUserKeycloakId(String keycloakRealm, String userKeycloakId) {

        /* Getting user */
        User user = userMapper.asUser(userRepository.findByUserRealmAndUserKeycloakId(keycloakRealm, userKeycloakId)
                .orElseThrow(() -> new ResourceNotFoundException(USER, keycloakRealm, userKeycloakId)));

        log.info("readUserByKeycloakRealmAndUserKeycloakId end ok - keycloakRealm: {} - userKeycloakId: {}", keycloakRealm, userKeycloakId);

        return user;

    }

    @Override
    public User readUserByUserKeycloakId(String userKeycloakId) {
        return userMapper.asUser(userRepository.findByUserKeycloakId(userKeycloakId).orElseThrow(() -> new ResourceNotFoundException(UserEntity.class.getSimpleName(), userKeycloakId)));
    }

    @Override
    public User readUserByUserName(String userName) {
        return userMapper.asUser(userRepository.findByUserName(userName).orElseThrow(() -> new ResourceNotFoundException(UserEntity.class.getSimpleName(), userName)));
    }

    @Override
    public User readUserByKeycloakRealmAndUsername(String keycloakRealm, String username) {

        /* Getting user */
        User user = userMapper.asUser(userRepository.findByUserRealmAndUserName(keycloakRealm, username)
                .orElseThrow(() -> new ResourceNotFoundException(USER, keycloakRealm, username)));

        log.info("readUserByKeycloakRealmAndUsername end ok - keycloakRealm: {} - username: {}", keycloakRealm, username);

        return user;

    }

    @Override
    public User updateUser(User user) {

        /* Getting ID */
        Long userId = user.getId();
        /* Checking if ID exists */

        UserEntity existUser = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER, userId));

        if (user.getUserEmailAddress() == null) {
            user.setUserEmailAddress(existUser.getUserEmailAddress());
        }

        if (user.getUserFirstName() == null) {
            user.setUserFirstName(existUser.getUserFirstName());
        }

        if (user.getUserLastName() == null) {
            user.setUserLastName(existUser.getUserLastName());
        }

        if (user.getUserName() == null) {
            user.setUserName(existUser.getUsername());
        }

        if (user.getUserStatus() == null) {
            user.setUserStatus(existUser.getUserStatus());
        }

        if (user.getUserLocale() == null) {
            user.setUserLocale(existUser.getUserLocale());
        }

        if (user.getUserRealm() == null) {
            user.setUserRealm(existUser.getUserRealm());
        }


        if (user.getRoles().isEmpty()) {
            Set<String> roles = new HashSet<>();
            for (RoleEntity role : existUser.getRoles())
                roles.add(role.getName());
            user.setRoles(roles);
        }

        /* Checking if userEmailAddress is valued */
        if (!StringUtils.isEmpty(user.getUserEmailAddress())) {
            if (!user.getUserEmailAddress().isEmpty() && !existUser.getUserEmailAddress().equalsIgnoreCase(user.getUserEmailAddress())) {
                /* Checking if userEmailAddress is not already used */
                checkUserEmailAddressUnicity(user.getUserRealm(), user.getUserEmailAddress());
            }
        }
        List<RoleRepresentation> roleRepresentations = keycloakService.getRoleRepresentationsByRealmAndNames(user.getUserRealm(), user.getRoles());

        /* Updating KEYCLOAK user */
        keycloakService.updateKeycloakUser(user);

        /* Updating KEYCLOAK roles */
        keycloakService.saveRoleRepresentationsInUser(user.getUserKeycloakId(), roleRepresentations);

        /* Getting related Entity */
        UserEntity userEntity = userMapper.asUserEntity(user);

        /* Get roles from keycloak (because of composite role) and set them into userEntity */
        List<RoleEntity> roleEntities = keycloakService
                .getRoleRepresentationsByRealmAndKeyCloakUserId(user.getUserRealm(), user.getUserKeycloakId()).stream().map(role -> {
                    RoleEntity roleEntity = new RoleEntity();
                    roleEntity.setName(role.getName());
                    roleEntity.setUser(userEntity);
                    return roleEntity;
                }).collect(Collectors.toList());

        userEntity.setRoles(roleEntities);
        userEntity.setUserCreationDate(user.getUserCreationDate());
        userEntity.setUserModificationDate(new Date());
        userEntity.setUserPassword(null);
        /* Saving entity */
        return userMapper.asUser(userRepository.save(userEntity));
    }

    @Override
    public ResponseEntity<String> deleteUser(String keycloakId) {
        User user = userMapper.asUser(userRepository.findByUserKeycloakId(keycloakId).orElseThrow(() -> new ResourceNotFoundException(USER, keycloakId)));
        keycloakService.deleteKeycloakUser(user.getUserKeycloakId());
        userRepository.deleteById(user.getId());
        log.info("deleteUser end ok - userId: {}", user.getId());
        return ResponseEntity.ok("User deleted successfully - user id " + user.getId());
    }

    @Override
    public void sendEmailActions(String keycloakClientId, Long userId, List<String> emailActionsList) {

        /* Getting user */
        User user = userMapper.asUser(userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER, userId)));

        /* Getting KEYCLOAK connection instance */
        keycloakService.sendEmailActions(user.getUserRealm(), keycloakClientId, user.getUserKeycloakId(), emailActionsList);

        log.info("sendEmailActions end ok - keycloakRealm: {}, keycloakClientId: {}, keycloakUserIdentifier: {}", user.getUserRealm(), keycloakClientId,
                user.getUserKeycloakId());
        log.trace("sendEmailActions end ok - emailActionsList: {}", emailActionsList);

    }

    private void checkUserEmailAddressUnicity(String keycloakRealm, String userEmailAddress) {

        /* Checking if resource already exists */
        if (userRepository.existsByUserRealmAndUserEmailAddress(keycloakRealm, userEmailAddress)) {
            throw new ResourceAlreadyExistException(USER, keycloakRealm, userEmailAddress);
        }
    }

}
