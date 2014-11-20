package buzov.task4.matrix;

import buzov.task4.matrix.exception.IncorrectFormatOfData;

/**
 *
 * @author Artur Buzov
 */
public enum DataType {

    /**
     * Data type <b>int</b>
     */
    INTEGER("INTEGER"),
    /**
     * Data type <b>double</b>
     */
    DOUBLE("DOUBLE"),
    /**
     * Data type <b>ArrayList(ArrayList(Double))</b>
     */
    ARRAY("ARRAY");

    private String typeValue;

    private DataType(String type) {
        typeValue = type;
    }

    /**
     *
     * Returns data type
     *
     * @param typeNumber data type.
     * @return enum
     */
    static public DataType getDataType(String typeNumber) throws IncorrectFormatOfData {

        boolean dataException = true;
        DataType dataType = null;

        for (DataType type : DataType.values()) {
            if (type.getDataTypeValue().equals(typeNumber)) {
                dataException = false;
                dataType = type;
            }
        }

        if (dataException) {
            throw new IncorrectFormatOfData("unknown type");
        }

        return dataType;
    }

    /**
     * Returns value of data type.
     *
     * @return value of type.
     */
    public String getDataTypeValue() {
        return typeValue;
    }

}
