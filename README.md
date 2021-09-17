# Robot Assistant
This is a real time bot that performs automatic tasks.

# Run

### Pre-requisites
- Java JDK 11 or higher is installed https://adoptopenjdsk.net/installation.html
- Test the installation with this command ```java -version```
- place your terminal in the root of the project (cd assistant)
- assign execute permissions to the mvnw file ```chmod +x mvnw```

### Run
``` ./mvnw clean install && java -jar target/assistant-1.1.jar "<TEXT_TO_LOOP>" SECONDS_TO_RUN```

* SECONDS_TO_RUN integer representing the amount of time the assistant should run for
* TEXT_TO_LOOP a text that will be typed in notepad

### Compile (experimental)
Compiled in windows using the win64 binaries from https://github.com/ReadyTalk/win64/tree/master/include
#### Build executable
```chmod +x build_dll && ./build_dll```
