#!/bin/bash
# Path to JavaFX SDK lib directory. Change this to match your local installation.
PATH_TO_FX="${PATH_TO_FX:-/home/argad/openjfx-25.0.3_linux-x64_bin-sdk/javafx-sdk-25.0.3/lib}"

echo "Compiling Java files..."
mkdir -p bin
javac --module-path "$PATH_TO_FX" --add-modules javafx.controls,javafx.fxml -d bin -cp "lib/*:src" $(find src -name "*.java")


echo "Copying resources..."
mkdir -p bin/view bin/style
cp src/view/*.fxml bin/view/ 2>/dev/null || true
cp src/view/*.css bin/view/ 2>/dev/null || true
cp src/style/*.css bin/style/ 2>/dev/null || true

echo "Done! You can run the app with ./run.sh"
