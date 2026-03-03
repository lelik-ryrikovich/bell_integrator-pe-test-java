package exception;

import java.nio.file.Path;

/**
 * Исключение при чтении файла
 */
public class ReadingFileException extends AppException {
    public ReadingFileException(Throwable cause, Path path) {
        super("Проблема при чтении файла: " + path, cause);
    }
}
