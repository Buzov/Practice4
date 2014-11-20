package buzov.task4.matrix.data.customer;

import buzov.task4.matrix.Matrix;
import buzov.task4.matrix.MatrixDouble;
import buzov.task4.matrix.data.dao.DAODataBase;
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
public class CustomerDaoMatrix extends CustomerDAOAbstract implements CustomerDAOInterface<Matrix> {

    private static final String insertMatrixString = "INSERT INTO `matrix` (`matrix_id`, `row_id`, `col_id`, `value`) VALUES (?,?,?,?)";

    private static final String serializeResultString = "INSERT INTO `matrix_history` (`data_time`, `matrix_first`, `matrix_second`, `matrix_result`) VALUES (?,?,?,?)";

    private static final String selectColsString = "SELECT COUNT(*) FROM matrix WHERE matrix_id = ? AND row_id = 1;";

    private static final String selectRowsString = "SELECT COUNT(*) FROM matrix WHERE matrix_id = ?;";

    private static final String selectMatrixString = "SELECT * FROM matrix WHERE matrix_id = ? AND row_id = ?;";

    private static final String deleteMatrixString = "DELETE FROM `matrix` WHERE matrix_id = ?;";

    private static final String deleteOfOldResultsStringH2 = "DELETE FROM `matrix_history` WHERE `data_time` < timestampadd(day, -1, now());";

    private static final String deleteOfOldResultsStringMySQl = "DELETE FROM `matrix_history` WHERE `data_time` < (NOW() - INTERVAL 1 DAY);";

    private static final String driverName = DAODataBase.getDriverName();

    private static final String createBase = "CREATE TABLE IF NOT EXISTS matrix ( \n"
            + "matrix_id INTEGER NOT NULL , \n"
            + "row_id INTEGER NOT NULL , \n"
            + "col_id INTEGER NOT NULL , \n"
            + "value DOUBLE NOT NULL ); \n"
            + "\n"
            + "CREATE TABLE IF NOT EXISTS matrix_history ( \n"
            + "id INT PRIMARY KEY AUTO_INCREMENT , \n"
            + "data_time TIMESTAMP NOT NULL , \n"
            + "matrix_first LONGBLOB  NOT NULL , \n"
            + "matrix_second LONGBLOB  NOT NULL , \n"
            + "matrix_result LONGBLOB NOT NULL , \n"
            + ");";

    /**
     *
     * @param connection
     * @throws SQLException
     */
    public CustomerDaoMatrix(Connection connection) throws SQLException {
        super(connection);
        initialize();
    }

    @Override
    public void insert(Matrix matrix, int matrix_id) throws SQLException {

        deleteMatrix(matrix_id);

        int rows = matrix.getRowsCount();
        int cols = matrix.getColsCount();

        long startTime = System.currentTimeMillis();

        statement = connection.prepareStatement(insertMatrixString);

        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= cols; j++) {
                try {

                    statement.setInt(1, matrix_id);
                    statement.setInt(2, i);
                    statement.setInt(3, j);
                    statement.setDouble(4, matrix.getValue(i - 1, j - 1));
                    statement.addBatch();
//statement.executeUpdate();

                } catch (Exception ex) {
                    System.out.println("Error: " + ex);
                }

            }

        }

        statement.executeBatch();

        statement.close();
        //run time
        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;

        System.out.println("Recording of the data base lasted " + time + " ms.");
    }

    @Override
    public void serializeResult(Matrix matrixA, Matrix matrixB, Matrix matrixC) throws SQLException {

        long startTime = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();
        java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());

        statement = connection.prepareStatement(serializeResultString);

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

        statement = connection.prepareStatement(selectColsString);
        statement.setInt(1, matrix_id);
        result = statement.executeQuery();

        int cols = 0;

        while (result.next()) {
            cols = result.getInt(1);
            //System.out.println("cols = " + cols);
        }

        int rows = 0;

        statement = connection.prepareStatement(selectRowsString);
        statement.setInt(1, matrix_id);
        result = statement.executeQuery();

        while (result.next()) {
            rows = result.getInt(1) / cols;
            //System.out.println("rows = " + rows);
        }

        Matrix matrix = new MatrixDouble(rows, cols);
        statement = connection.prepareStatement(selectMatrixString);

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
        statement.close();
    }

    @Override
    public void deleteMatrix(int matrix_id) throws SQLException {
        statement = connection.prepareStatement(deleteMatrixString);
        statement.setInt(1, matrix_id);
        statement.execute();
        statement.close();
    }

    @Override
    public void deleteAllResult() throws SQLException {
        statement = connection.prepareStatement("DELETE FROM matrix_rusult;");
        statement.execute();
        statement.close();
    }

    @Override
    public void deleteOfOldResults() throws SQLException {

        switch (driverName) {
            case "org.h2.Driver":
                statement = connection.prepareStatement(deleteOfOldResultsStringH2);
                break;
            case "com.mysql.jdbc.Driver":
                statement = connection.prepareStatement(deleteOfOldResultsStringMySQl);
                break;
            default:
                System.out.println("Not true name of the driver.");
        }

        statement.execute();
        statement.close();
    }

    private void initialize() throws SQLException {

        statement = connection.prepareStatement(createBase);
        statement.execute();
    }

}
