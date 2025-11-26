package Stream;

import java.util.*;
import java.util.stream.Collectors;

public class StreamAPI {

    public static double average(List<Integer> numbers){
        return numbers.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElseThrow(() -> new  IllegalArgumentException("Список пуст"));
    }

    public static List<String> addPrefix(List<String> strings) {
        return strings.stream()
                .map(s -> "_new_" + s.toUpperCase())
                .collect(Collectors.toList());
    }

    public static List<Integer> uniqueSquares(List<Integer> numbers) {
        Map<Integer, Long> freq = numbers.stream()
                .collect(Collectors.groupingBy(n -> n, Collectors.counting()));

        return numbers.stream()
                .filter(n -> freq.get(n) == 1)
                .map(n -> n * n)
                .collect(Collectors.toList());
    }

    public static <T> T last(Collection<T> collection) {
        return collection.stream()
                .reduce((first, second) -> second)
                .orElseThrow(() -> new NoSuchElementException("Коллекция пуста"));
    }

    public static int sumEven(int[] numbers) {
        return Arrays.stream(numbers)
                .filter(n -> n % 2 == 0)
                .sum();
    }

    public static Map<Character, String> toMap (List<String> strings) {
        return strings.stream()
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toMap(
                        s -> s.charAt(0),
                        s -> s.length() > 1 ? s.substring(1) : "",
                        (oldVal, newVal) -> oldVal
                ));
    }

    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 2, 3, 4, 4, 5);
        List<String> words = Arrays.asList("cat", "dog", "cow", "mouse", "duck", "snake");
        int[] arr = {1, 2, 3, 4, 5, 6};

        System.out.println("Среднее значение списка целых чисел: " + average(numbers));
        System.out.println("Строки в верхнем регистре с префиксом _new_: " + addPrefix(words));
        System.out.println("Квадраты всех встречающихся только один раз элементов: " + uniqueSquares(numbers));
        System.out.println("Последний элемент коллекции: " + last(numbers));
        try {
            last(new ArrayList<>());
        } catch (Exception e) {
            System.out.println("Исключение при пустой коллекции: " + e.getMessage());
        }
        System.out.println("Сумма чётных чисел массива: " + sumEven(arr));
        int[] noEvens = {1, 3, 5, 7};
        System.out.println("Сумма чётных чисел массива ( в массиве только нечётные): " + sumEven(noEvens));
        System.out.println("Map из строк: " + toMap(words));
    }
}