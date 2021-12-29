package com.map_toysocialnetwork_gui.Repository.FileRepository;

import com.map_toysocialnetwork_gui.Domain.Friendship;
import com.map_toysocialnetwork_gui.Domain.User;

import java.util.*;

/**
 * Defines a repository that stores Friendship type objects into csv file.
 */
public class FriendshipFileRepository extends AbstractFileRepository<Friendship.FriendshipID, Friendship> {
    private UserFileRepository userRepo;

    /**
     * Constructor for a Friendship file repository.
     *
     * @param fileName name and path of the file in which the entities are stored.
     * @param userFileRepository reference to a User repository
     */
    public FriendshipFileRepository(String fileName, UserFileRepository userFileRepository) {
        super(fileName);
        this.userRepo=userFileRepository;
        populateFriendships();
    }

    private void populateFriendships() {
        for(Friendship friendship:findAll()) {
            User u1 = userRepo.findOne(friendship.getId().getUserID1());
            User u2 = userRepo.findOne(friendship.getId().getUserID2());
            friendship.setUser1(u1);
            friendship.setUser2(u2);
        }
    }

    @Override
    protected Friendship extractEntity(List<String> attributes) {
        String userID1 = attributes.get(0);
        String userID2 = attributes.get(1);
        Friendship friendship = new Friendship(null, null);
        friendship.setId(new Friendship.FriendshipID(userID1, userID2));
        return friendship;
    }

    @Override
    protected String createEntityAsString(Friendship entity) {
        String friendshipString = "";
        Friendship.FriendshipID friendshipID = entity.getId();
        friendshipString += friendshipID.getUserID1() + ";" + friendshipID.getUserID2();
        return friendshipString;
    }

    @Override
    public Friendship update(Friendship entity) {
        return null;
    }
}
