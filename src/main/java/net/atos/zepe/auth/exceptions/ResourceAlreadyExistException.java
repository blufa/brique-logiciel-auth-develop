package net.atos.zepe.auth.exceptions;

public class ResourceAlreadyExistException extends RuntimeException {

    public ResourceAlreadyExistException(String entity, Object value) {
        super(entity + " in conflict with the value : " + value);
    }

    public ResourceAlreadyExistException(String entity, String realm, String usernameOrEmail) {
        super(entity + " already exists for realm : " + realm + " and username/email : " + usernameOrEmail);
    }
    
}
