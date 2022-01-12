package com.map_toysocialnetwork_gui.Domain.Factory;

import com.map_toysocialnetwork_gui.Domain.User;

public class UserFactory implements Factory<User> {

    private static UserFactory instance = null;

    private UserFactory() {}

    public static UserFactory getInstance() {
        if (instance==null) {
            instance = new UserFactory();
        }
        return instance;
    }

    @Override
    public User createObject(String... attributes) {
        User u = new User(attributes[1], attributes[2], attributes[3]);
        u.setId(attributes[0]);
        return u;
    }
}
