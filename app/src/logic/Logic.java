package logic;

import exception.FileIsNotFoundException;
import exception.ReadingFileException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
//import java.util.concurrent.atomic.AtomicInteger;

public class Logic {
    /**
     * Номер тест-файла
     */
    //private static final AtomicInteger numberOfTestFile = new AtomicInteger(0);

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
            //Logic.printResult(checkResult, sourceResult, expectedResult);
            Logic.printResult(numberOfTestFile, checkResult, sourceResult, expectedResult);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    /**
     * Возвращает результат входного значения
     * @param inputString входное значение
     * @return входное значение
     */
    public static String logic(String inputString) {
        return inputString;
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
        //numberOfTestFile.incrementAndGet();
        System.out.println("Тест файл " + numberOfTestFile + " результат " + (isOk ? "OK" : "Failed") + ":");
        System.out.println("Результат:");
        System.out.println(sourceResult);

        if (!isOk) {
            System.out.println("Ожидали:");
            System.out.println(expectedResult);
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