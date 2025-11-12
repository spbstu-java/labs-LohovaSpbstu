package Processor;

import Annotations.RepeatCall;
import Test.TestClass;

import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.lang.reflect.Method;
import java.util.Arrays;

public class MethodProcessor {

    private static Object createDefaultValue(Class<?> type) {
        if (type.isArray()) {
            return Array.newInstance(type.getComponentType(), 0);
        }
        return switch (type.getName()) {
            case "int" -> 0;
            case "boolean" -> false;
            case "double" -> 0.0;
            case "long" -> 0L;
            case "float" -> 0.0f;
            case "short" -> (short) 0;
            case "byte" -> (byte) 0;
            case "char" -> '\u0000';
            default -> null;
        };
    }

    private static Object createParameter(Class<?> type) {
        try {
            if (type.isPrimitive()) return createDefaultValue(type);

            if (type == String.class) {
                return "null";
            }

            var constructor = type.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            System.out.println("Ошибка! не удалось создать параметр типа: " + type.getName());
            return null;
        }
    }

    public static void processAnnotatedMethods(Class<?> targetClass) {
        System.out.println("Обработка класса: " + targetClass.getSimpleName());

        for (Method method : targetClass.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(RepeatCall.class)) continue;

            if (Modifier.isPublic(method.getModifiers())) {
                System.out.println("Пропущен публичный метод: " + method.getName());
                continue;
            }

            Object[] parameters = Arrays.stream(method.getParameterTypes())
                    .map(MethodProcessor::createParameter)
                    .toArray();

            try {
                method.setAccessible(true);
                int callCount = method.getAnnotation(RepeatCall.class).value();

                System.out.println("\nМетод: " + method.getName() + " (вызовов: " + callCount + ")");

                for (int i = 0; i < callCount; i++) {
                    method.invoke(null, parameters);
                    System.out.println("Вызов #" + (i + 1) + " выполнен");
                }
            } catch (Exception e) {
                System.out.println("Ошибка вызова метода " + method.getName() + ": " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        processAnnotatedMethods(TestClass.class);
    }
}