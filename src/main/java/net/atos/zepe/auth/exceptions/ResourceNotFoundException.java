package net.atos.zepe.auth.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String entity, Object value) {
        super(entity + " not found with the value : " + value);
    }

    public ResourceNotFoundException(String entity, String realm, String reference) {
        super(entity + " not found with the realm : " + realm + " and reference : " + reference);
    }

}
