package buzov.task4.matrix.tread;

import buzov.task4.matrix.Matrix;
import buzov.task4.matrix.MatrixDouble;
import buzov.task4.matrix.exception.MatrixIndexOutOfBoundsException;

/**
 *
 * @author Artur Buzov
 */
public class MultiplyThread implements Runnable {

    private boolean stopTread = false;
    private int numderTread = 0;
    private static int sumTread = 0;
    private int rowTotal;
    private int rowsA;
    private int colsA;
    private int colsB;
    private Matrix matrixA;
    private Matrix matrixB;
    private Matrix matrixC;

    /**
     * This is the constructor of the multiplication thread.
     *
     * @param matrixA First matrix.
     * @param matrixB Second matrix.
     * @param matrixС This matrix in which to save result.
     * @throws MatrixIndexOutOfBoundsException
     */
    public MultiplyThread(Matrix matrixA, Matrix matrixB, Matrix matrixС) throws MatrixIndexOutOfBoundsException {

        rowTotal = matrixA.getRowsCount();
        this.rowsA = matrixA.getRowsCount();
        this.colsA = matrixA.getColsCount();
        this.colsB = matrixB.getColsCount();
        this.matrixA = matrixA;
        this.matrixB = matrixB;
        this.matrixC = matrixС;

        sumTread++;
        numderTread = sumTread;
    }

    /**
     * This method takes the line of the first matrix and multiplies by a column of the second matrix.
     *
     * @param rows It is a line number of the multiplied matrix.
     * @throws MatrixIndexOutOfBoundsException
     */
    protected void multiply(int rows) throws MatrixIndexOutOfBoundsException {
        //temporary variable
        double temp = 0;
        //It is one-tenth of the number of rows of the matrix
        int stepProgress = rowsA / 10;
        //this quantity of rows which the thread takes
        int step = matrixA.getStepRow();
        //limiter
        int range = rows + step;
        for (int i = rows; i < range; i++) {
            if ((rowsA >= 1000) && (i % stepProgress == 0)) {
                //Multiplication progress
                System.out.println("Multiplication progress " + i / stepProgress * 10 + "%.");
            }
            for (int j = 0; j < colsB; j++) {
                for (int k = 0; k < colsA; k++) {
                    temp += matrixA.getValue(i, k) * matrixB.getValue(k, j);
                }
                matrixC.setValue(i, j, temp);
                temp = 0;
            }
        }

    }

    @Override
    public void run() {

        System.out.println("The tread number " + numderTread + " started.");

        while (!stopTread) {
            int rows = MatrixDouble.getRowsForThreadWithInkrement();
            //System.out.println("The tread number " + numderTread + ". Rows = " + rowsA + ".");
            if (rows >= rowTotal) {
                stopTread();
            } else {
                try {
                    multiply(rows);
                } catch (MatrixIndexOutOfBoundsException e) {
                    System.out.println("Error: " + e);
                }
            }
        }
        System.out.println("The tread number " + numderTread + " stopped.");
        sumTread = 0;
    }

    /**
     * This method stops the thread.
     */
    public void stopTread() {
        stopTread = true;
    }

}
