package com.progetto.viewmodel;

import com.progetto.model.User;
import com.progetto.repository.UserRepository;
import com.progetto.util.SessionManager;
import java.util.ArrayList;

public class EditorProfileViewModel {
    private final UserRepository userRepository;
    private User currentUser;

    public EditorProfileViewModel() {
        userRepository = new UserRepository();
        loadUserData();
    }

    public void loadUserData() {
        String loggedUserId = SessionManager.getCurrentUserId();
        currentUser = null;
        for (User user : userRepository.getAllUsers()) {
            if (user.getId() != null && user.getId().equals(loggedUserId)) {
                currentUser = user;
                break;
            }
        }
        if (currentUser == null) {
            currentUser = new User();
            currentUser.setProgress(new ArrayList<>());
        }
    }

    public String getFullName() {
        return currentUser.getFirstName() != null ? currentUser.getFirstName() : "";
    }
    public void setFullName(String fullName) {
        currentUser.setFirstName(fullName);
    }

    public String getUsername() {
        return currentUser.getUsername() != null ? currentUser.getUsername() : "";
    }

    public void setUsername(String username) {
        currentUser.setUsername(username);
    }



    public String getPassword() {
        return currentUser.getPassword() != null ? currentUser.getPassword() : "";
    }
    public void setPassword(String password) {
        currentUser.setPassword(password);
    }

    public String getEmail() {
        return currentUser.getEmail() != null ? currentUser.getEmail() : "";
    }

    public void saveUserData() {
        userRepository.updateUser(currentUser);
        System.out.println("Saving user data: "
                + getFullName() + ", "
                + getUsername());
    }
}