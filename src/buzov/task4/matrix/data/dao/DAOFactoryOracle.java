package buzov.task4.matrix.data.dao;

import buzov.task4.matrix.data.customer.CustomerDAOInterface;
import buzov.task4.matrix.data.customer.CustomerDaoMatrixMySQL;
import buzov.task4.matrix.data.enumDao.TypeTable;
import java.sql.SQLException;

class DAOFactoryOracle extends DAOAbstract {

    final private static String USER = "root";
    final private static String PASSWORD = "";
    final private static String DRIVERNAME = "oracle.jdbc.driver.OracleDriver";

    final private static String SERVER = "localhost";
    final private static String PORT = "1521";
    final private static String SID = "XE";
    final private static String URL = "jdbc:oracle:thin:@" + SERVER + ":" + PORT + ":" + SID;

    private DAOFactoryOracle() {
        super();
    }

    /**
     * Gets the single connection with the database Oracle.
     */
    public static DAOAbstract getInstance() {
        if (instance == null) {
            instance = new DAOFactoryOracle();
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
