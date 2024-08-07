package net.atos.zepe.auth.mappers;

import org.springframework.stereotype.Component;
import net.atos.zepe.auth.models.RoleEntity;
import net.atos.zepe.auth.models.User;
import net.atos.zepe.auth.models.UserEntity;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapperImpl extends UserMapper {

    @Override
    public User asUser(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .userCreationDate(userEntity.getUserCreationDate())
                .userName(userEntity.getUsername())
                .userFirstName(userEntity.getUserFirstName())
                .userLastName(userEntity.getUserLastName())
                .userLocale(userEntity.getUserLocale())
                .userKeycloakId(userEntity.getUserKeycloakId())
                .userStatus(userEntity.getUserStatus())
                .userEmailAddress(userEntity.getUserEmailAddress())
                .userRealm(userEntity.getUserRealm())
                .firstConnection(userEntity.isFirstConnection())
                .roles(userEntity.getRoles().stream().map(RoleEntity::getName).collect(Collectors.toSet()))
                .userModificationDate(userEntity.getUserModificationDate())
                .build();
    }

    @Override
    public UserEntity asUserEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setUserName(user.getUserName());
        entity.setUserKeycloakId(user.getUserKeycloakId());
        entity.setUserLocale(user.getUserLocale());
        entity.setUserFirstName(user.getUserFirstName());
        entity.setUserLastName(user.getUserLastName());
        entity.setUserRealm(user.getUserRealm());
        entity.setUserPassword(user.getUserPassword());
        entity.setUserCreationDate(user.getUserCreationDate());
        entity.setUserModificationDate(user.getUserModificationDate());
        entity.setUserStatus(user.getUserStatus());
        entity.setUserEmailAddress(user.getUserEmailAddress());
        entity.setFirstConnection(user.isFirstConnection());
        entity.setRoles(user.getRoles().stream().map(roleName -> {
            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setName(roleName);
            return roleEntity;
        }).collect(Collectors.toList()));
        return entity;
    }

    @Override
    public List<User> asUsers(List<UserEntity> userEntities) {
        return userEntities.stream().parallel().map(this::asUser).collect(Collectors.toList());
    }

    @Override
    public List<UserEntity> asUserEntities(List<User> users) {
        return users.stream().parallel().map(this::asUserEntity).collect(Collectors.toList());
    }
}
