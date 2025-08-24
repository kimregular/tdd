package com.example.tdd;

import java.util.UUID;

public class PasswordGenerator {

    public static String generatePassword() {
        return "password" + UUID.randomUUID();
    }
}
