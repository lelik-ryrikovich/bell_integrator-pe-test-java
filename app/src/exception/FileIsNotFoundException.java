package exception;

import java.nio.file.Path;

/**
 * Исключение о ненахождении файла
 */
public class FileIsNotFoundException extends AppException {

    public FileIsNotFoundException(Path path) {
        super("Указанный путь " + path + " не ведет к текстовому файлу.");
    }
}
