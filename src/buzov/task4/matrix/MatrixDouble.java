package buzov.task4.matrix;

import buzov.task4.matrix.creat.InitsializatorMatrix;
import buzov.task4.matrix.exception.MatrixIndexOutOfBoundsException;

/**
 *
 * @author Artur Buzov
 */
public class MatrixDouble extends MatrixAbstract {

    /**
     *
     */
    public double[][] mas;

    /**
     * Creates the matrix which size is equal <b>rows</b> x <b>cols</b>. Type of the massif of the matrix is <b>double [][]</b>.
     *
     * @param rows quantity of rows of the matrix.
     * @param cols uantity of columns of the matrix.
     */
    public MatrixDouble(int rows, int cols) {
        super(rows, cols);
        mas = new double[rows][cols];
        dataType = "DOUBLE";
    }

    /**
     * Creates the matrix and sets its array.
     *
     * @param object
     */
    public MatrixDouble(Object object) {
        super(object);
        this.mas = (double[][]) object;
        this.rows = mas.length;
        this.cols = mas[0].length;
        dataType = "DOUBLE";
    }

    @Override
    public double getValue(int row, int col) throws MatrixIndexOutOfBoundsException {
        if ((row < 0) || (col < 0) || (row >= mas.length) || (col >= mas[0].length)) {
            throw new MatrixIndexOutOfBoundsException("Inadmissible value of an index.");
        }
        return mas[row][col];
    }

    @Override
    public void setValue(int row, int col, double value) throws MatrixIndexOutOfBoundsException {
        if ((row < 0) || (col < 0) || (row >= mas.length) || (col >= mas[0].length)) {
            throw new MatrixIndexOutOfBoundsException("Inadmissible value of an index.");
        }
        mas[row][col] = value;
    }

    @Override
    public Object getArray() {
        return (Object) mas;
    }

}
