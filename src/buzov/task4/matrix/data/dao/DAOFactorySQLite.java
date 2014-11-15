package buzov.task4.matrix.data.dao;

import buzov.task4.matrix.data.customer.CustomerDAOInterface;
import buzov.task4.matrix.data.customer.CustomerDaoMatrixSQLite;
import buzov.task4.matrix.data.enumDao.TypeTable;
import java.sql.SQLException;

class DAOFactorySQLite extends DAOAbstract {

    final private static String USER = "root";
    final private static String PASSWORD = "";
    final private static String DRIVERNAME = "org.sqlite.JDBC";

    final private static String URL = "jdbc:sqlite:sqlite/practice.db";
//"jdbc:sqlite:sqlite/practice.db";
    private DAOFactorySQLite() {
        super();
    }

    /**
     * Gets the single connection with the database SQLite.
     */
    public static DAOAbstract getInstance() {
        if (instance == null) {
            instance = new DAOFactorySQLite();
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
                return new CustomerDaoMatrixSQLite(connect);
            default:
                return null;
        }
    }
}
