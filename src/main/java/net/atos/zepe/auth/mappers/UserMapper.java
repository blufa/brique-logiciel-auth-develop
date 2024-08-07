package net.atos.zepe.auth.mappers;

import net.atos.zepe.auth.models.User;
import net.atos.zepe.auth.models.RoleEntity;
import net.atos.zepe.auth.models.UserEntity;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class UserMapper {

	public abstract User asUser(UserEntity userEntity);
	public abstract UserEntity asUserEntity(User user);

	public abstract List<User> asUsers(List<UserEntity> userEntities);
	public abstract List<UserEntity> asUserEntities(List<User> users);

	protected List<RoleEntity> map(Set<String> value) {
		return value.stream().map(s -> {
			RoleEntity roleEntity = new RoleEntity();
			roleEntity.setName(s);
			return roleEntity;
		}).collect(Collectors.toList());
	}

	protected String mapRoleEntitySourceToString(RoleEntity roleEntity) {
		return roleEntity.getName();
	}

}
