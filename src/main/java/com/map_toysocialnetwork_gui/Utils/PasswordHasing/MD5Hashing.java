package com.map_toysocialnetwork_gui.Utils.PasswordHasing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public interface MD5Hashing {
    static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.update(password.getBytes());
        byte[] bytes = m.digest();
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b:bytes) {
            stringBuffer.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return stringBuffer.toString();
    }
}
