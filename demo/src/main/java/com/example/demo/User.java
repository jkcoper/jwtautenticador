package com.example.demo;

public class User {
    private String id_user; // Agregar este campo
    private String username;
    private String password;

    // Getters y Setters
    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getUsername() {
        return username;
    }

    public String getIduser() {
        return id_user;
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
