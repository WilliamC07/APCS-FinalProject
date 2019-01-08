/**
 * This is the command given by the user. Everything the user types in should be made into this class. This
 * will be added to the stack and all information done on this will.
 */
public class Command {
    private String oldValue;
    private String newValue;
    private int row;
    private int column;

    /**
     * Constructs an command
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

    public String getOldValue() {
        return oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
