package logic;

import exception.FileIsNotFoundException;
import exception.ReadingFileException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Logic {
    /**
     * Русские гласные буквы
     */
    private static final String VOWELS_RU = "аеёиоуыэюяАЕЁИОУЫЭЮЯ";

    /**
     * Обрабатывает тест-файл
     * @param numberOfTestFile номер тест-файла
     * @param inputFile исходный тест-файл
     * @param outputFile ожидаемый тест-файл
     */
    public static void processTestFile(int numberOfTestFile, String inputFile, String outputFile) {
        try {
            String sourceResult = Logic.getInput(Path.of(inputFile));
            String expectedResult = Logic.getInput(Path.of(outputFile));
            boolean checkResult = Logic.checkResult(sourceResult, expectedResult);
            Logic.printResult(numberOfTestFile, checkResult, sourceResult, expectedResult);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    /**
     * Считает в каждой строке количество английских букв, русских гласных букв и других символов
     * @param inputString входное значение
     * @return результат подсчета
     */
    public static String logic(String inputString) {
        String[] lines = inputString.split("\n");
        StringBuilder result = new StringBuilder();

        // проходимся по каждой строке
        for (String line: lines) {
            int countEngChar = 0; // кол-во английских букв
            int countRuVowels = 0; // кол-во русских гласных
            int otherSymbols = 0; // кол-во других символов

            char[] charArray = line.toCharArray();
            // проходимся по каждому символу строки
            for (char c: charArray) {
                if (isEnglishLetterRegex(c)) {
                    countEngChar += 1;
                } else if (isRussianVowel(c)) {
                    countRuVowels += 1;
                } else {
                    otherSymbols += 1;
                }
            }
            result.append("Английских: ").append(countEngChar).append(", ")
                    .append("Русских гласных: ").append(countRuVowels).append(", ")
                    .append("Другие символы ").append(otherSymbols).append("\n");
        }

        // Удаляем последний перевод строки, если нужно
        if (result.length() > 0) {
            result.setLength(result.length() - 1);
        }

        return result.toString();
    }

    /**
     * Проверяет, является ли символ русской гласной буквой
     * @param c символ
     * @return true, если символ является русской гласной буквой
     */
    public static boolean isRussianVowel(char c) {
        return VOWELS_RU.indexOf(c) != -1;
    }

    /**
     * Проверяет, является ли символ английской буквой
     * @param c символ
     * @return true, если символ является английской буквой
     */
    public static boolean isEnglishLetterRegex(char c) {
        return String.valueOf(c).matches("[a-zA-Z]");
    }

    /**
     * Проверяет путь текстового файла и если файл существует, то возвращает его содержимое
     * @param path путь к текстовому файлу
     * @return содержимое текстового файла
     */
    public static String getInput(Path path) {
        if (!Files.isRegularFile(path)) {
            throw new FileIsNotFoundException(path);
        } else {
            return getContentFromFile(path);
        }
    }

    /**
     * Получает содержимое файла
     * @param path путь до файла
     * @return содержимое файла
     */
    public static String getContentFromFile(Path path) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
            // Удаляем последний перенос строки
            if (content.length() > 0) {
                content.setLength(content.length() - 1);
            }
        } catch (IOException e) {
            throw new ReadingFileException(e, path);
        }
        return content.toString();
    }

    /**
     * Выводит результат
     * @param numberOfTestFile номер тест-файла
     * @param isOk пройдена ли проверка на эквивалентность исходного и ожидаемого результата
     * @param sourceResult исходный результат
     * @param expectedResult ожидаемый результат
     */
    public static void printResult(int numberOfTestFile, boolean isOk, String sourceResult, String expectedResult) {
        System.out.println("Тест файл " + numberOfTestFile + " результат " + (isOk ? "OK" : "Failed") + ":");
        String[] lines = sourceResult.split("\n");

        for (String line: lines) {
            System.out.println("Строка: " + line);
        }
        System.out.println("Результат:");
        System.out.println(logic(sourceResult));

        if (!isOk) {
            System.out.println("Ожидали:");
            System.out.println(logic(expectedResult));
        }
    }

    /**
     * Сверяет исходный результат с ожидаемым
     * @param sourceResult исходный результат
     * @param expectedResult ожидаемый результат
     * @return статус равенства исходного и ожидаемого результата
     */
    public static boolean checkResult(String sourceResult, String expectedResult) {
        return sourceResult.equals(expectedResult);
    }
}