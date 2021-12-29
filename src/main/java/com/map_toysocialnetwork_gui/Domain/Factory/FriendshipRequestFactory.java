package com.map_toysocialnetwork_gui.Domain.Factory;

import com.map_toysocialnetwork_gui.Domain.FriendshipRequest;
import com.map_toysocialnetwork_gui.Domain.User;
import com.map_toysocialnetwork_gui.Repository.Repository;

public class FriendshipRequestFactory implements Factory<FriendshipRequest> {

    private static Repository friendshipRequestRepository = null;
    private static FriendshipRequestFactory instance =null;

    private FriendshipRequestFactory(){}

    public static FriendshipRequestFactory getInstance(Repository friendshipRequestRepo){
        if(friendshipRequestRepository==null){
            friendshipRequestRepository = friendshipRequestRepo;
        }
        if(instance ==null){
            instance =new FriendshipRequestFactory();
        }
        return instance;
    }
    @Override
    public FriendshipRequest createObject(String... attributes) {
        String userID1 = attributes[0];
        String userID2 = attributes[1];
        FriendshipRequest.FriendshipRequestID friendshipRequestID = new FriendshipRequest.FriendshipRequestID(userID1,userID2);
        User user1 = (User) friendshipRequestRepository.findOne(userID1);
        User user2 = (User) friendshipRequestRepository.findOne(userID2);
        FriendshipRequest friendshipRequest = new FriendshipRequest(user1,user2);
        friendshipRequest.setId(friendshipRequestID);
        return friendshipRequest;
    }
}
