package buzov.task4.matrix.run;

import buzov.task4.matrix.Matrix;
import buzov.task4.matrix.MatrixDouble;
import buzov.task4.matrix.data.customer.CustomerDAOInterface;
import buzov.task4.matrix.data.dao.DAODataBase;
import buzov.task4.matrix.data.enumDao.TypeTable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

/**
 * This class launches the matrix multiplier.
 *
 * The program can use some threads for multiplication of matrixes. The program can read matrixes from files.
 * <p>
 * The first line of the file should contain two numbers:</p>
 * <ul>
 * <li>the first number is quantity of rows of the matrix;</li>
 * <li>the second number is quantity of rows of the matrix.</li>
 * </ul>
 *
 *
 * @author Artur Buzov
 */
public class RunProgramm {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        Matrix matrixA = null;
        Matrix matrixB = null;
        Matrix matrixC = null;
        DAODataBase factory = null;
        CustomerDAOInterface matrixTable = null;
        String exit = "";

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Hello! It is matrix multiplier=)");

        boolean worker = true;

        try {
            //starts the program
            while (worker) {
                try {

                    //shows program work
                    //Inputs quantity of rows of a matrix
                    System.out.println("Input quantity of rows of the matrix.");
                    System.out.println("The quantity of rows must be less than 200.");
                    String matrixRowAndCol;
                    int rowsA = 0;
                    while (true) {
                        try {
                            matrixRowAndCol = reader.readLine();
                            rowsA = Integer.parseInt(matrixRowAndCol);
                            if ((rowsA > 0) && (rowsA <= 200)) {
                                break;
                            } else {
                                System.out.println("Incorrect value.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Incorrect value.");
                        } catch (IOException ex) {
                            System.out.println(ex);
                        }

                    }
                    //Inputs quantity of columns of the matrix
                    System.out.println("Input quantity of columns of the matrix.");
                    System.out.println("The quantity of columns must be less than 200.");
                    int colsA = 0;
                    while (true) {
                        try {
                            matrixRowAndCol = reader.readLine();
                            colsA = Integer.parseInt(matrixRowAndCol);
                            if ((colsA > 0) && (colsA <= 200)) {
                                break;
                            } else {
                                System.out.println("Incorrect value.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Incorrect value.");
                        } catch (IOException ex) {
                            System.out.println(ex);
                        }

                    }

                    int rowsB = colsA;
                    int colsB = rowsA;

                    //gets factory of the database
                    factory = new DAODataBase();
                    //gets object for work with database tables
                    matrixTable = factory.getCustomerDAO(TypeTable.MATRIX);

                    //creates two matrixes
                    matrixA = new MatrixDouble(rowsA, colsA);
                    matrixB = new MatrixDouble(rowsB, colsB);
                    //initializes two matrixes
                    matrixA.initialize();
                    matrixB.initialize();
                    //inserts two matrixes into a database
                    matrixTable.insert(matrixA, 1);
                    matrixTable.insert(matrixB, 2);

                    //reads out matrixes from the database
                    matrixA = (Matrix) matrixTable.select(1);
                    matrixB = (Matrix) matrixTable.select(2);
                    //multiplies two matrixes and creates the third matrix
                    matrixC = matrixA.multiplyThread(matrixB);
                    //serializes result in a database
                    matrixTable.serializeResult(matrixA, matrixB, matrixC);
                    //closes communication with the database

                    matrixC.print();

                    System.out.println();
                    System.out.println("To continue press \"Enter\"");

                    System.out.println("To stop the running program input \"0\".");

                    try {
                        exit = reader.readLine();
                    } catch (IOException ex) {
                        System.out.println("Error:" + ex);
                    }

                    //chooses the menu
                    if (exit.equals("0")) {
                        worker = false;
                        try {
                            matrixTable.deleteOfOldResults();
                        } catch (SQLException ex) {
                            System.out.println("Error: " + ex);
                        }

                    }

                } catch (Exception e) {
                    System.out.println("Error: " + e);
                }
            }

            try {
                reader.close();
            } catch (IOException ex) {
                System.out.println("Error: " + ex);
            }

        } finally {

            factory.close();
        }

        System.out.println("Author - Artur Buzov.");
    }
}
