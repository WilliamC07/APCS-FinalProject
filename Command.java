/**
 * This is the command given by the user. Everything the user types in should be made into this class. This
 * will be added to the stack and all information done on this will.
 */
public class Command {
    /**
     * The old value of the cell
     */
    private String oldValue;
    /**
     * The new value of the cell. This value will be shown to the user.
     */
    private String newValue;
    /**
     * Row position of the cell
     */
    private int row;
    /**
     * Column position of the cell
     */
    private int column;

    /**
     * Constructs a command
     * @param oldValue Previous value
     * @param newValue New value
     * @param column Column of edit done to
     * @param row Row of edit done to
     */
    public Command(String oldValue, String newValue, int column, int row) {
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.column = column;
        this.row = row;
    }

    /**
     * Gets the value of the cell
     * @return A string containing the old data inside the cell
     */
    public String getOldValue() {
        return oldValue;
    }

    /**
     * Gets the value of the cell
     * @return A string containing the new data inside the cell
     */
    public String getNewValue() {
        return newValue;
    }

    /**
     * Gets the row of a cell
     * @return An int that represents the location of the row of the cell
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the column of a cell
     * @return An int that represents the location of the column of the cell
     */
    public int getColumn() {
        return column;
    }
}
