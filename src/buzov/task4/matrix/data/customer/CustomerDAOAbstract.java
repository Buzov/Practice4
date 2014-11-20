package buzov.task4.matrix.data.customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Artur Buzov
 */
public abstract class CustomerDAOAbstract {

    /**
     * Object of connection with a database.
     */
    Connection connection = null;
    /**
     * The object used for executing a static SQL statement and returning the results it produces.
     */
    PreparedStatement statement = null;
    /**
     * This object maintains a cursor pointing to its current row of data.
     */
    ResultSet result = null;

    /**
     *
     * @param connection Object of connection with a database.
     * @throws SQLException
     */
    public CustomerDAOAbstract(Connection connection) throws SQLException {
        this.connection = connection;

    }

}
