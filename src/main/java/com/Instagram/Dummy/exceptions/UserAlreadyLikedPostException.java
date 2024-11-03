package com.Instagram.Dummy.exceptions;

public class UserAlreadyLikedPostException extends RuntimeException {
    public UserAlreadyLikedPostException(String message) {
        super(message);
    }
}
