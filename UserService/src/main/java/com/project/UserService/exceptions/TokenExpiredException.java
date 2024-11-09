package com.project.UserService.exceptions;

public class TokenExpiredException extends RuntimeException {

    public TokenExpiredException() {
        super(ExceptionMessages.TOKEN_EXPIRED.getMessage());

    }
    public TokenExpiredException(Throwable cause) {
        super(ExceptionMessages.TOKEN_EXPIRED.getMessage(), cause);
    }
}
