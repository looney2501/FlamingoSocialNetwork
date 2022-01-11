package com.map_toysocialnetwork_gui.Domain;

import com.map_toysocialnetwork_gui.Domain.DTO.Conversation;
import com.map_toysocialnetwork_gui.Domain.DTO.FriendDTO;
import com.map_toysocialnetwork_gui.Service.Service;
import com.map_toysocialnetwork_gui.Utils.Observer.Observable;
import com.map_toysocialnetwork_gui.Utils.Observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Defines the page of a logged user.
 */
public class Page implements Observable {
    private User user;
    private List<FriendDTO> friends;
    private List<FriendshipRequest> sentFriendRequests;
    private List<FriendshipRequest> receivedFriendRequests;
    private List<Conversation> conversations;
    private Service service;
    private List<Observer> observers;

    /**
     * Creates the page for a given username and loads the data associated with it.
     * @param username String representing the username of the user.
     * @param service Service of the app.
     */
    public Page(String username, Service service) {
        this.service = service;
        this.user = service.findUser(username);
        refreshPage();
    }

    /**
     * Reloads all data from database to page.
     */
    public void refreshPage() {
        friends = new ArrayList<>(service.getFriendsOfUser(user.getId()));
        sentFriendRequests = new ArrayList<>(service.getAllFriendRequestsFrom(user.getId()));
        receivedFriendRequests = new ArrayList<>(service.getAllFriendRequestsFor(user.getId()));
        conversations = new ArrayList<>(service.getAllConversationsFor(user));
    }

    public User getUser() {
        return user;
    }

    public List<FriendDTO> getFriends() {
        return friends;
    }

    public List<FriendshipRequest> getSentFriendRequests() {
        return sentFriendRequests;
    }

    public List<FriendshipRequest> getReceivedFriendRequests() {
        return receivedFriendRequests;
    }

    public List<Conversation> getConversations() {
        return conversations;
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }
}
