package buzov.task4.matrix.data.dao;

import buzov.task4.matrix.data.customer.CustomerDAOInterface;
import buzov.task4.matrix.data.customer.CustomerDaoMatrixMySQL;
import buzov.task4.matrix.data.enumDao.TypeTable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Artur Buzov
 */
public abstract class DAOAbstract {

    /**
     * singleton object of this type
     */
    protected static DAOAbstract instance;

    /**
     * A connection (session) with a specific database.
     */
    protected Connection connect;

    /**
     * A user name of the database.
     */
    protected String user = null;

    /**
     * A password for access to the database.
     */
    protected String password = null;

    /**
     * A URL of the database.
     */
    protected String url = null;

    /**
     * A name of the database driver.
     */
    protected String driverName = null;

    DAOAbstract() {
        setUser();
        setPassword();
        setUrl();
        setDriverName();

        try {
            Class.forName(driverName);//Registers the driver
        } catch (ClassNotFoundException e) {
            System.out.println("Error: " + e);

        }
    }

    /**
     * Sets a user name.
     */
    protected abstract void setUser();

    /**
     * Sets a password.
     */
    protected abstract void setPassword();

    /**
     * Sets a URL of the database.
     */
    protected abstract void setUrl();

    /**
     * Sets a name of the database driver.
     */
    protected abstract void setDriverName();

    /**
     * Connects to a database.
     *
     * @throws SQLException
     */
    public void open() throws SQLException {
        if ((this.connect == null) || (this.connect.isClosed())) {
            this.connect = DriverManager.getConnection(url, user, password);
        }
    }

    /**
     * Disconnects with a database.
     *
     * @throws SQLException
     */
    //public abstract DAOAbstract getInstance();
    public void close() throws SQLException {
        if ((this.connect != null) && (!this.connect.isClosed())) {
            this.connect.close();
        }
    }

    /**
     * Gets object for work with database tables.
     *
     * @param typeTable This is type table of database.
     * @return
     * @throws SQLException
     */
    public abstract CustomerDAOInterface getCustomerDAO(TypeTable typeTable) throws SQLException;

}
