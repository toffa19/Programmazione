package com.progetto.model;

import java.util.List;

public class User {
    private String id;
    private String username;
    private String password; // password già hashata
    private String firstName;
    private String lastName;
    private String email;
    private List<Progress> progress;
    private String nickName;
    private String country;
    private String language;
    public User() {
    }

    public User(String id, String username, String password, String firstName, String lastName, String email, List<Progress> progress) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.progress = progress;
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<Progress> getProgress() { return progress; }
    public void setProgress(List<Progress> progress) { this.progress = progress; }
    // Getter e setter per nickName
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    // Getter e setter per country
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    // Getter e setter per language
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }

}
