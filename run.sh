#!/bin/bash
# Path to JavaFX SDK lib directory. Change this to match your local installation.
PATH_TO_FX="${PATH_TO_FX:-/home/argad/openjfx-25.0.3_linux-x64_bin-sdk/javafx-sdk-25.0.3/lib}"

java --module-path "$PATH_TO_FX" --add-modules javafx.controls,javafx.fxml -cp "bin:lib/*" AppMain

