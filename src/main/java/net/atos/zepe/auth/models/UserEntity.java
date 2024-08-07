package net.atos.zepe.auth.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "UM_User", uniqueConstraints = { @UniqueConstraint(columnNames = "UM_UserName"), @UniqueConstraint(columnNames = "UM_UserEmailAddress")})
public class UserEntity implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "UM_UserName", unique = true)
	private String userName;

	@CreatedDate
	@Column(name = "UM_UserCreationDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date userCreationDate;

	@LastModifiedDate
	@Column(name = "UM_UserModificationDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date userModificationDate;

	@EqualsAndHashCode.Include
	@Column(name = "UM_UserRealm", nullable = false)
	private String userRealm;

	@Column(name = "UM_UserKeycloakId")
	private String userKeycloakId;

	@Column(name = "UM_UserStatus")
	private UserStatus userStatus;

	private String userPassword;

	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RoleEntity> roles;

	@EqualsAndHashCode.Include
	@Column(name = "UM_UserLastName")
	private String userLastName;

	@EqualsAndHashCode.Include
	@Column(name = "UM_UserFirstName")
	private String userFirstName;

	@EqualsAndHashCode.Include
	@Column(name = "UM_UserEmailAddress")
	private String userEmailAddress;

	@EqualsAndHashCode.Include
	@Column(name = "UM_UserLocale")
	private String userLocale;

	@Column(name = "UM_UserFirstConnection")
	boolean firstConnection;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles.stream().map(roleEntity -> new SimpleGrantedAuthority(roleEntity.getName())).toList();
	}

	@Override
	public String getPassword() {
		return this.userPassword;
	}

	@Override
	public String getUsername() {
		return this.userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return isEnabled();
	}

	@Override
	public boolean isAccountNonLocked() {
		return isEnabled();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return isEnabled();
	}

	@Override
	public boolean isEnabled() {
		return userStatus.equals(UserStatus.ACTIVATED);
	}
}
