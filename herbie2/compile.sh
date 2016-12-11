#!/bin/bash
#
# Build and package all java files in the src directory

echo "Cleanup of previous build ..."
find bin -name *.class -exec rm -f '{}' \;
rm -rf bin
rm -rf target
rm -f ProjectFileList

mkdir target
mkdir bin

echo "Build list of files in project ..." 
find src/main/java -name *.java > ProjectFileList

echo "Compile Java code ..."
javac @ProjectFileList -d bin -classpath bin:/opt/pi4j/lib/'*' -sourcepath org:com

echo "Build library herbie-1.0.jar "
jar -cf target/herbie-1.0.jar -C bin/ .

