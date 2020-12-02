package io.github.qszhu.database;

import java.util.Random;
import java.util.UUID;

public class Util {
    public static String randomString(int len) {
        return new Random().ints(97, 123)
                .limit(len)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static int randomInt(int upper) {
        return new Random().nextInt(upper);
    }

    public static String newId() {
        return UUID.randomUUID().toString();
    }
}
