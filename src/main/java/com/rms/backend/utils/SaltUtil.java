package com.rms.backend.utils;

import java.util.Random;

public class SaltUtil {
    private SaltUtil(){}
    public static String createSalt(Integer length){
        char[] charArray = "qwertyuiopasdfghjklzxcvbnm,.;1234567890QWERTYUIOPASDFGHJKLZXCVBNM".toCharArray();
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer integer = 0; integer < length; integer++) {
            int index = random.nextInt(charArray.length);
            stringBuilder.append(charArray[index]);
        }
        return stringBuilder.toString();
    }
}
