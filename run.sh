#!/usr/bin/env bash
# Run this bash script to download all needed files and compile the project.
# The parameter given will be passed into the program

# Download the dependency
# curl http://www.stuycs.org/courses/apcs/k/2019-01-03/lanterna.jar -o lanterna.ja

rm *.class

# Compile the project
javac -cp lanterna.jar:. Main.java

# Run the project
java -cp lanterna.jar:. Main $1
