package buzov.task4.matrix.data.customer;

import buzov.task4.matrix.Matrix;
import buzov.task4.matrix.MatrixDouble;
import buzov.task4.matrix.data.enumDao.TypeMatrixResult;
import buzov.task4.matrix.exception.MatrixIndexOutOfBoundsException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;

/**
 *
 * @author Artur Buzov
 */
public class CustomerDaoMatrixMySQL extends CustomerDAOAbstract {

    /**
     *
     * @param connection
     * @throws SQLException
     */
    public CustomerDaoMatrixMySQL(Connection connection) throws SQLException {
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

        statement = connection.prepareStatement(insertMatrix);

        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= cols; j++) {
                try {

                    statement.setInt(1, matrix_id);
                    statement.setInt(2, i);
                    statement.setInt(3, j);
                    statement.setDouble(4, matrix.getValue(i - 1, j - 1));
                    statement.executeUpdate();

                } catch (Exception ex) {
                    System.out.println("Error: " + ex);
                }

            }

        }

        statement.close();
        //run time
        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;

        System.out.println("Recording of the data base lasted " + time + " ms.");
    }

    @Override
    public void serializeResult(Matrix matrixA, Matrix matrixB, Matrix matrixC) throws SQLException {

        deleteOfOldResults();

        long startTime = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();
        java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());

        String insertMatrix = "INSERT INTO `matrix_history` (`data_time`, `matrix_first`, `matrix_second`, `matrix_result`) VALUES (?,?,?,?)";

        statement = connection.prepareStatement(insertMatrix);

        statement.setTimestamp(1, ourJavaTimestampObject);
        statement.setObject(2, matrixA);
        statement.setObject(3, matrixB);
        statement.setObject(4, matrixC);
        statement.executeUpdate();

        statement.close();
        //run time
        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;

        System.out.println("Recording of the data base lasted " + time + " ms.");
    }

    @Override
    public Matrix select(int matrix_id) throws SQLException {

        statement = connection.prepareStatement("SELECT COUNT(*) FROM matrix WHERE matrix_id = ? AND row_id = 1;");
        statement.setInt(1, matrix_id);
        result = statement.executeQuery();

        int cols = 0;

        while (result.next()) {
            cols = result.getInt(1);
            System.out.println("cols = " + cols);
        }

        int rows = 0;

        statement = connection.prepareStatement("SELECT COUNT(*) FROM matrix WHERE matrix_id = ?;");
        statement.setInt(1, matrix_id);
        result = statement.executeQuery();

        while (result.next()) {
            rows = result.getInt(1) / cols;
            System.out.println("rows = " + rows);
        }

        Matrix matrix = new MatrixDouble(rows, cols);
        statement = connection.prepareStatement("SELECT * FROM matrix WHERE matrix_id = ? AND row_id = ?;");

        for (int i = 0; i < rows; i++) {
            int j = 0;
            statement.setInt(1, matrix_id);
            statement.setInt(2, (i + 1));
            result = statement.executeQuery();
            while (result.next()) {
                try {
                    matrix.setValue(i, j, result.getDouble("value"));
                } catch (MatrixIndexOutOfBoundsException ex) {
                    System.out.println("Error: " + ex);
                }
                j++;
            }
        }

        statement.close();
        result.close();
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

        statement = connection.prepareStatement(sqlDeserializeObject);
        statement.setInt(1, id);

        result = statement.executeQuery();

        result.next();

        InputStream inputStream = result.getBlob(1).getBinaryStream();
        ObjectInputStream oip = new ObjectInputStream(inputStream);
        Object deSerializedObject = oip.readObject();

        result.close();
        statement.close();

        return (Matrix) deSerializedObject;
    }

    @Override
    public void deleteAllMatrix() throws SQLException {
        statement = connection.prepareStatement("DELETE FROM matrix;");
        statement.execute();
        System.out.println("Data successfully removed.");
        statement.close();
    }

    @Override
    public void deleteMatrix(int matrix_id) throws SQLException {
        statement = connection.prepareStatement("DELETE FROM `matrix` WHERE matrix_id = ?;");
        statement.setInt(1, matrix_id);
        boolean deleteResult = statement.execute();
        if (deleteResult) {
            System.out.println("Data successfully removed.");
        } else {
            System.out.println("Matrix with such id = " + matrix_id + " is not found.");
        }
        statement.close();
    }

    @Override
    public void deleteAllResult() throws SQLException {
        statement = connection.prepareStatement("DELETE FROM matrix_rusult;");
        statement.execute();
        System.out.println("Data successfully removed.");
        statement.close();
    }

    @Override
    public void deleteOfOldResults() throws SQLException {
        statement = connection.prepareStatement("DELETE FROM `matrix_history` WHERE `data_time` < (NOW() - INTERVAL 1 DAY);");
        statement.execute();
        System.out.println("Data successfully removed.");
        statement.close();
    }

}
