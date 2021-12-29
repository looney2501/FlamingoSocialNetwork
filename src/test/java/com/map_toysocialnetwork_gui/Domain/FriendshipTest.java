package com.map_toysocialnetwork_gui.Domain;

import com.map_toysocialnetwork_gui.Domain.Factory.UserFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FriendshipTest {

    @Test
    void getUsers() {
        UserFactory userFactory = UserFactory.getInstance();
        User user1 = userFactory.createObject("id1", "fn1", "ln1");
        User user2 = userFactory.createObject("id2", "fn2", "ln2");
        Friendship friendship = new Friendship(user1, user2);
        User fu1 = friendship.getUser1();
        assertEquals(fu1.getId(), "id1");
        assertEquals(fu1.getFirstName(), "fn1");
        assertEquals(fu1.getLastName(), "ln1");
        user2.setFirstName("Gigel");
        assertEquals(friendship.getUser2().getFirstName(), "Gigel");
    }

    @Test
    void testIDandEquals() {
        UserFactory userFactory = UserFactory.getInstance();
        User user1 = userFactory.createObject("id1", "fn1", "ln1");
        User user2 = userFactory.createObject("id2", "fn2", "ln2");
        Friendship friendship = new Friendship(user1, user2);
        friendship.setId(new Friendship.FriendshipID(user1.getId(), user2.getId()));
        assertEquals(friendship.getId().getUserID1(), "id1");
        assertEquals(friendship.getId().getUserID2(), "id2");
        User user3 = userFactory.createObject("id1", "fn1", "ln1");
        User user4 = userFactory.createObject("id2", "fn2", "ln2");
        Friendship friendship2 = new Friendship(user4, user3);
        friendship2.setId(new Friendship.FriendshipID(user3.getId(), user4.getId()));
        assertEquals(friendship, friendship2);
    }

    @Test
    void testCreateFriendshipRequest() {
        UserFactory userFactory = UserFactory.getInstance();
        User user1 = userFactory.createObject("ana1", "Ana", "Maria");
        User user2 = userFactory.createObject("diana1", "Diana", "Maria");
        FriendshipRequest friendshipRequest = new FriendshipRequest(user1, user2);
        friendshipRequest.setId(new FriendshipRequest.FriendshipRequestID(user1.getId(), user2.getId()));
        assertEquals(friendshipRequest.getId().getUsernameSender(), "ana1");
        assertEquals(friendshipRequest.getId().getUsernameReceiver(), "diana1");
        friendshipRequest.setStatus("pending");
        assertEquals(friendshipRequest.getStatus(), "pending");
        User user3 = userFactory.createObject("dan", "Dan", "Pop");
        User user4 = userFactory.createObject("nina", "Nina", "Maria");
        FriendshipRequest friendshipRequest1 = new FriendshipRequest(user4, user3);
        friendshipRequest1.setStatus("approved");
        friendshipRequest1.setId(new FriendshipRequest.FriendshipRequestID(user4.getId(), user3.getId()));
        assertNotEquals(friendshipRequest, friendshipRequest1);
    }
}