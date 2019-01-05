## How to run the program
1. Clone the repository through your terminal and change your working directory into it.
2. Download the lantern jar from [stuycs.org](http://www.stuycs.org/courses/apcs/k/2019-01-03) and put it in the repo.
3. Run `bash run.sh <arguments>`
... the argument is the path to the csv file you want to edit. It supports absolute and relative paths. 

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
#### Samson
