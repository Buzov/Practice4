package buzov.task4.matrix;

import buzov.task4.matrix.creat.InitsializatorMatrix;
import buzov.task4.matrix.exception.MatrixIndexOutOfBoundsException;
import java.util.ArrayList;

/**
 *
 * @author Artur Buzov
 */
public class MatrixArray extends MatrixAbstract {

    private ArrayList<ArrayList<Double>> mas;

    /**
     * Creates the matrix which size is equal <b>rows</b> x <b>cols</b>.
     * Type of the massif of the matrix is <b>ArrayList(ArrayList(Double))</b>..
     *
     * @param rows quantity of rows of the matrix.
     * @param cols uantity of columns of the matrix.
     */
    public MatrixArray(int rows, int cols) {
        super(rows, cols);
        mas = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            mas.add(new ArrayList<Double>(cols));
            for (int j = 0; j < cols; j++) {
                mas.get(i).add(Double.NaN);
            }
        }
        dataType = "ARRAY";

    }

    /**
     * Creates the matrix and sets its array.
     *
     * @param object
     */
    public MatrixArray(Object object) {
        super(object);
        mas = (ArrayList<ArrayList<Double>>) dataObject;
        this.rows = mas.size();
        this.cols = mas.get(0).size();
        dataType = "ARRAY";
    }

    @Override
    public double getValue(int row, int col) throws MatrixIndexOutOfBoundsException {
        if ((row < 0) || (col < 0) || (row >= mas.size()) || (col >= mas.get(0).size())) {
            throw new MatrixIndexOutOfBoundsException("Inadmissible value of an index.");
        }
        return mas.get(row).get(col);
    }

    @Override
    public void setValue(int row, int col, double value) throws MatrixIndexOutOfBoundsException {
        if ((row < 0) || (col < 0) || (row >= mas.size()) || (col >= mas.get(0).size())) {
            throw new MatrixIndexOutOfBoundsException("Inadmissible value of an index.");
        }
        mas.get(row).set(col, (double) value);
    }

    @Override
    public Object getArray() {
        return (Object) mas;
    }
   
}
