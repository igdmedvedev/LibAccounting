package com.springtest.crudrest.dto;

import jakarta.validation.constraints.Size;

public class AuthenticationDto {
    @Size(min = 2, max = 20, message = "Username should be between 2 and 20 characters")
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
