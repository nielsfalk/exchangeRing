package de.hh.changeRing.user;

import java.util.ArrayList;

import static com.google.common.collect.Lists.newArrayList;

public class UserUpdateEvent {

    private final ArrayList<User> users;

    public UserUpdateEvent(User... users) {
        this.users = newArrayList(users);
    }

    public boolean regards(User user) {
        return users.contains(user);
    }
}
