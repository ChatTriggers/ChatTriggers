#!/bin/sh
cp ./gradlew.default ./gradlew
cp ./gradlew.bat.default ./gradlew.bat
cp ./gradle/wrapper/gradle-wrapper.jar.default ./gradle/wrapper/gradlew-wrapper.jar
cp ./gradle/wrapper/gradle-wrapper.properties.default ./gradle/wrapper/gradle-wrapper.properties
sudo chmod a+x ./gradlew
./gradlew wrapper --gradle-version 2.9
./gradlew setupDecompWorkspace
./gradlew wrapper --gradle-version 3.5
./gradlew copySrg idea genIntellijRuns
