package com.springtest.crudrest.dto;

import jakarta.validation.constraints.Size;

public class LibrarianDto {
    @Size(min = 2, max = 20, message = "Username should be between 2 and 20 characters")
    private String username;

    private String password;

    // Класс содержит точно такие же поля как и AuthenticationDto, но этот класс создан на будущее.
    // Сюда можно добавить доп. поля.
    // AuthenticationDto будет хранить только username и password.

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
