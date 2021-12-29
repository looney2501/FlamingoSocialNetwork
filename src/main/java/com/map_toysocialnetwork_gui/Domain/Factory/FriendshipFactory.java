package com.map_toysocialnetwork_gui.Domain.Factory;

import com.map_toysocialnetwork_gui.Domain.Friendship;
import com.map_toysocialnetwork_gui.Domain.User;
import com.map_toysocialnetwork_gui.Repository.Repository;

public class FriendshipFactory implements Factory<Friendship> {

    private static Repository userFileRepository = null;
    private static FriendshipFactory instance = null;

    private FriendshipFactory() {}

    public static FriendshipFactory getInstance(Repository userFileRepo) {
        if (userFileRepository==null) {
            userFileRepository = userFileRepo;
        }
        if (instance == null) {
            instance = new FriendshipFactory();
        }
        return instance;
    }

    @Override
    public Friendship createObject(String... attributes) {
        String userID1 = attributes[0];
        String userID2 = attributes[1];
        Friendship.FriendshipID friendshipID= new Friendship.FriendshipID(userID1, userID2);
        User user1 = (User) userFileRepository.findOne(userID1);
        User user2 = (User) userFileRepository.findOne(userID2);
        Friendship friendship = new Friendship(user1, user2);
        friendship.setId(friendshipID);
        return friendship;
    }
}
