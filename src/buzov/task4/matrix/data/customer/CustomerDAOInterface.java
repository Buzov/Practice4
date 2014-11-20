package buzov.task4.matrix.data.customer;

import buzov.task4.matrix.data.enumDao.TypeMatrixResult;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author Artur Buzov
 * @param <T>
 */
public interface CustomerDAOInterface<T extends DAOMatrixInterface> {

    /**
     * Inserts a matrix into a database, assigns to a matrix identification number. If in a database there is a matrix with such identification number, it will
     * be removed.
     *
     *
     * @param matrix Object of a matrix.
     * @param matrix_id Identification number of a matrix in a database.
     * @throws SQLException
     */
    void insert(T matrix, int matrix_id) throws SQLException;

    /**
     * Serializes three matrixes into a database.
     *
     * @param matrixA First matrix.
     * @param matrixB Second matrix.
     * @param matrixC Matrix containing result of multiplication of the first and second matrix.
     * @throws SQLException
     */
    void serializeResult(T matrixA, T matrixB, T matrixC) throws SQLException;

    /**
     * Deserializes a matrix from a row of a database. It is necessary to specify the identifier of a row and type of a returned matrix.
     *
     *
     * @param id Number of record in a database.
     * @param typeMatrixResult Type of a returned matrix.
     * @return
     * @throws ClassNotFoundException
     * @throws IOException
     * @throws SQLException
     */
    T deserializeMatrixResult(int id, TypeMatrixResult typeMatrixResult) throws
            ClassNotFoundException, IOException, SQLException;

    /**
     * Selects a matrix according to the specified identifier.
     *
     * @param matrix_id Identification number of a matrix in a database.
     * @return
     * @throws SQLException
     */
    T select(int matrix_id) throws SQLException;

    /**
     *
     * Deletes all created matrixes from a database.
     *
     * @throws SQLException
     */
    public void deleteAllMatrix() throws SQLException;

    /**
     * Deletes all results of multiplication of a matrix from a database.
     *
     * @throws SQLException
     */
    public void deleteAllResult() throws SQLException;

    /**
     * Deletes a matrix according to the specified identifier from a database.
     *
     * @param matrix_id Identification number of a matrix in a database.
     * @throws SQLException
     */
    public void deleteMatrix(int matrix_id) throws SQLException;

    /**
     * Deletes old results of multiplication of matrixes from a database.
     *
     * @throws SQLException
     */
    public void deleteOfOldResults() throws SQLException;

}
