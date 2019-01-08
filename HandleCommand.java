import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.Terminal.Color;

public class HandleCommand {
    private final CSVRepresentation csvRepresentation;
    public HandleCommand(CSVRepresentation csvRepresentation){
        this.csvRepresentation = csvRepresentation;
    }

    public void set(int column, int row, String value){
      //putString(column,row,value,Terminal.Color.DEFAULT, Terminal.Color.DEFAULT);
    }
}
