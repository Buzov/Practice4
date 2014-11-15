package buzov.task4.matrix.otput;

import buzov.task4.matrix.Matrix;
import buzov.task4.matrix.exception.MatrixIndexOutOfBoundsException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Artur Buzov
 */
public class WriterMatrix {

    /**
     * This method writes down the matrix in the file.
     * 
     * @param matrix Matrix which will be written in the file.
     * @param path Path to the file.
     * @throws MatrixIndexOutOfBoundsException
     */
    public static void write(Matrix matrix, String path) throws MatrixIndexOutOfBoundsException {

        int rows = matrix.getRowsCount();
        int cols = matrix.getColsCount();

        BufferedWriter buffer = null;

        long startTime = System.currentTimeMillis();

        try {
            buffer = new BufferedWriter(new FileWriter(path));
            buffer.write(rows + " " + cols + "\r\n");
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    buffer.write(matrix.getValue(i, j) + " ");
                }
                buffer.write("\r\n");
            }
        } catch (IOException e) {
            System.out.println("It is not possible to make record in the specified file.");
        } catch (MatrixIndexOutOfBoundsException ex) {
            System.out.println("Error: " + ex);
        }

        try {
            buffer.flush();
            buffer.close();
        } catch (IOException e) {
            System.out.println("Error of input-output.");
        }

        //run time
        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;

        System.out.println("Recording of the file lasted " + time + " ms.");

    }

}
