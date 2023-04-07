# Gutenberg
CS1003P4

## Compiling
    javac -Xlint:unchecked -d "<outputPath>" -cp "/cs/studres/CS1003/0-General/spark/*" *.java

## Running
For Windows replace `:` with `;`

    java -cp "<outputPath>:/cs/studres/CS1003/0-General/spark/*" --add-exports java.base/sun.nio.ch=ALL-UNNAMED --add-exports java.base/sun.security.action=ALL-UNNAMED CS1003P4 <data directory> <search term> <minimum jaccard threshold>

