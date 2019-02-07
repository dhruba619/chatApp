package org.tomlang.livechat.util;

import java.util.Base64;
import java.util.Date;
import java.util.Random;

public class PasswordEncoder {

    public static String createRandomPasswordSalt() {
        Random rand = new Random((new Date()).getTime());
        byte[] salt = new byte[8];
        rand.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
    
    public static String encodePassword(String password, String salt) {
        byte[] saltBytes = salt.getBytes();
        byte[] passwordBytes = password.getBytes();
        
        return Base64.getEncoder().encodeToString(saltBytes)+Base64.getEncoder().encodeToString(passwordBytes);
    }
    
    
}
