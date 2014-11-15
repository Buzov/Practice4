package buzov.task4.matrix.test;

import buzov.task4.matrix.Matrix;
import buzov.task4.matrix.MatrixDouble;
import buzov.task4.matrix.data.customer.CustomerDAOInterface;
import buzov.task4.matrix.data.dao.DAOAbstract;
import buzov.task4.matrix.data.dao.DAODataBaseFactory;
import buzov.task4.matrix.data.enumDao.TypeDataBase;
import buzov.task4.matrix.data.enumDao.TypeMatrixResult;
import buzov.task4.matrix.data.enumDao.TypeTable;
import buzov.task4.matrix.exception.IllegalSizesException;
import buzov.task4.matrix.exception.IncorrectFormatOfData;
import buzov.task4.matrix.exception.MatrixIndexOutOfBoundsException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Test {

    public static void main(String[] args) {

        try {
            //gets factory of the database
            DAOAbstract factory = DAODataBaseFactory.getDAOFactory(TypeDataBase.H2);
            //gets object for work with database tables
            CustomerDAOInterface matrixTable = factory.getCustomerDAO(TypeTable.MATRIX);

            Matrix matrixA;
            Matrix matrixB;
            Matrix matrixC;

            int stap = 0;

            switch (stap) {
                case 0:
                    //creates two matrixes
                    matrixA = new MatrixDouble(25, 15);
                    matrixB = new MatrixDouble(15, 25);
                    //initializes two matrixes
                    matrixA.initialize();
                    matrixB.initialize();
                    //inserts two matrixes into a database
                    matrixTable.insert(matrixA, 1);
                    matrixTable.insert(matrixB, 2);
                    //closes communication with the database
                    factory.close();
                    break;
                case 1:

                    //reads out matrixes from the database
                    matrixA = matrixTable.select(1);
                    matrixB = matrixTable.select(2);
                    //multiplies two matrixes and creates the third matrix
                    matrixC = matrixA.multiply(matrixB);
                    //serializes result in a database
                    matrixTable.serializeResult(matrixA, matrixB, matrixC);
                    //closes communication with the database
                    factory.close();
                    break;
                case 2:
                    //deserializes result in a database
                    matrixC = matrixTable.deserializeMatrixResult(1, TypeMatrixResult.MATRIX_RESULT);
                    matrixC.print();
                case 3:

                default:
                    System.out.println("Exit");
            }
        } catch (SQLException |
                 MatrixIndexOutOfBoundsException |
                 IllegalSizesException |
                 IncorrectFormatOfData |
                 ClassNotFoundException |
                 IOException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: " + ex);
        }

    }

}
