package uz.mk.apphrmanagement.utils;


import java.util.Random;

public class CommonUtils {

    public static Integer generateCode() {
        return new Random().nextInt((999999 - 100000) + 1) + 100000;
    }
}
