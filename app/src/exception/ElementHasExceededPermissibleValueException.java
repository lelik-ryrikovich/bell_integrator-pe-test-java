package exception;

/**
 * Исключение о том, что элемент превысил допустимое значение
 */
public class ElementHasExceededPermissibleValueException extends AppException {

    public ElementHasExceededPermissibleValueException(int element) {
        super("Элемент превысил допустимое значение (от 1 до 9 включительно): " + element);
    }
}
