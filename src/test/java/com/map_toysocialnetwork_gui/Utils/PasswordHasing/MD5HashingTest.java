package com.map_toysocialnetwork_gui.Utils.PasswordHasing;

import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class MD5HashingTest {

    @Test
    void hashPassword() throws NoSuchAlgorithmException {
        String hashedPassword = MD5Hashing.hashPassword("aaa");
        assertEquals(hashedPassword, MD5Hashing.hashPassword("aaa"));
        assertNotEquals(hashedPassword, MD5Hashing.hashPassword("Aaa"));
    }
}