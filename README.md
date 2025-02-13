# Bus Port Choice

## Overview
Bus Port Choice is a Java application that continuously scans and connects to available serial ports, listening for incoming data. It utilizes the `jSerialComm` library for serial communication and `log4j` for logging.

## Features
- Scans for available serial ports.
- Connects to the first available port with a baud rate of 115200.
- Listens for incoming data and logs received messages.
- Handles disconnections and automatically attempts to reconnect.
- Implements error handling and logging for debugging purposes.

## Requirements
- Java 11 or later
- [jSerialComm](https://fazecast.github.io/jSerialComm/)
- [Apache Log4j 2](https://logging.apache.org/log4j/2.x/)

## Usage
- The application runs in an infinite loop, scanning for serial ports.
- If no ports are found, it waits 5 seconds before retrying.
- Once a port is opened, the program listens for data and logs incoming messages.
- In case of errors, the program logs them and attempts to reconnect.

## Configuration
The following parameters can be adjusted in the `Main` class:
- `BAUD_RATE` (default: 115200)
- `READ_TIMEOUT_MS` (default: 100000 ms)
- `RECONNECT_DELAY_MS` (default: 5000 ms)
