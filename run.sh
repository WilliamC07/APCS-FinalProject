#!/usr/bin/env bash
# Run this bash script to run the program
# The parameter given will be passed into the program

# An csv file must be passed in for the program to work.
if [ $# -eq 0 ]
then
    echo "Give the path to the csv file (relative or absolute)"
    exit 1
fi

# Removes existing classes
rm *.class

# Compile the project
javac -cp lanterna.jar:. Main.java

# Run the project
java -cp lanterna.jar:. Main $1
