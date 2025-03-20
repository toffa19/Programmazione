package com.progetto.model;

import java.util.List;

public class UsersWrapper {
    private List<User> users;

    public UsersWrapper() {
    }

    public UsersWrapper(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() { return users; }
    public void setUsers(List<User> users) { this.users = users; }
}
