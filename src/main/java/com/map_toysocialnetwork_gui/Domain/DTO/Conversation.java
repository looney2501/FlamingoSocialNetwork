package com.map_toysocialnetwork_gui.Domain.DTO;

import com.map_toysocialnetwork_gui.Domain.Message;
import com.map_toysocialnetwork_gui.Domain.User;

import java.util.List;

/**
 * Defines the relation between a thread of messages and the users involved.
 */
public class Conversation {
    private List<Message> allMessages;
    private List<User> allUsers;

    public Conversation(List<Message> allMessages, List<User> allUsers) {
        this.allMessages = allMessages;
        this.allUsers = allUsers;
    }

    public List<Message> getAllMessages() {
        return allMessages;
    }

    public List<User> getAllUsers() {
        return allUsers;
    }
}
