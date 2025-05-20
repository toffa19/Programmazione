package com.progetto.model;

import java.time.LocalDate;
import java.util.List;

public class User {
    private String id;
    private String username;
    private String password;
    private String firstName;
    private String email;

    private String role;



    // Campo per registrare i progressi
    private List<Progress> progress;

    // Lista di date di login per gestire streak
    private List<String> loginDates;

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }


    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    // Getter / Setter
    public List<String> getLoginDates() {
        return loginDates;
    }
    public void setLoginDates(List<String> loginDates) {
        this.loginDates = loginDates;
    }

    public List<Progress> getProgress() { return progress; }
    public void setProgress(List<Progress> progress) { this.progress = progress; }
}