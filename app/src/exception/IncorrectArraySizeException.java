package exception;

public class IncorrectArraySizeException extends AppException {

    public IncorrectArraySizeException(int N, int M) {
        super("Неправильный размер массива! Размер должен быть " + N + "x" + M);
    }
}
