package buzov.task4.matrix.operations;

import buzov.task4.matrix.DataType;
import buzov.task4.matrix.Matrix;
import static buzov.task4.matrix.MatrixAbstract.setZeroRowsForThread;
import buzov.task4.matrix.MatrixSelector;
import buzov.task4.matrix.exception.IllegalSizesException;
import buzov.task4.matrix.exception.IncorrectFormatOfData;
import buzov.task4.matrix.exception.MatrixIndexOutOfBoundsException;
import buzov.task4.matrix.tread.MultiplyThread;

/**
 *
 * @author Artur Buzov
 */
public class MultiplierMatrix {

    private static String dataTypeA;
    private static String dataTypeB;

    /**
     *
     * This method multiplies matrixes using one thread.
     *
     *
     * @param matrixA First matrix.
     * @param matrixB Second matrix.
     * @return Returns the matrix which contains result of multiplication.
     * @throws IllegalSizesException
     * @throws MatrixIndexOutOfBoundsException
     * @throws buzov.task4.matrix.exception.IncorrectFormatOfData
     */
    public static Matrix multiply(Matrix matrixA, Matrix matrixB) throws 
            IllegalSizesException, MatrixIndexOutOfBoundsException, IncorrectFormatOfData {

        Matrix matrixC = null;
        int rowsA = matrixA.getRowsCount();
        int colsA = matrixA.getColsCount();
        int rowsB = matrixB.getRowsCount();
        int colsB = matrixB.getColsCount();
        System.out.println("Multiplication of matrixes A" + rowsA
                + "x" + colsA + " and B" + rowsB + "x" + colsB + ".");

        if ((rowsA != colsB) && (colsA != rowsB)) {
            throw new IllegalSizesException("Illegal matrix dimensions.");
        }

        dataTypeA = matrixA.getDataType();
        dataTypeB = matrixB.getDataType();
        //creates the matrix analising data type of matrixes A and B
        if (dataTypeA.equals(dataTypeB)) {
            matrixC = MatrixSelector.getMatrix(rowsA, colsB, DataType.getDataType(dataTypeA));
            //throw new IncorrectFormatOfData("Data of matrixes are not identical.");
        } else {
            matrixC = MatrixSelector.getMatrix(rowsA, colsB, DataType.DOUBLE);
        }

        System.out.println("Type of data is " + dataTypeA + ".");

        matrixC = MatrixSelector.getMatrix(rowsA, colsB, DataType.getDataType(dataTypeA));
        //temporary variable
        double temp = 0;
        //It is one-tenth of the number of rows of the matrix
        int stepProgress = rowsA / 10;
        //run time
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < rowsA; i++) {

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

        //run time
        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;

        System.out.println("Multiplication of matrixes lasted " + time + " ms.");

        return matrixC;
    }

    /**
     *
     * This method multiplies matrixes using some threads.
     *
     * @param matrixA First matrix.
     * @param matrixB Second matrix.
     * @return Returns the matrix which contains result of multiplication.
     * @throws IllegalSizesException
     * @throws MatrixIndexOutOfBoundsException
     * @throws buzov.task4.matrix.exception.IncorrectFormatOfData
     */
    public static Matrix multiplyThread(Matrix matrixA, Matrix matrixB) throws
            IllegalSizesException, MatrixIndexOutOfBoundsException, IncorrectFormatOfData {

        Matrix matrixC = null;
        int rowsA = matrixA.getRowsCount();
        int colsA = matrixA.getColsCount();
        int rowsB = matrixB.getRowsCount();
        int colsB = matrixB.getColsCount();

        System.out.println("Multitread multiplication of matrixes A" + rowsA
                + "x" + colsA + " and B" + rowsB + "x" + colsB + ".");

        if ((rowsA != colsB) && (colsA != rowsB)) {
            throw new IllegalSizesException("Illegal matrix dimensions.");
        }

        dataTypeA = matrixA.getDataType();
        dataTypeB = matrixB.getDataType();
        //creates the matrix analising data type of matrixes A and B
        if (dataTypeA.equals(dataTypeB)) {
            matrixC = MatrixSelector.getMatrix(rowsA, colsB, DataType.getDataType(dataTypeA));
            //throw new IncorrectFormatOfData("Data of matrixes are not identical.");
        } else {
            matrixC = MatrixSelector.getMatrix(rowsA, colsB, DataType.DOUBLE);
        }

        System.out.println("Type of data is " + dataTypeA + ".");

        //defines quantity of necessary thread
        int quantityOfStreams;
        if (rowsA <= 100) {
            quantityOfStreams = 1;
        } else if (rowsA < 1000) {
            //The maximum number of processors available to the virtual machine
            quantityOfStreams = Runtime.getRuntime().availableProcessors();
        } else {
            //The maximum number of threads else size of the matrix >= 1000
            quantityOfStreams = 2 * Runtime.getRuntime().availableProcessors();
        }
        
        Thread[] thrd = new Thread[quantityOfStreams];

        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < quantityOfStreams; i++) {
            thrd[i] = new Thread(new MultiplyThread(matrixA, matrixB, matrixC));
            thrd[i].start(); //thread start

        }

        for (int i = 0; i < quantityOfStreams; i++) {
            try {
                thrd[i].join(); // joining threads
            } catch (InterruptedException e) {
                System.out.println("Exception of tread.");
            }
        }

        //run time
        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;
        System.out.println("Multiplication of matrixes lasted " + time + " ms.");
        setZeroRowsForThread();

        return matrixC;
    }
}
