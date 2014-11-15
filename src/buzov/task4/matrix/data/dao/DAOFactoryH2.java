package buzov.task4.matrix.data.dao;

import buzov.task4.matrix.data.customer.CustomerDAOInterface;
import buzov.task4.matrix.data.customer.CustomerDaoMatrixH2;
import buzov.task4.matrix.data.enumDao.TypeTable;
import java.sql.SQLException;

class DAOFactoryH2 extends DAOAbstract {

    final private static String USER = "root";
    final private static String PASSWORD = "";
    final private static String DRIVERNAME = "org.sqlite.JDBC";

    final private static String URL = "jdbc:h2:file://d:\\Documents and Settings\\RT\\Мои документы\\NetBeansProjects\\Practice4\\h2\\practice";
        
    private DAOFactoryH2() {
        super();
    }

    /**
     * Gets the single connection with the database H2.
     */
    public static DAOAbstract getInstance() {
        if (instance == null) {
            instance = new DAOFactoryH2();
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
                return new CustomerDaoMatrixH2(connect);
            default:
                return null;
        }
    }

}
