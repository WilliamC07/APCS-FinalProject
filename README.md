
## How to run the program
 1. Clone the repository through your terminal and change your working directory into it.
 2. Download the lantern jar from [stuycs.org](http://www.stuycs.org/courses/apcs/k/2019-01-03) and put it in the repo.
 3. Run `bash run.sh <arguments>`
    - the argument is the path to the csv file you want to edit. It supports absolute and relative paths.

Group 19

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
