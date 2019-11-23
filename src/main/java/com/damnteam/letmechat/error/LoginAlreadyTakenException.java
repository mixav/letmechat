package com.damnteam.letmechat.error;

public class LoginAlreadyTakenException extends Exception {
    public LoginAlreadyTakenException() {
        super("User with this login already exists");
    }
}
