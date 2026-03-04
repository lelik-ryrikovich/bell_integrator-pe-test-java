package logic;

import exception.ElementHasExceededPermissibleValueException;
import exception.FileIsNotFoundException;
import exception.IncorrectArraySizeException;
import exception.ReadingFileException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class Logic {

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
        int[][] sourceArr = transformToIntArray(inputString);
        int[][] resultArr = calculateSumOfNeighbors(sourceArr);
        return arrayToString(resultArr);
    }

    /**
     * Преобразовывает двумерный массив чисел в строку (нужно для вывода)
     * @param array двумерный массив
     * @return преобразованный в строку двумерный массив
     */
    public static String arrayToString(int[][] array) {
        StringBuilder sb = new StringBuilder();
        for (int[] row: array) {
            sb.append(Arrays.toString(row)
                            .replace(",", "")
                            .replace("[", "")
                            .replace("]", ""))
                    .append("\n");
        }
        String result = sb.toString();
        // Удаляем последний перенос строки
        if (result.length() > 0) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    /**
     * Считает сумму соседних элементов у каждого элемента двумерного массива
     * @param sourceArr исходный двумерный массив
     * @return двумерный массив результата
     */
    public static int[][] calculateSumOfNeighbors(int[][] sourceArr) {
        int N = sourceArr.length; // кол-во строк
        int M = sourceArr[0].length; // кол-во столбцов
        int [][] resultArr = new int[N][M];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                int sum = 0;
                if (i > 0) sum += sourceArr[i-1][j]; // Проверяем соседа СВЕРХУ
                if (i < N-1) sum += sourceArr[i+1][j]; // Проверяем соседа СНИЗУ
                if (j > 0) sum += sourceArr[i][j-1]; // Проверяем соседа СЛЕВА
                if (j < M-1) sum += sourceArr[i][j+1]; // Проверяем соседа СПРАВА

                resultArr[i][j] = sum;
            }
        }
        return resultArr;
    }

    /**
     * Преобразует содержание тестового файла в двумерный массив чисел
     * @param inputString содержание тестового файла
     */
    public static int[][] transformToIntArray(String inputString) {
        String[] lines = inputString.split("\n");

        int N = lines.length; // кол-во строк
        int M = lines[0].split(" ").length; // кол-во столбцов (берем по первой строчке)
        int[][] sourceArray = new int[N][M]; // размер исходного массива

        // Проходим по каждой строчке
        for (int i = 0; i < N; i++) {
            // Разделяем строчку по пробелам и формируем массив
            String[] lineElementsArray = lines[i].split(" ");

            // Проверка, что массив соответствует размеру NxM
            if (lineElementsArray.length != M) {
                throw new IncorrectArraySizeException(N, M);
            }

            // Проходимся по каждому элементу строки
            for (int j = 0; j < M; j++) {
                // Пытаемся спарсить числовое значение элемента
                int element = Integer.parseInt(lineElementsArray[j]);
                // Если значение элемента не входит в допустимый диапазон [1;9], то бросаем исключение
                if (element < 1 || element > 9) {
                    throw new ElementHasExceededPermissibleValueException(element);
                } else {
                    sourceArray[i][j] = element;
                }
            }
        }
        return sourceArray;
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
        String logicSourceResult; // результат метода logic на sourceResult
        String logicExpectedResult;  // результат метода logic на expectedResult
        logicSourceResult = logic(sourceResult);
        logicExpectedResult = logic(expectedResult);

        System.out.println("Тест файл " + numberOfTestFile + " результат " + (isOk ? "OK" : "Failed") + ":");
        System.out.println("Входной файл:");
        System.out.println(sourceResult);

        System.out.println("\nРезультат:");
        System.out.println(logicSourceResult);

        if (!isOk) {
            System.out.println("Ожидали:");
            System.out.println(logicExpectedResult);
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