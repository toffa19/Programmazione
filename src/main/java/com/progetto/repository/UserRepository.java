package com.progetto.repository;

import com.progetto.model.User;
import com.progetto.model.UsersWrapper;
import com.progetto.util.Config;
import java.util.ArrayList;
import java.util.List;

public class UserRepository extends AbstractRepository<UsersWrapper> {

    private UsersWrapper usersWrapper;

    public UserRepository() {
        super();
        loadUsers();
    }

    @Override
    protected String getFilePath() {
        // Utilizza il percorso centralizzato definito in Config
        return Config.USERS_JSON_PATH;
    }

    private void loadUsers() {
        UsersWrapper loaded = loadData(UsersWrapper.class);
        if (loaded != null) {
            usersWrapper = loaded;
            System.out.println("[LOG] Loaded " + usersWrapper.getUsers().size() + " user(s) from JSON.");
        } else {
            System.out.println("[LOG] No data found. Initializing new UsersWrapper.");
            usersWrapper = new UsersWrapper(new ArrayList<>());
            saveUsers();
        }
    }

    public List<User> getAllUsers() {
        return usersWrapper.getUsers();
    }

    public void addUser(User user) {
        usersWrapper.getUsers().add(user);
        System.out.println("[LOG] Added new user: " + user.getUsername());
        saveUsers();
    }

    public void updateUser(User updatedUser) {
        List<User> users = usersWrapper.getUsers();
        boolean updated = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(updatedUser.getId())) {
                users.set(i, updatedUser);
                updated = true;
                System.out.println("[LOG] Updated user: " + updatedUser.getUsername());
                break;
            }
        }
        if (!updated) {
            System.out.println("[LOG] User with id " + updatedUser.getId() + " not found.");
        }
        saveUsers();
    }

    private void saveUsers() {
        saveData(usersWrapper);
    }
}
