package buzov.task4.matrix.data.dao;

import buzov.task4.matrix.data.customer.CustomerDAOInterface;
import buzov.task4.matrix.data.customer.CustomerDaoMatrixMySQL;
import buzov.task4.matrix.data.enumDao.TypeTable;
import java.sql.SQLException;

class DAOFactoryMySQL extends DAOAbstract {

    final private static String USER = "root";
    final private static String PASSWORD = "";
    final private static String DRIVERNAME = "com.mysql.jdbc.Driver";

    final private static String SERVER = "localhost";
    final private static String PORT = "3306";
    final private static String SID = "/practice?zeroDateTimeBehavior=convertToNull";
    final private static String URL = "jdbc:mysql://" + SERVER + ":" + PORT + SID;

    private DAOFactoryMySQL() {
        super();
    }

    /**
     * Gets the single connection with the database MySQL.
     */
    public static DAOAbstract getInstance() {
        if (instance == null) {
            instance = new DAOFactoryMySQL();
        }
        return instance;
    }

    @Override
    protected void setUser() {
        super.user = USER;
    }

    @Override
    protected void setPassword() {
        super.password = PASSWORD;
    }

    @Override
    protected void setUrl() {
        super.url = URL;
    }

    @Override
    protected void setDriverName() {
        super.driverName = DRIVERNAME;
    }

    @Override
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
                return new CustomerDaoMatrixMySQL(connect);
            default:
                return null;
        }
    }

}
