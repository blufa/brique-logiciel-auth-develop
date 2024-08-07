package net.atos.zepe.auth.repository;

import java.util.Optional;

import net.atos.zepe.auth.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  UserRepository extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByUserName(String username);

	Boolean existsByUserName(String username);

	Boolean existsByUserEmailAddress(String email);

	Optional<UserEntity> findByUserKeycloakId(String keyclaokId);

	Optional<UserEntity> findByUserRealmAndUserKeycloakId(String keycloakRealm, String userKeycloakId);

	Optional<UserEntity> findByUserRealmAndUserName(String keycloakRealm, String username);

	boolean existsByUserRealmAndUserName(String keycloakRealm, String username);

	boolean existsByUserRealmAndUserEmailAddress(String keycloakRealm, String userEmailAddress);

	Optional<UserEntity> findByUserEmailAddress(String email);
}
