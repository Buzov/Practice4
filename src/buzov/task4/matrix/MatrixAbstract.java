package buzov.task4.matrix;

import buzov.task4.matrix.creat.InitsializatorMatrix;
import buzov.task4.matrix.exception.IllegalSizesException;
import buzov.task4.matrix.exception.IncorrectFormatOfData;
import buzov.task4.matrix.exception.MatrixIndexOutOfBoundsException;
import buzov.task4.matrix.operations.MultiplierMatrix;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Artur Buzov
 */
public abstract class MatrixAbstract implements Matrix, Serializable {

    private int id = 0;
    
    /**
     * Quantity of rows of the matrix.
     */
    protected int rows;

    /**
     * Quantity of columns of the matrix.
     */
    protected int cols;

    /**
     * Data type of the matrix.
     */
    protected String dataType;

    /**
     * Quantity of rows which the thread takes.
     */
    protected static final int stepRow = 1;

    /**
     * Quantity of rows which threads executed.
     */
    protected static /*volatile*/ AtomicInteger rowForTрread = new AtomicInteger(0);
    Object dataObject;

    /**
     * Creates the matrix which size is equal <b>rows</b> x <b>cols</b>.
     *
     * @param rows quantity of rows of the matrix.
     * @param cols quantity of columns of the matrix.
     */
    public MatrixAbstract(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    /**
     * Creates the matrix and sets its array.
     *
     * @param object
     */
    protected MatrixAbstract(Object object) {
        dataObject = object;
    }

    @Override
    public int getColsCount() {
        return cols;
    }

    @Override
    public int getRowsCount() {
        return rows;
    }

    @Override
    public int getSize() {
        return cols * rows;
    }

    @Override
    public String getDataType() {
        return dataType;
    }

    @Override
    public int getStepRow() {
        return stepRow;
    }

    /**
     * Returns number of the row of the matrix for the thread.
     *
     *
     * @return number of the row.
     */
    public static synchronized int getRowsForThread() {
        int temp = rowForTрread.get();
        return temp;
    }

    /**
     * Returns number of the row of the matrix for the thread. Increases number of the row by 1.
     *
     * @return number of the row.
     */
    public static synchronized int getRowsForThreadWithInkrement() {
        int temp = rowForTрread.get();
        setRowsForThread(stepRow);
        return temp;
    }

    /**
     * Sets quantity of rows which the thread takes.
     *
     * @param stepRow quantity of rows which the thread takes.
     */
    public static synchronized void setRowsForThread(int stepRow) {
        int temp;
        temp = rowForTрread.get();
        rowForTрread.compareAndSet(temp, temp + stepRow);
    }

    /**
     * Sets number of the row of the matrix equal to zero.
     */
    public static synchronized void setZeroRowsForThread() {
        int temp;
        temp = rowForTрread.get();
        rowForTрread.compareAndSet(temp, 0);
    }

    @Override
    public void print() {
        System.out.println();
        System.out.println("Matrix" + rows + "x" + cols);
        System.out.println("-------------------------------------------------");

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                try {
                    System.out.printf("%8.3f ", this.getValue(i, j));
                } catch (MatrixIndexOutOfBoundsException e) {
                    System.out.println("Error: " + e);
                }
            }
            System.out.println();
        }
    }

    @Override
    public void read(String path) {

        long startTime = System.currentTimeMillis();

        try {

            Scanner in = new Scanner(new FileReader(path));
            rows = in.nextInt();
            cols = in.nextInt();
            in.useLocale(Locale.US);
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    this.setValue(i, j, in.nextDouble());
                }
            }

        } catch (FileNotFoundException ex) {
            System.out.println("The specified file is not found!");
        } catch (MatrixIndexOutOfBoundsException ex) {
            System.out.println("Error: " + ex);
        }

        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;

        System.out.println("Reading of the file lasted " + time + " ms.");

    }

    @Override
    public void write(String path) {

        BufferedWriter buffer = null;

        long startTime = System.currentTimeMillis();

        try {
            buffer = new BufferedWriter(new FileWriter(path));
            buffer.write(rows + " " + cols + "\r\n");
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    buffer.write(InitsializatorMatrix.roundNumber(this.getValue(i, j), 3) + " ");
                }
                buffer.write("\r\n");
            }
        } catch (IOException e) {
            System.out.println("It is not possible to make record in the specified file.");
        } catch (MatrixIndexOutOfBoundsException ex) {
            Logger.getLogger(MatrixAbstract.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            buffer.flush();
            buffer.close();
        } catch (IOException e) {
            System.out.println("Error of input-output.");
        }

        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;

        System.out.println("Recording of the file lasted " + time + " ms.");
    }

    @Override
    public Matrix multiply(Matrix B) throws IllegalSizesException,
                                            IncorrectFormatOfData,
                                            MatrixIndexOutOfBoundsException {

        return MultiplierMatrix.multiply(this, B);
    }

    @Override
    public Matrix multiplyThread(Matrix B) throws IllegalSizesException,
                                                  IncorrectFormatOfData,
                                                  MatrixIndexOutOfBoundsException {

        return MultiplierMatrix.multiplyThread(this, B);
    }

    @Override
    public void initialize() throws MatrixIndexOutOfBoundsException {
        InitsializatorMatrix.makeRandomDouble(this);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

}
