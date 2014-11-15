package buzov.task4.matrix;

import buzov.task4.matrix.creat.InitsializatorMatrix;
import buzov.task4.matrix.exception.MatrixIndexOutOfBoundsException;

/**
 *
 * @author Artur Buzov
 */
public class MatrixInteger extends MatrixAbstract {

    /**
     *
     */
    public int[][] mas;

    /**
     * Creates the matrix which size is equal <b>rows</b> x <b>cols</b>.
     * Type of the massif of the matrix is <b>int [][]</b>.
     *
     * @param rows quantity of rows of the matrix.
     * @param cols uantity of columns of the matrix.
     */
    public MatrixInteger(int rows, int cols) {
        super(rows, cols);
        mas = new int[rows][cols];
        dataType = "INTEGER";
    }

    /**
     * Creates the matrix and sets its array.
     *
     * @param object
     */
    public MatrixInteger(Object object) {
        super(object);
        this.mas = (int[][]) object;
        this.rows = mas.length;
        this.cols = mas[0].length;
        dataType = "INTEGER";
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
        mas[row][col] = (int) value;
    }

    @Override
    public Object getArray() {
        return (Object) mas;
    }

    @Override
    public void initialize() throws MatrixIndexOutOfBoundsException {
        InitsializatorMatrix.makeRandomInteger(this);
    }

}
