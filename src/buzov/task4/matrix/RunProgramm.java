package buzov.task4.matrix;

import buzov.task4.matrix.exception.IllegalSizesException;
import buzov.task4.matrix.exception.IncorrectFormatOfData;
import buzov.task4.matrix.exception.MatrixIndexOutOfBoundsException;
import buzov.task4.matrix.input.ReaderMatrix;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

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
        String exit = "";
        boolean exitFromDemonstration = true;

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Hello! It is matrix multiplier=)");

        System.out.println("To multiply in one thread input \"1\"");
        System.out.println("To multiply in more treads input \"2\"");

        String numberOfThread = "";
        //chooses quantity of threads
        while (true) {
            try {
                numberOfThread = reader.readLine();
            } catch (IOException ex) {
                System.out.println(ex);
            }
            if (numberOfThread.equals("1") || numberOfThread.equals("2")) {
                break;
            }
            System.out.println("Incorrect value. Please try once again");
        }

        boolean inputNumber = false;
        boolean worker = true;

        //starts the program
        while (worker) {
            try {
                exitFromDemonstration = true;
                //chooses menu point
                System.out.println("To look at demonstration of work input \"0\".");
                System.out.println("To multiply your matrixes input \"1\".");
                String verificationVariable = "";
                try {
                    verificationVariable = reader.readLine();
                } catch (IOException ex) {
                    System.out.println(ex);
                }

                switch (verificationVariable) {
                    case "0":
                        //shows program work
                        while (true) {
                            //Inputs quantity of rows of a matrix
                            System.out.println("Input quantity of rows of the matrix.");
                            String matrixRowAndCol;
                            int rowsA = 0;
                            while (true) {
                                try {
                                    matrixRowAndCol = reader.readLine();
                                    rowsA = Integer.parseInt(matrixRowAndCol);
                                    if (rowsA > 0) {
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
                            int colsA = 0;
                            while (true) {
                                try {
                                    matrixRowAndCol = reader.readLine();
                                    colsA = Integer.parseInt(matrixRowAndCol);
                                    if (colsA > 0) {
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
                            //creates names of matrixes A and B
                            String fileNameOfMatrixA = "A" + rowsA + "x" + colsA + ".txt";
                            String fileNameOfMatrixB = "B" + rowsB + "x" + colsB + ".txt";

                            //initializes matrix A
                            matrixA = MatrixSelector.getMatrix(rowsA, colsA, DataType.DOUBLE);
                            try {
                                matrixA.initialize();
                            } catch (MatrixIndexOutOfBoundsException ex) {
                                System.out.println("Error:" + ex);
                            }
                            //writes down the matrix A in the file
                            matrixA.write(fileNameOfMatrixA);

                            //initializes matrix A
                            matrixB = MatrixSelector.getMatrix(rowsB, colsB, DataType.DOUBLE);
                            try {
                                matrixB.initialize();
                            } catch (MatrixIndexOutOfBoundsException ex) {
                                System.out.println("Error:" + ex);
                            }
                            //writes down the matrix B in the file
                            matrixB.write(fileNameOfMatrixB);

                            //creates name of the matrix C
                            String pathMatrixC = "C" + rowsA + "x" + colsB
                                    + "forJavaDouble" + ".txt";

                            //chooses multiplication type
                            try {
                                switch (numberOfThread) {
                                    case "1":

                                        matrixC = matrixA.multiply(matrixB);

                                        break;
                                    case "2":
                                        matrixC = matrixA.multiplyThread(matrixB);
                                        break;
                                }
                            } catch (IllegalSizesException |
                                     IncorrectFormatOfData |
                                     MatrixIndexOutOfBoundsException ex) {
                                System.out.println("Error:" + ex);
                            }

                            //writes down the matrix C in the file
                            matrixC.write(pathMatrixC);

                            System.out.println();
                            System.out.println("To continue press \"Enter\"");
                            System.out.println("To return to the menu input \"1\"");
                            System.out.println("To stop the running program input \"0\".");

                            try {
                                exit = reader.readLine();
                            } catch (IOException ex) {
                                System.out.println("Error:" + ex);
                            }

                            //chooses the menu
                            if (exit.equals("1")) {
                                inputNumber = true;
                                exitFromDemonstration = false;
                                break;
                            } else if (exit.equals("0")) {
                                inputNumber = true;
                                worker = false;
                                exitFromDemonstration = false;
                                break;
                            } else {

                            }
                        }

                    case "1":
                        while (exitFromDemonstration) {

                            System.out.println("Specify the path to the matrix A.");
                            System.out.println("Example: \"c:\\A10X10.txt\".");
                            String pathMatrixA = "";

                            //reads out the path to the file of the matrix A and checks it
                            while (true) {
                                try {
                                    pathMatrixA = reader.readLine();
                                } catch (IOException ex) {
                                    System.out.println("Error:" + ex);
                                }
                                if (new File(pathMatrixA).exists()) {
                                    break;
                                }
                                System.out.println("The file is not found.");
                                System.out.println("Specify the path repeatedly.");
                            }

                            System.out.println("Specify the way to the matrix B.");
                            String pathMatrixB = "";

                            //reads out the path to the file of the matrix B and checks it
                            while (true) {
                                try {
                                    pathMatrixB = reader.readLine();
                                } catch (IOException ex) {
                                    System.out.println("Error:" + ex);
                                }
                                if (new File(pathMatrixB).exists()) {
                                    break;
                                }
                                System.out.println("The file is not found.");
                                System.out.println("Specify the path repeatedly.");

                            }

                            System.out.println("Specify the path to the matrix in which to save result.");
                            String pathMatrixC = "";

                            //reads out the path to the file of the matrix C and checks it
                            while (true) {
                                try {
                                    pathMatrixC = reader.readLine();
                                } catch (IOException ex) {
                                    System.out.println("Error:" + ex);
                                }
                                try {
                                    FileWriter writer = new FileWriter(pathMatrixC);
                                    break;
                                } catch (IOException e) {
                                    System.out.println("The file cannot be created.");
                                    System.out.println("Specify the path repeatedly.");
                                }

                            }

                            //reads matrixes from files
                            try {
                                matrixA = ReaderMatrix.readFromFile(pathMatrixA, DataType.DOUBLE);
                                matrixB = ReaderMatrix.readFromFile(pathMatrixB, DataType.DOUBLE);

                                //chooses multiplication type
                                switch (numberOfThread) {
                                    case "1":
                                        matrixC = matrixA.multiply(matrixB);
                                        break;
                                    case "2":
                                        matrixC = matrixA.multiplyThread(matrixB);
                                        break;
                                }
                            } catch (IllegalSizesException |
                                     IncorrectFormatOfData |
                                     MatrixIndexOutOfBoundsException ex) {
                                System.out.println("Error:" + ex);
                            }

                            //writes down the matrix C in the file
                            matrixC.write(pathMatrixC);

                            System.out.println();
                            System.out.println("To continue press \"Enter\"");
                            System.out.println("To return to the menu input \"1\"");
                            System.out.println("To stop the running program input \"0\".");

                            try {
                                exit = reader.readLine();
                            } catch (IOException ex) {
                                System.out.println("Error:" + ex);
                            }

                            //chooses the menu
                            if (exit.equals("1")) {
                                inputNumber = true;
                                break;
                            } else if (exit.equals("0")) {
                                inputNumber = true;
                                worker = false;
                                break;
                            } else {

                            }
                        }

                        break;
                    default:
                        if (!inputNumber) {
                            System.err.println("The incorrect value is input. Please try again.");
                        }

                }
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }

        System.out.println("Author - Artur Buzov.");
    }
}
