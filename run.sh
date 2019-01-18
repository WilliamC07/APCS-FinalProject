#!/usr/bin/env bash
# Run this bash script to run the program
# The parameter given will be passed into the program

# Removes existing classes
rm *.class

# Compile the project
javac -cp .:dependency/* Main.java

# Run the project
java -cp .:dependency/* Main $1 $2 $3
