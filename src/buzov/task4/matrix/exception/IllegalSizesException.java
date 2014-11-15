package buzov.task4.matrix.exception;

/**
 *
 * Signals about that the size of the first matrix 
 * does not match to the size of the second matrix.
 * 
 * @author Artur Buzov
 */
public class IllegalSizesException extends Exception {

    /**
     *
     */
    public IllegalSizesException() {
        super();
    }

    /**
     *
     * @param message the detail message.
     */
    public IllegalSizesException(String message) {
        super(message);
    }
}
