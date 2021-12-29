package com.map_toysocialnetwork_gui.Repository.FileRepository;

import com.map_toysocialnetwork_gui.Domain.FriendshipRequest;
import com.map_toysocialnetwork_gui.Domain.User;

import java.util.List;

/**
 * Defines a repository for friendship requests that can store data in a file
 */
public class FriendshipRequestFileRepository extends AbstractFileRepository<FriendshipRequest.FriendshipRequestID,FriendshipRequest>{
    private UserFileRepository userFileRepository;
    /**
     * Constructor for an abstract file repository.
     *
     * @param fileName name and path of the file in which the entities are stored.
     * @param userFileRepository reference to a friendship request repository
     */
    public FriendshipRequestFileRepository(String fileName,UserFileRepository userFileRepository) {
        super(fileName);
        this.userFileRepository = userFileRepository;
        loadFriendsRequests();
    }

    private void loadFriendsRequests(){
        for (FriendshipRequest friendshipRequest : findAll()){
            User user = userFileRepository.findOne(friendshipRequest.getId().getUsernameSender());
            User user2 = userFileRepository.findOne(friendshipRequest.getId().getUsernameReceiver());
            friendshipRequest.setUserSender(user);
            friendshipRequest.setUserReceiver(user2);
        }
    }
    @Override
    protected FriendshipRequest extractEntity(List<String> attributes) {
        String userID1 = attributes.get(0);
        String userID2 = attributes.get(1);
        FriendshipRequest friendshipRequest = new FriendshipRequest(null,null);
        friendshipRequest.setId(new FriendshipRequest.FriendshipRequestID(userID1,userID2));
        return friendshipRequest;
    }

    @Override
    protected String createEntityAsString(FriendshipRequest entity) {
        String friendshipRequestString  = "";
        FriendshipRequest.FriendshipRequestID friendshipRequestID= entity.getId();
        friendshipRequestString += friendshipRequestID.getUsernameSender()+";"+friendshipRequestID.getUsernameReceiver();
        return friendshipRequestString;
    }
}
