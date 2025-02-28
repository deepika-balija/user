package com.apexon.catchIt.user.config;

public class AuthResponse {
    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    private String token;
}
