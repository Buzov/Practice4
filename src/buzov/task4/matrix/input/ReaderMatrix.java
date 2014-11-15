package buzov.task4.matrix.input;

import buzov.task4.matrix.DataType;
import buzov.task4.matrix.Matrix;
import buzov.task4.matrix.MatrixSelector;
import buzov.task4.matrix.exception.MatrixIndexOutOfBoundsException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Locale;
import java.util.Scanner;

/**
 *
 * @author Artur Buzov
 */
public class ReaderMatrix {

    /**
     *
     * @param path Path to the matrix file.
     * @param dataType Data type which are stored in the matrix.
     * @return Returns the matrix.
     * @throws MatrixIndexOutOfBoundsException
     */
    public static Matrix readFromFile(String path, DataType dataType) throws MatrixIndexOutOfBoundsException {
        Matrix matrix = null;
        int rows;
        int cols;
        try {

            Scanner in = new Scanner(new FileReader(path));
            rows = in.nextInt();
            cols = in.nextInt();
            //creates the matrix
            matrix = MatrixSelector.getMatrix(rows, cols, dataType);

            in.useLocale(Locale.US);
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    matrix.setValue(i, j, in.nextDouble());
                }
            }

        } catch (FileNotFoundException ex) {
            System.out.println("The specified file is not found!");
        }
        return matrix;
    }
}
