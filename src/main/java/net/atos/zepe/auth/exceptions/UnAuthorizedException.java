package net.atos.zepe.auth.exceptions;

import lombok.Getter;
import lombok.Setter;

import jakarta.ws.rs.core.Response;


@Getter
@Setter
public class UnAuthorizedException extends RuntimeException {

    private int errorCode = -1;
    private boolean loggable;
    private transient Response response;

    public UnAuthorizedException() {
        this.errorCode = 401;
    }

    public UnAuthorizedException(String message, Response response) {
        super(message);
        this.response = response;
    }

    public UnAuthorizedException(Throwable throwable, Response response) {
        super(throwable);
        this.response = response;
    }

    public UnAuthorizedException(String s, Throwable throwable) {
        super(s, throwable);
        this.errorCode = 500;
    }

    public UnAuthorizedException(Throwable throwable) {
        super(throwable);
        this.errorCode = 500;
    }

    public UnAuthorizedException(String s) {
        super(s);
        this.errorCode = 500;
    }

    public UnAuthorizedException(int errorCode) {
        this.errorCode = errorCode;
    }

    public UnAuthorizedException(String s, int errorCode) {
        super(s);
        this.errorCode = errorCode;
    }

    public UnAuthorizedException(String s, Throwable throwable, int errorCode) {
        super(s, throwable);
        this.errorCode = errorCode;
    }

    public UnAuthorizedException(Throwable throwable, int errorCode) {
        super(throwable);
        this.errorCode = errorCode;
    }
}
