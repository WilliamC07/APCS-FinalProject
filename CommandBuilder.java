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

    /**
     * Each keystroke the user presses (that is a alphabet, space, enter, or backspace ) should be be passed into this.
     * Enter key means the user string being typed is completed and the command the user gave will be created.
     * @param key The user's keystroke
     */
    public void addChar(Key key){
        switch(key.getKind()){
            case Enter:
                // make the command
                handleCommand.handle(builder.toString());
                builder = new StringBuilder();
                break;
            case Backspace:
                // Will delete the last character (won't delete if there is nothing)
                if(builder.length() != 0){
                    builder.deleteCharAt(builder.length() - 1);
                }
                break;
            default:
                builder.append(key.getCharacter());
        }
    }

    /**
     * Get the string the user is currently typing.
     * @return String the user is currently typing.
     */
    @Override
    public String toString(){
        return builder.toString();
    }
}
