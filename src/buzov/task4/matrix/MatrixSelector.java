package buzov.task4.matrix;

/**
 *
 * @author Artur Buzov
 */
public class MatrixSelector {

    /**
     * Returns matrix which size is equal <b>rows</b>x<b>cols</b>.
     * 
     * @param rows quantity of rows of the matrix.
     * @param cols quantity of columns of the matrix.
     * @param dataType data type of the matrix.
     * @return matrix.
     */
    public static Matrix getMatrix(int rows, int cols, DataType dataType) {
        Matrix matrix = null;
        switch (dataType) {
            case INTEGER:
                matrix = new MatrixInteger(rows, cols);
                break;
            case DOUBLE:
                matrix = new MatrixDouble(rows, cols);
                break;
            case ARRAY:
                matrix = new MatrixArray(rows, cols);
                break;
        }

        return matrix;
    }

}
