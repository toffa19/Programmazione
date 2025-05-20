// src/main/java/com/progetto/repository/UserRepository.java
package com.progetto.repository;

import com.progetto.model.User;
import com.progetto.model.UsersWrapper;
import com.progetto.util.Config;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository utenti. Gestisce anche il campo "role".
 */
public class UserRepository extends AbstractRepository<UsersWrapper> {

    private UsersWrapper usersWrapper;

    public UserRepository() {
        super();
        loadUsers();
    }

    @Override
    protected String getFilePath() {
        return Config.USERS_JSON_PATH;
    }

    // src/main/java/com/progetto/repository/UserRepository.java
    private void loadUsers() {
        // 1) Provo a caricare dal classpath (/target/classes/users.json)
        try (InputStream is = getClass().getResourceAsStream(Config.USERS_JSON_PATH)) {
            if (is != null) {
                usersWrapper = Config.MAPPER.readValue(is, UsersWrapper.class);
                System.out.println("[LOG] Loaded users from classpath resource");
                return;
            }
            System.out.println("[WARN] Classpath users.json not found");
        } catch (Exception ex) {
            System.out.println("[ERROR] Failed parsing users.json from classpath");
            ex.printStackTrace();
        }

        // 2) Se non c'è, provo a caricare un file users.json nella working dir
        File ext = new File(Config.USERS_JSON_PATH);
        if (ext.exists()) {
            try {
                usersWrapper = Config.MAPPER.readValue(ext, UsersWrapper.class);
                System.out.println("[LOG] Loaded users from external file");
            } catch (Exception ex) {
                System.out.println("[ERROR] Failed parsing external users.json");
                ex.printStackTrace();
                usersWrapper = new UsersWrapper(new ArrayList<>());
            }
        } else {
            // 3) Non esiste: creo un wrapper vuoto e lo salvo subito su working dir
            usersWrapper = new UsersWrapper(new ArrayList<>());
            saveUsers();
            System.out.println("[LOG] Created new external users.json");
        }
    }

    public List<User> getAllUsers() {
        return usersWrapper.getUsers();
    }

    public void addUser(User user) {
        usersWrapper.getUsers().add(user);
        System.out.println("[LOG] Added new user: " + user.getUsername() + " (role=" + user.getRole() + ")");
        saveUsers();
    }

    public void updateUser(User updatedUser) {
        List<User> users = usersWrapper.getUsers();
        boolean found = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(updatedUser.getId())) {
                users.set(i, updatedUser);
                found = true;
                System.out.println("[LOG] Updated user: " + updatedUser.getUsername());
                break;
            }
        }
        if (!found) {
            System.out.println("[WARN] User id=" + updatedUser.getId() + " not found.");
        }
        saveUsers();
    }

    private void saveUsers() {
        try {
            File ext = new File(Config.USERS_JSON_PATH);
            Config.MAPPER.writerWithDefaultPrettyPrinter()
                    .writeValue(ext, usersWrapper);
            System.out.println("[LOG] Saved users to external file");
        } catch (Exception ex) {
            System.out.println("[ERROR] Failed writing users.json");
            ex.printStackTrace();
        }
    }

}