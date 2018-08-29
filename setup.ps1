Copy-Item -Path .\gradlew.default -Destination .\gradlew
Copy-Item -Path .\gradlew.bat.default -Destination .\gradlew.bat
Copy-Item -Path .\gradle\wrapper\gradle-wrapper.jar.default -Destination .\gradle\wrapper\gradle-wrapper.jar
Copy-Item -Path .\gradle\wrapper\gradle-wrapper.properties.default -Destination .\gradle\wrapper\gradle-wrapper.properties

.\gradlew wrapper --gradle-version 2.9
.\gradlew setupDecompWorkspace
.\gradlew wrapper --gradle-version 3.5
.\gradlew copySrg
.\gradlew idea
.\gradlew genIntellijRuns
