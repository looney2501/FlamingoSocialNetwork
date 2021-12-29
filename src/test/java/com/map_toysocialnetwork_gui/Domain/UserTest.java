package com.map_toysocialnetwork_gui.Domain;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @org.junit.jupiter.api.Test
    void getUsername() {
        User u1 = new User("fname1", "lname1");
        u1.setId("u1");
        assertEquals(u1.getId(), "u1");
    }

    @org.junit.jupiter.api.Test
    void getFirstName() {
        User u1 = new User("fname1", "lname1");
        assertEquals(u1.getFirstName(), "fname1");
    }

    @org.junit.jupiter.api.Test
    void getLastName() {
        User u1 = new User("fname1", "lname1");
        assertEquals(u1.getLastName(), "lname1");
    }

    @org.junit.jupiter.api.Test
    void testEquals() {
        User u1 = new User("fname1", "lname1");
        u1.setId("u1");
        User u2 = new User("fname2", "lname2");
        u2.setId("u2");
        User u3 = new User("fname3", "lname3");
        u3.setId("u1");
        assertNotEquals(u1,u2);
        assertEquals(u1,u3);
    }
}