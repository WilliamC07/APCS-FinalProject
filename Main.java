import java.io.File;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

public class Main{
	public static void main(String[] args){
		processArgs(args);
	}

	private static void processArgs(String[] args){
		// One variable passed in is opening a file
		if(args.length == 1){
			Path pathToCSV = processPath(args[0]);
			new Head(pathToCSV);
		}
	}

	private static Path processPath(String input){
		Path pathToFile = null; // argument converted to usage path by the program
		String errorMessage = "Bad file: " + input;
		try{
			if(input.startsWith("/")){
				// The user gave an absolute path from /
				pathToFile = new File(input).toPath();
			}else if(input.startsWith("~")){
				// The user gave an absolute path from ~
				pathToFile = new File(System.getProperty("user.home")).toPath().resolve(input);
			}else{
				pathToFile = new File(System.getProperty("user.dir")).toPath().resolve(input);
			}
		}catch(InvalidPathException e){
			// The file the user gave is incorrect
			exitWithError(errorMessage);
		}

		// Make sure the file exists and there is something to read
		if(!Files.exists(pathToFile)){
			exitWithError(errorMessage);
		}else{
			return pathToFile;
		}

		// This will never be called
		return null;
	}

	private static void exitWithError(String error){
		System.out.println(error);
		System.exit(1); // Edit with error
	}
}
