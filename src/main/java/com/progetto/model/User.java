package com.progetto.model;

import java.util.List;

public class User {
    private String id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    // Nuovi campi per il profilo
    private String nickName;
    private String country;
    private String language;
    private String gender;
    private String timeZone;
    // Campo per registrare i progressi
    private List<Progress> progress;

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

//    public String getLastName() { return lastName; }
//    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }


    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public List<Progress> getProgress() { return progress; }
    public void setProgress(List<Progress> progress) { this.progress = progress; }
}
