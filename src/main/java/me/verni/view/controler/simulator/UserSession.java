package me.verni.view.controler.simulator;

import me.verni.user.User;

public class UserSession {
    private static User currentUser;

    public static User getUser() {
        return currentUser;
    }

    public static void setUser(User user) {
        currentUser = user;
    }
}