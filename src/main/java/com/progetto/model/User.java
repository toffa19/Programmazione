package com.progetto.model;

import java.time.LocalDate;
import java.util.List;

public class User {
    private String id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;

    private String role;


    // Nuovi campi per il profilo
    private String nickName;
    private String country;
    private String language;
    private String gender;
    private String timeZone;

    // Campo per registrare i progressi
    private List<Progress> progress;

    // Lista di date di login per gestire streak
    private List<LocalDate> loginDates;

    // Getters and setters
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

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

//    public String getNickName() { return nickName; }
//    public void setNickName(String nickName) { this.nickName = nickName; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

//    public String getGender() { return gender; }
//    public void setGender(String gender) { this.gender = gender; }
//
//    public String getTimeZone() { return timeZone; }
//    public void setTimeZone(String timeZone) { this.timeZone = timeZone; }

    public List<Progress> getProgress() { return progress; }
    public void setProgress(List<Progress> progress) { this.progress = progress; }

    public List<LocalDate> getLoginDates() {
        return loginDates;
    }
    public void setLoginDates(List<LocalDate> loginDates) {
        this.loginDates = loginDates;
    }
}