package com.map_toysocialnetwork_gui.Domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntityTest {

    @Test
    void getId() {
        Entity<Integer> e1 = new Entity<>();
        e1.setId(12);
        assertEquals(e1.getId(),12);
    }

    @Test
    void setId() {
        Entity<Integer> e1 = new Entity<>();
        e1.setId(12);
        assertEquals(e1.getId(),12);
    }

    @Test
    void equals() {
        Entity<String> e1 = new Entity<>();
        e1.setId("user1");
        Entity<String> e2 = new Entity<>();
        e2.setId("user1");
        assertEquals(e1,e2);
        e2.setId("user2");
        assertNotEquals(e1,e2);
    }
}