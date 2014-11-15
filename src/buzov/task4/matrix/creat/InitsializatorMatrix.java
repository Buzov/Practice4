package buzov.task4.matrix.creat;

import buzov.task4.matrix.Matrix;
import buzov.task4.matrix.exception.MatrixIndexOutOfBoundsException;
import java.math.BigDecimal;

/**
 *
 * @author Artur Buzov
 */
public class InitsializatorMatrix {

    private static final int accuracy = 3;
    private static final int maxNumber = 10;

    /**
     * Rounds the number wich the given accuracy.
     *
     * @param value The number, which will be rounded.
     * @param digits The value of calculations accuracy.
     * @return
     */
    public static BigDecimal roundNumber(double value, int digits) {
        //we approximate the transferred number "value" with accuracy "digits"          
        BigDecimal num = new BigDecimal(value).setScale(digits, BigDecimal.ROUND_UP);
        return num;
    }

    /**
     * The method fills the matrix with type numbers. 
     * Type of numbers is <b>double</b>.
     *
     * @param matrix filled matrix
     * @throws MatrixIndexOutOfBoundsException
     */
    public static void makeRandomDouble(Matrix matrix) throws MatrixIndexOutOfBoundsException {

        int rows = matrix.getRowsCount();
        int cols = matrix.getColsCount();
        double temp = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // fill with random values
                temp = roundNumber(Math.random() * maxNumber, accuracy).doubleValue();
                matrix.setValue(i, j, temp);
            }
        }

    }

    /**
     * The method fills the matrix with type numbers. 
     * Type of numbers is <b>int</b>.
     *
     * @param matrix filled matrix
     * @throws MatrixIndexOutOfBoundsException
     */
    public static void makeRandomInteger(Matrix matrix) throws MatrixIndexOutOfBoundsException {

        int rows = matrix.getRowsCount();
        int cols = matrix.getColsCount();
        double temp = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // fill with random values
                temp = (int) (Math.random() * maxNumber);
                matrix.setValue(i, j, temp);
            }
        }

    }

}
