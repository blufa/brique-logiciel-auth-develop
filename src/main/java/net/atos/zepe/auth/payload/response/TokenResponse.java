
package net.atos.zepe.auth.payload.response;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "exp",
    "iat",
    "jti",
    "iss",
    "sub",
    "typ",
    "azp",
    "session_state",
    "acr",
    "allowed-origins",
    "realm_access",
    "scope",
    "sid",
    "email_verified",
    "name",
    "preferred_username",
    "locale",
    "given_name",
    "family_name",
    "email"
})
@Generated("jsonschema2pojo")
public class TokenResponse {

    @JsonProperty("exp")
    private Integer exp;
    @JsonProperty("iat")
    private Integer iat;
    @JsonProperty("jti")
    private String jti;
    @JsonProperty("iss")
    private String iss;
    @JsonProperty("sub")
    private String sub;
    @JsonProperty("typ")
    private String typ;
    @JsonProperty("azp")
    private String azp;
    @JsonProperty("session_state")
    private String sessionState;
    @JsonProperty("acr")
    private String acr;
    @JsonProperty("allowed-origins")
    private List<String> allowedOrigins;
    @JsonProperty("realm_access")
    private RealmAccessResponse realmAccess;
    @JsonProperty("scope")
    private String scope;
    @JsonProperty("sid")
    private String sid;
    @JsonProperty("email_verified")
    private Boolean emailVerified;
    @JsonProperty("name")
    private String name;
    @JsonProperty("preferred_username")
    private String preferredUsername;
    @JsonProperty("locale")
    private String locale;
    @JsonProperty("given_name")
    private String givenName;
    @JsonProperty("family_name")
    private String familyName;
    @JsonProperty("email")
    private String email;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("exp")
    public Integer getExp() {
        return exp;
    }

    @JsonProperty("exp")
    public void setExp(Integer exp) {
        this.exp = exp;
    }

    @JsonProperty("iat")
    public Integer getIat() {
        return iat;
    }

    @JsonProperty("iat")
    public void setIat(Integer iat) {
        this.iat = iat;
    }

    @JsonProperty("jti")
    public String getJti() {
        return jti;
    }

    @JsonProperty("jti")
    public void setJti(String jti) {
        this.jti = jti;
    }

    @JsonProperty("iss")
    public String getIss() {
        return iss;
    }

    @JsonProperty("iss")
    public void setIss(String iss) {
        this.iss = iss;
    }

    @JsonProperty("sub")
    public String getSub() {
        return sub;
    }

    @JsonProperty("sub")
    public void setSub(String sub) {
        this.sub = sub;
    }

    @JsonProperty("typ")
    public String getTyp() {
        return typ;
    }

    @JsonProperty("typ")
    public void setTyp(String typ) {
        this.typ = typ;
    }

    @JsonProperty("azp")
    public String getAzp() {
        return azp;
    }

    @JsonProperty("azp")
    public void setAzp(String azp) {
        this.azp = azp;
    }

    @JsonProperty("session_state")
    public String getSessionState() {
        return sessionState;
    }

    @JsonProperty("session_state")
    public void setSessionState(String sessionState) {
        this.sessionState = sessionState;
    }

    @JsonProperty("acr")
    public String getAcr() {
        return acr;
    }

    @JsonProperty("acr")
    public void setAcr(String acr) {
        this.acr = acr;
    }

    @JsonProperty("allowed-origins")
    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    @JsonProperty("allowed-origins")
    public void setAllowedOrigins(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    @JsonProperty("realm_access")
    public RealmAccessResponse getRealmAccess() {
        return realmAccess;
    }

    @JsonProperty("realm_access")
    public void setRealmAccess(RealmAccessResponse realmAccess) {
        this.realmAccess = realmAccess;
    }

    @JsonProperty("scope")
    public String getScope() {
        return scope;
    }

    @JsonProperty("scope")
    public void setScope(String scope) {
        this.scope = scope;
    }

    @JsonProperty("sid")
    public String getSid() {
        return sid;
    }

    @JsonProperty("sid")
    public void setSid(String sid) {
        this.sid = sid;
    }

    @JsonProperty("email_verified")
    public Boolean getEmailVerified() {
        return emailVerified;
    }

    @JsonProperty("email_verified")
    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("preferred_username")
    public String getPreferredUsername() {
        return preferredUsername;
    }

    @JsonProperty("preferred_username")
    public void setPreferredUsername(String preferredUsername) {
        this.preferredUsername = preferredUsername;
    }

    @JsonProperty("locale")
    public String getLocale() {
        return locale;
    }

    @JsonProperty("locale")
    public void setLocale(String locale) {
        this.locale = locale;
    }

    @JsonProperty("given_name")
    public String getGivenName() {
        return givenName;
    }

    @JsonProperty("given_name")
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    @JsonProperty("family_name")
    public String getFamilyName() {
        return familyName;
    }

    @JsonProperty("family_name")
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
