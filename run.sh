#!/usr/bin/env bash
# Run this bash script to download all needed files and compile the project.
# The parameter given will be passed into the program

# Download the dependency
# curl http://www.stuycs.org/courses/apcs/k/2019-01-03/lanterna.jar -o lanterna.ja

# An csv file must be passed in for the program to work.
if [ $# -eq 0 ]
then
    echo "Give the path to the csv file (relative or absolute)"
    exit 1
fi

rm *.class

# Compile the project
javac -cp lanterna.jar:. Main.java

# Run the project
java -cp lanterna.jar:. Main $1
