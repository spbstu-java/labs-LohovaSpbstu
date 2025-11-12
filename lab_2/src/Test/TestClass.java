package Test;

import Annotations.*;

public class TestClass {
    public static void publicNotAnnotated(String text) {}

    @RepeatCall(2)
    public static void publicAnnotated(int a, int b) {
        System.out.println("Публичный метод: " + a + ", " + b);
    }

    protected static void protectedNotAnnotated(double value) {}

    @RepeatCall(3)
    protected static void protectedAnnotated(String first, String second) {
        System.out.println("  Защищенный метод: " + String.valueOf(first) + ", " + String.valueOf(second));
    }

    // Приватные методы
    private static void privateNotAnnotated(int value, String text) {}

    @RepeatCall(2)
    private static void privateAnnotated(double number, String text1, String text2) {
        System.out.println("  Приватный метод: " + number + ", " + text1 + ", " + text2);
    }
}
