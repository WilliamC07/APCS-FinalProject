import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.Terminal.Color;

public class HandleCommand {
    private final CSVRepresentation csvRepresentation;
    public HandleCommand(CSVRepresentation csvRepresentation){
        this.csvRepresentation = csvRepresentation;
    }

    public void set(int column, int row, String value){
      Command c = new Command(csvRepresentation.getValue(column,row), value, column, row);
      csvRepresentation.pushCommand(c);
    }

    public void swap(int columnOld, int rowOld, int columnNew, int rowNew){
      String oldval = csvRepresentation.getValue(columnOld,rowOld);
      String newval = csvRepresentation.getValue(columnNew, rowNew);
      Command c = new Command(oldval, newval, columnNew , rowNew);
      Command d = new Command(newval, oldval, columnOld, rowOld);
      csvRepresentation.pushCommand(c);
      csvRepresentation.pushCommand(d);
    }

}
