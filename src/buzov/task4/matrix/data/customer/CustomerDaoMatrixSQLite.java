package buzov.task4.matrix.data.customer;

import buzov.task4.matrix.Matrix;
import buzov.task4.matrix.MatrixDouble;
import buzov.task4.matrix.data.enumDao.TypeMatrixResult;
import buzov.task4.matrix.exception.MatrixIndexOutOfBoundsException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Artur Buzov
 */
public class CustomerDaoMatrixSQLite extends CustomerDAOAbstract {

    /**
     *
     * @param connection
     * @throws SQLException
     */
    public CustomerDaoMatrixSQLite(Connection connection) throws SQLException {
        super(connection);
    }

    @Override
    public void insert(Matrix matrix, int matrix_id) throws SQLException {

        deleteMatrix(matrix_id);

        int rows = matrix.getRowsCount();
        int cols = matrix.getColsCount();

        long startTime = System.currentTimeMillis();

        String insertMatrix = "INSERT INTO `matrix` (`matrix_id`, `row_id`, `col_id`, `value`) "
                + "VALUES (?,?,?,?)";

        PreparedStatement pstmtProducts = connection.prepareStatement(insertMatrix);

        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= cols; j++) {
                try {

                    pstmtProducts.setInt(1, matrix_id);
                    pstmtProducts.setInt(2, i);
                    pstmtProducts.setInt(3, j);
                    pstmtProducts.setDouble(4, matrix.getValue(i - 1, j - 1));
                    pstmtProducts.executeUpdate();

                } catch (Exception ex) {
                    System.out.println("Error: " + ex);
                }

            }

        }

        //run time
        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;

        System.out.println("Recording of the data base lasted " + time + " ms.");
    }

    @Override
    public void serializeResult(Matrix matrixA, Matrix matrixB, Matrix matrixC) throws SQLException {

        deleteOfOldResults();

        long startTime = System.currentTimeMillis();

        String insertMatrix = "INSERT INTO `matrix_history` (`data_time`, `matrix_first`, `matrix_second`, `matrix_result`) VALUES (DATETIME('NOW'),?,?,?)";

        PreparedStatement pstmt = connection.prepareStatement(insertMatrix);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oout;
        try {

            oout = new ObjectOutputStream(baos);
            oout.writeObject(matrixA);
            oout.close();

        } catch (IOException ex) {
            Logger.getLogger(CustomerDaoMatrixSQLite.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] buf = baos.toByteArray();

        pstmt.setObject(1, matrixA);
        pstmt.setObject(2, matrixB);
        pstmt.setObject(3, matrixC);
        pstmt.executeUpdate();

        pstmt.close();
        //run time
        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;

        System.out.println("Recording of the data base lasted " + time + " ms.");
    }

    @Override
    public Matrix select(int matrix_id) throws SQLException {

        result = statement.executeQuery("SELECT COUNT(*) FROM matrix WHERE matrix_id = "
                + matrix_id + " AND row_id = 1;");

        int cols = 0;

        while (result.next()) {
            cols = result.getInt(1);
            System.out.println("cols = " + cols);
        }

        int rows = 0;

        result = statement.executeQuery("SELECT COUNT(*) FROM matrix WHERE matrix_id = "
                + matrix_id + ";");

        while (result.next()) {
            rows = result.getInt(1) / cols;
            System.out.println("rows = " + rows);
        }

        Matrix matrix = new MatrixDouble(rows, cols);

        for (int i = 0; i < rows; i++) {
            int j = 0;
            result = statement.executeQuery("SELECT * FROM matrix WHERE matrix_id = "
                    + matrix_id + " AND row_id = " + (i + 1) + ";");
            while (result.next()) {
                try {
                    matrix.setValue(i, j, result.getDouble("value"));
                } catch (MatrixIndexOutOfBoundsException ex) {
                    System.out.println("Error: " + ex);
                }
                j++;
            }
        }
        return matrix;
    }

    @Override
    public Matrix deserializeMatrixResult(int id, TypeMatrixResult typeMatrixResult) throws
            ClassNotFoundException, IOException, SQLException {

        String sqlDeserializeObject = null;

        switch (typeMatrixResult) {
            case MATRIX_FIRST:
                sqlDeserializeObject = "SELECT matrix_first FROM matrix_history WHERE id = ? ;";
                break;
            case MATRIX_SECOND:
                sqlDeserializeObject = "SELECT matrix_second FROM matrix_history WHERE id = ? ;";
                break;
            case MATRIX_RESULT:
                sqlDeserializeObject = "SELECT matrix_result FROM matrix_history WHERE id = ? ;";
                break;
            default:
        }

        PreparedStatement pstmt = connection.prepareStatement(sqlDeserializeObject);

        pstmt.setInt(1, id);
        ResultSet resultSet = pstmt.executeQuery();

        resultSet.next();

        InputStream inputStream = resultSet.getBlob(1).getBinaryStream();
        ObjectInputStream oip = new ObjectInputStream(inputStream);
        Object deSerializedObject = oip.readObject();
        resultSet.close();

        pstmt.close();

        return (Matrix) deSerializedObject;
    }

    @Override
    public void deleteAllMatrix() throws SQLException {
        statement.execute("DELETE FROM matrix;");
        System.out.println("Data successfully removed.");
    }

    @Override
    public void deleteMatrix(int matrix_id) throws SQLException {
        boolean deleteResult = statement.execute("DELETE FROM `matrix` WHERE matrix_id = " + matrix_id + ";");
        if (deleteResult) {
            System.out.println("Data successfully removed.");
        } else {
            System.out.println("Matrix with such id = " + matrix_id + " is not found.");
        }
    }

    @Override
    public void deleteAllResult() throws SQLException {
        statement.execute("DELETE FROM matrix_rusult;");
        System.out.println("Data successfully removed.");
    }

    @Override
    public void deleteOfOldResults() throws SQLException {
        statement.execute("DELETE FROM `matrix_history` WHERE `data_time` < (DATETIME('NOW','start of day','-1 day'));");
        System.out.println("Data successfully removed.");
    }

}
