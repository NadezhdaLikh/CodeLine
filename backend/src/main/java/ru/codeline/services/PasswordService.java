package ru.codeline.services;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class PasswordService {

    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String OTHER_CHAR = "!@#$%&*()_+-=[]?";

    private static final String PASSWORD_ALLOW_BASE = CHAR_LOWER + CHAR_UPPER + NUMBER + OTHER_CHAR;
    private static final SecureRandom random = new SecureRandom();

    public static String generateRandomPassword(int length) {
        if (length < 4) {
            throw new IllegalArgumentException("Password length must be at least 4 characters.");
        }

        StringBuilder password = new StringBuilder(length);

        // at least one lowercase char
        password.append(CHAR_LOWER.charAt(random.nextInt(CHAR_LOWER.length())));

        // at least one uppercase char
        password.append(CHAR_UPPER.charAt(random.nextInt(CHAR_UPPER.length())));

        // at least one digit
        password.append(NUMBER.charAt(random.nextInt(NUMBER.length())));

        // at least one special character
        password.append(OTHER_CHAR.charAt(random.nextInt(OTHER_CHAR.length())));

        // fill in the rest of the password
        for (int i = 4; i < length; i++) {
            password.append(PASSWORD_ALLOW_BASE.charAt(random.nextInt(PASSWORD_ALLOW_BASE.length())));
        }

        return shuffleString(password.toString());
    }

    // shuffle password characters
    private static String shuffleString(String input) {
        char[] charArray = input.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            int randomIndex = random.nextInt(charArray.length);
            char temp = charArray[i];
            charArray[i] = charArray[randomIndex];
            charArray[randomIndex] = temp;
        }
        return new String(charArray);
    }
}
