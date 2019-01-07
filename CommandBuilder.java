import com.googlecode.lanterna.input.Key;
import static com.googlecode.lanterna.input.Key.Kind;

/**
 * Takes in character input and creates a string (which is a command).
 */
public class CommandBuilder {
    private StringBuilder builder = new StringBuilder();
    private final HandleCommand handleCommand;

    public CommandBuilder(HandleCommand handleCommand){
        this.handleCommand = handleCommand;
    }

    public void addChar(Key key){
        switch(key.getKind()){
            case Enter:
                // make the command
                System.out.println(builder);
                builder = new StringBuilder();
                break;
            case Delete:
                // Will delete the last character
                builder.deleteCharAt(builder.length() - 1);
            default:
                builder.append(key.getCharacter());
        }
    }

    @Override
    public String toString(){
        return builder.toString();
    }
}
