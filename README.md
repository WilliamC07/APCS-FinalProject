# Samilliam Edit - Group 19
## Members:
* Samson Badlia
* William Cao
## What is this program?
This is a terminal based csv editor inspired by vim. Open a csv file and see a well-formatted grid, in which you can edit a cell and perform mathematical operations.
## How to run
### Before running, make sure:
* You have java 8 or newer installed
* Using MacOS or Linux (not tested on windows)

### Instructions to run:
Use the bash script provided in the program. The first argument passed in the script is the file you want to edit or view. The given path can be either absolute (/FILE.csv or ~/FILE.csv) or relative.

`bash run.sh <PATH TO FILE>`

Alternatively, you can compile and run the program yourself. All needed dependencies are needed. 

## Commands to use
* Note: Start typing to enter a command and press enter to push the command. If a command is not valid (give letters for column index, command doesn't exist), nothing will happen.
* Scrolling: Pressing the arrow keys will move the screen around. You can press the arrow keys while typing.
* Pressing escape will quit the program and save the file
* All methods require column to be provided first then the row
> * `set <column> <row> <value>`  
> Sets the cell at the given column and row to be the value given. Note that if the cell has a value, it will be overridden without warning.
> * `undo`
> Undo the most recently done command.
> * `swap <cell 1 column> <cell 1 row> <cell 2 column> <cell 2 row>`
> Swaps the content of two cells
> * `remove <column> <row>`
> Deletes the content of the cell
> * `multiple/add/divide/subtract/average <cell 1 column row> <cell 2 column row> <place to store value column and row>`
> Performs mathematical operation on the given two cells
> * `raise <cell 1 column row> <power>`
> Performs the mathematical operation of raising the value of the cell to the given power
> * `show <column> <row>`
> Refreshes the screen to show the top left cell as the given values. This ignores negative values. 


## Development log
### 1/2/19:
#### William:
* Initialized the project by making the entry point class (Main.java and Head.java), .gitignore.
* Can convert the given terminal argument into a java.nio.Path
* Made the CSV cell and made the toString() to format the data correctly.
#### Samson:
* Finished mode, mean , median, sort, range, max, min , sum untested
### 1/3/19:
#### William:
* Made constructors private and used a static factory method to get an instance of the class.
* Moved nested CSV cell class to keep one class per file
* Can convert a string representation of the cell into a cell
#### Samson
* Changed the sort method from bubble sort to default java sort
* Tested all the methods using a driver I made
* Wrote count and product
### 1/4/19:
#### Merged read_csv_file to master, testing now
#### William:
* Made static factory method and private constructor to make creating new instances easier.
* Made CSV row extend LinkedList so we don't need to rewrite the entire class
* Allowed for reading and writing the csv file to disk.
* Finished writing method to construct a row of the CSV file given a string representation of it.
#### Samson
* Fixed methods that didn't work (median)
* Wrote more test cases in driver
* Wrote javadocs comments for the functions
### 1/5/19:
#### Merged drawing_the_screen and tableMaking
#### William
* Can show the table created by Samson onto the terminal
* Screen freshes on keystroke or when the terminal is resized to prevent flickering
* Added instructions to run the program.
* Added onto Samson code to allow data from a csv value to fit in
* Fixed the bugs I made in CSVNode and CSVRow toString and converting a string back to an object.
* Fixed the bug I made of not calling the right method (used toString() which gives the raw data(the one stored to disk) not the processed one(the one to show to the user).

#### Samson
* Made function to create a table given the cell size, and number of rows and columns that outputs to the terminal
* Made function that resizes a string in order to fit into the table
### 1/7/19:
#### William
* Made skeleton involving how commands will be stored
* Added ability to scroll using arrow keys
* Can turn user key strokes into String
### 1/8/19
#### William
* Process in printing to terminal using Screen rather than Terminal for readability and to fix most bugs from printing line by line
* Continue to enfore the idea of a parent-child controller to make the code more reabable and maintainable
* Use a stack to store changes done on the csv file
#### Samson
* Wrote function where if user writes set and parameters, it is handled (untested)
### 1/9/19
#### William
* Finished using screen.putString() to display text
* Bug fixed the command builder not erasing characters (confused backspace and delete)
* Fixed bugs in saving the csv file to disk (too many quotes and comma)
* Fixed bug of crashing the program when adding a new cell/row
#### Samson
* Wrote functions that handle the user wanting to swap the values of 2 cells as well as adding the values of multiple cells
* Added comments, both javadoc style and regular to HandleCommand
### 1/10/19
#### William
* When saving, trailing cells (those are cells that do not contain any data and are not added to make each row the same length) are not saved to disk.
* When saving, trailing rows (those are rows that are empty, or only contains comma, but must not be between rows that contain data) are not saved to disk.
* Resizing the screen no longer shows characters "randomly" placed and works on fullscreen
* Finished writing undo
### 1/12/19
#### William
* Can now show the row and column index for the user
* Added Thread.sleep() to refreshing the screen and reading user input to prevent the program from eating the cpu.
* Moves cursor to the end of what the user is typing
* If the user types a command longer than the width of the screen, the last x amount of characters will be shown along with the cursor.
### 1/13/19
#### William
* Wrote instructions and more information about the project.
* Wrote show column row command.
#### Samson
* Handled when user wants to subtract the values of 2 cells, finding the average of 2 cells, and raising the value of a cell to a power
* Can now continue the program when user adds bad input
* Created a file for use during demo
### Base product is finished, we are starting to work on the Google Sheets API this week.
### 1/14/19
#### William
* Added dependencies for the project (now includes all the Google API and lanterna). You can just use the bash script to compile and run the program
* Added request to google sheets, need to add credentials before continuing 
