package be.better.at.processing.util;

import java.util.UUID;

public class JpaUtil {

    public static String generateId() {
        return UUID.randomUUID().toString();
    }
}
