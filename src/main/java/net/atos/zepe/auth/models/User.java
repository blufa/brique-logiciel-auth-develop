package net.atos.zepe.auth.models;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.*;

/**
 * Bean for User
 *
 * @author a513197
 */
@Builder
@Data
public class User {

    private Long id;

    private String userName;

    @Temporal(TemporalType.TIMESTAMP)
    private Date userCreationDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date userModificationDate;
    @Valid
    private String userRealm;
    @Valid
    private String userKeycloakId;

    private UserStatus userStatus;

    private String userPassword;
    @Valid
    private String userLastName;
    @Valid
    private String userFirstName;
    @Valid
    private String userEmailAddress;

    private String userLocale;
    private Set<String> roles = new HashSet<>();

    boolean firstConnection;

    public boolean isFirstConnection() {
        return firstConnection;
    }

    public void setFirstConnection(boolean firstConnection) {
        this.firstConnection = firstConnection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getUserName(), user.getUserName()) && Objects.equals(getUserRealm(), user.getUserRealm()) && Objects.equals(getUserKeycloakId(), user.getUserKeycloakId()) && Objects.equals(getUserEmailAddress(), user.getUserEmailAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserName(), getUserRealm(), getUserKeycloakId(), getUserEmailAddress());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getUserCreationDate() {
        return userCreationDate;
    }

    public void setUserCreationDate(Date userCreationDate) {
        this.userCreationDate = userCreationDate;
    }

    public Date getUserModificationDate() {
        return userModificationDate;
    }

    public void setUserModificationDate(Date userModificationDate) {
        this.userModificationDate = userModificationDate;
    }

    public String getUserRealm() {
        return userRealm;
    }

    public void setUserRealm(String userRealm) {
        this.userRealm = userRealm;
    }

    public String getUserKeycloakId() {
        return userKeycloakId;
    }

    public void setUserKeycloakId(String userKeycloakId) {
        this.userKeycloakId = userKeycloakId;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserEmailAddress() {
        return userEmailAddress;
    }

    public void setUserEmailAddress(String userEmailAddress) {
        this.userEmailAddress = userEmailAddress;
    }

    public String getUserLocale() {
        return userLocale;
    }

    public void setUserLocale(String userLocale) {
        this.userLocale = userLocale;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

}