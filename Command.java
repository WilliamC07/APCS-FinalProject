public class Command {
    private String oldValue;
    private String newValue;
    private int row;
    private int column;

    public Command(String oldValue, String newValue, int row, int column) {
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.row = row;
        this.column = column;
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
