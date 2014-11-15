package buzov.task4.matrix.data.dao;

import buzov.task4.matrix.data.enumDao.TypeDataBase;
import java.sql.SQLException;

/**
 * 
 * 
 * @author Artur Buzov
 */
public class DAODataBaseFactory {

    /**
     *
     * Returns object of the chosen database.
     * 
     * @param typeDataBase This is type of the connected database.
     * @return DAOAbstract
     * @throws SQLException
     */
    public static DAOAbstract getDAOFactory(TypeDataBase typeDataBase) throws SQLException {

        switch (typeDataBase) {
            case MySQL:
                return DAOFactoryMySQL.getInstance();
            case ORACLE:
                return DAOFactoryOracle.getInstance();
            case H2:
                return DAOFactoryH2.getInstance();
            case SQLite:
                return DAOFactorySQLite.getInstance();
            default:
                return null;
        }
    }

}
