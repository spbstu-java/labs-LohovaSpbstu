package Translator;

import Exceptions.FileReadException;
import Exceptions.InvalidFileFormatException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Scanner;



public final class Translator {
    private final Map<String, String> dictionary = new HashMap<>();
    private final Set<String> unknownWords = new HashSet<>();

    public void loadDictionaryFromFile(String filePath) throws InvalidFileFormatException, FileReadException {
        try {
            final var lines = Files.readAllLines(Paths.get(filePath));
            if (lines.isEmpty()) {
                throw new InvalidFileFormatException("Файл словаря пуст");
            }
            for (final var line : lines) {
                if (line.trim().isEmpty()) continue;

                final var parts = line.split("\\|");
                if (parts.length != 2)
                    throw new InvalidFileFormatException(
                            "Неверный формат в строке: " + line + ". Ожидается 'слово | перевод'");

                final var word = parts[0].trim();
                final var translation = parts[1].trim();

                if (word.isEmpty() || translation.isEmpty()) {
                    throw new InvalidFileFormatException("Пустое слово или перевод в строке: " + line);
                }
                dictionary.put(word.toLowerCase(), translation);
            }
        } catch (IOException e) {
            throw new FileReadException("Не удалось прочитать файл: " + e.getMessage());
        }
    }

    private String findLongestMatchingPhrase(String[] words, int start) {
        String longestMatchingPhrase = null;
        final var currentPhrase = new StringBuilder();

        for (var i = start; i < words.length; i++) {
            if (!currentPhrase.isEmpty()) {
                currentPhrase.append(" ");
            }
            String cleanWord = cleanWord(words[i]);
            currentPhrase.append(cleanWord.toLowerCase());

            final var phrase = currentPhrase.toString();
            if (dictionary.containsKey(phrase)) {
                longestMatchingPhrase = phrase;
            }
        }

        return longestMatchingPhrase;
    }

    private String cleanWord(String word) {
        if (word == null || word.isEmpty()) {
            return word;
        }

        return word.replaceAll("^[^a-zA-Zа-яА-Я0-9]+|[^a-zA-Zа-яА-Я0-9]+$", "");
    }

    private boolean isCleanWord(String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }
        return word.matches("[a-zA-Zа-яА-Я0-9]+");
    }

    public String translate(String inputText) {
        if (inputText == null || inputText.trim().isEmpty()) {
            return inputText;
        }

        final var words = inputText.split("\\s+");
        final var translationResult = new StringBuilder();

        int i = 0;
        while (i < words.length) {
            final var longestMatchingPhrase = findLongestMatchingPhrase(words, i);
            if (longestMatchingPhrase != null) {
                translationResult.append(dictionary.get(longestMatchingPhrase));
                i += longestMatchingPhrase.split("\\s+").length;
            } else {

                final String cleanWord = cleanWord(words[i]);

                if (!cleanWord.isEmpty() && isCleanWord(cleanWord) && !dictionary.containsKey(cleanWord.toLowerCase())) {
                    unknownWords.add(cleanWord.toLowerCase());
                }

                translationResult.append(words[i]);
                i++;
            }

            if (i < words.length) {
                translationResult.append(" ");
            }
        }

        return translationResult.toString();
    }

    public void saveUnknownWords() {
        if (unknownWords.isEmpty()) {
            return;
        }

        try {
            Files.createDirectories(Paths.get("output"));
            final var outputPath = Paths.get("output/unknown_words.txt");

            Set<String> existingWords = new HashSet<>();
            if (Files.exists(outputPath)) {
                final var existingLines = Files.readAllLines(outputPath);
                for (String line : existingLines) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.split("\\|");
                    if (parts.length > 0) {
                        String existingWord = parts[0].trim();
                        if (!existingWord.isEmpty()) {
                            existingWords.add(existingWord);
                        }
                    }
                }
            }

            final var lines = new java.util.ArrayList<String>();
            for (String word : unknownWords) {
                if (!existingWords.contains(word)) {
                    lines.add(word + " | ");
                }
            }

            if (!lines.isEmpty()) {
                Files.write(outputPath, lines,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND);
                System.out.println("Добавлено новых непереведенных слов: " + lines.size());
            } else {
                System.out.println("Все слова уже есть в файле unknown_words.txt");
            }

            System.out.println("Непереведенные слова обновлены в: output/unknown_words.txt");

        } catch (IOException e) {
            System.err.println("Ошибка при сохранении непереведенных слов: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        final var scanner = new Scanner(System.in);
        final var translator = new Translator();

        try {
            translator.loadDictionaryFromFile("input/dictionary.txt");
            System.out.println("Введите текст для перевода ('выход' или 'exit' для завершения):");

            while (true) {
                System.out.print("> ");
                final var inputText = scanner.nextLine();

                if ("выход".equalsIgnoreCase(inputText) || "exit".equalsIgnoreCase(inputText)) {
                    translator.saveUnknownWords();
                    System.out.println("До свидания!");
                    break;
                }

                if (inputText.trim().isEmpty()) {
                    continue;
                }

                final var translation = translator.translate(inputText);
                System.out.println("Перевод: " + translation);
            }
        } catch (InvalidFileFormatException | FileReadException e) {
            System.err.println("Ошибка: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}