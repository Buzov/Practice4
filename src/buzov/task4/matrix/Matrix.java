package buzov.task4.matrix;

import buzov.task4.matrix.exception.IllegalSizesException;
import buzov.task4.matrix.exception.IncorrectFormatOfData;
import buzov.task4.matrix.exception.MatrixIndexOutOfBoundsException;
import java.io.Serializable;

/**
 *
 * @author Artur Buzov
 */
public interface Matrix extends Serializable {

    /**
     * Returns quantity of columns of the matrix.
     * 
     * @return columns.
     */
    int getColsCount();

    /**
     * Returns quantity of rows of the matrix.
     * 
     * @return rows.
     */
    int getRowsCount();

    /**
     * Returns size of the matrix.
     * 
     * @return size.
     */
    int getSize();

    /**
     * Returns array of the matrix.
     * 
     * @return array.
     */
    Object getArray();
    
    /**
     * Returns quantity of rows which the thread takes.
     *
     * @return step of rows.
     */
    int getStepRow();

    /**
     * Returns value of the cell of the matrix.
     * 
     * @param row row of the element.
     * @param col column of the element.
     * @return value.
     * @throws MatrixIndexOutOfBoundsException
     */
    double getValue(int row, int col) throws MatrixIndexOutOfBoundsException;
    
    /**
     * Returns data type of the matrix.
     * 
     * @return data type.
     */
    String getDataType();

    /**
     * Sets value of the element.
     * 
     * @param row row of the element.
     * @param col column of the element.
     * @param value value of the element which it is necessary to set.
     * @throws MatrixIndexOutOfBoundsException
     */
    void setValue(int row, int col, double value) throws MatrixIndexOutOfBoundsException;

    /**
     * Prints the elements of the matrix.
     */
    void print();
    
    /**
     * Reads the matrix from the file.
     * 
     * 
     * @param path Path to the matrix file.
     */
    void read(String path);

    /**
     * This method writes down the matrix in the file.
     * 
     * 
     * @param path Path to the file.
     */
    void write(String path);
    
    /**
     * 
     * Fills the matrix with values.
     *
     * @throws MatrixIndexOutOfBoundsException
     */
    void initialize() throws MatrixIndexOutOfBoundsException;

    /**
     * This method multiplies matrixes using one thread.
     * 
     * @param matrix matrix on which it is necessary to increase.
     * @return Returns the resultant matrix.
     * @throws IllegalSizesException
     * @throws IncorrectFormatOfData
     * @throws MatrixIndexOutOfBoundsException
     */
    Matrix multiply(Matrix matrix) throws IllegalSizesException, 
                                     IncorrectFormatOfData,
                                     MatrixIndexOutOfBoundsException;

    /**
     * This method multiplies matrixes using some threads.
     * 
     * @param matrix matrix on which it is necessary to increase.
     * @return Returns the resultant matrix.
     * @throws IllegalSizesException
     * @throws IncorrectFormatOfData
     * @throws MatrixIndexOutOfBoundsException
     */
    Matrix multiplyThread(Matrix matrix) throws IllegalSizesException, 
                                           IncorrectFormatOfData,
                                           MatrixIndexOutOfBoundsException;
    
    
    int getId();

    void setId(int id);

}
