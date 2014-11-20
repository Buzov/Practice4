package buzov.task4.matrix.data.dao;

import buzov.task4.matrix.data.customer.CustomerDAOInterface;
import buzov.task4.matrix.data.customer.CustomerDaoMatrix;
import buzov.task4.matrix.data.enumDao.TypeTable;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author Artur Buzov
 */
public class DAODataBase {

    /**
     * A user name of the database.
     */
    private static final String USER;

    /**
     * A password for access to the database.
     */
    private static final String PASSWORD;

    /**
     * A URL of the database.
     */
    private static final String URL;

    /**
     * A name of the database driver.
     */
    private static final String DRIVERNAME;

    /**
     * The <code>Properties</code> class represents a persistent set of properties. The <code>Properties</code> can be loaded from a stream.
     */
    private static final Properties settings;

    /**
     * Singleton object of this type.
     */
    private static DAODataBase instance;

    /**
     * A connection (session) with a specific database.
     */
    private Connection connect;

    static {
        settings = new Properties();
        try {
            FileInputStream input = new FileInputStream("config/settings.ini");
            settings.load(input);
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
        USER = settings.getProperty("USER");
        PASSWORD = settings.getProperty("PASSWORD");
        DRIVERNAME = settings.getProperty("DRIVERNAME");
        URL = settings.getProperty("URL");
    }

    public DAODataBase() {

        try {
            Class.forName(DRIVERNAME);//Registers the driver
        } catch (ClassNotFoundException e) {
            System.out.println("Error: " + e);

        }
    }
    
    /**
     * Gets the driver name.
     *
     * @return
     */
    public static String getDriverName() {
        return DRIVERNAME;
    }

    /**
     * Gets the single connection with the database H2.
     *
     * @return
     */
    public static DAODataBase getInstance() {
        synchronized (DAODataBase.class) {
            if (instance == null) {
                instance = new DAODataBase();
            }
            return instance;
        }
    }

    /**
     * Connects to a database.
     *
     * @throws SQLException
     */
    public void open() throws SQLException {
        if ((this.connect == null) || (this.connect.isClosed())) {
            this.connect = DriverManager.getConnection(URL, USER, PASSWORD);
        }
    }

    /**
     * Disconnects with a database.
     *
     */
    //public abstract DAOAbstract getInstance();
    public void close() {
        try {
            if ((this.connect != null) && (!this.connect.isClosed())) {
                this.connect.close();
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex);
        }
    }

    /**
     * Gets object for work with database tables.
     *
     * @param typeTable This is type table of database.
     * @return
     * @throws SQLException
     */
    public CustomerDAOInterface getCustomerDAO(TypeTable typeTable) throws SQLException {

        try {
            //Let's ensure our connection is open
            if (this.connect == null || this.connect.isClosed()) {
                this.open();
            }
        } catch (SQLException e) {
            throw e;
        }

        switch (typeTable) {
            case MATRIX:
                return new CustomerDaoMatrix(connect);
            default:
                return null;
        }
    }

}
