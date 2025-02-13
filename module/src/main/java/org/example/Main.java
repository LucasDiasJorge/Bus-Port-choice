package org.example;

import com.fazecast.jSerialComm.SerialPort;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final int BAUD_RATE = 115200;
    private static final int READ_TIMEOUT_MS = 100000;
    private static final int RECONNECT_DELAY_MS = 5000;

    protected static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        logger.info("Starting serial port communication application.");

        while (true) {
            try {
                logger.info("Scanning for available serial ports...");
                SerialPort[] serialPorts = SerialPort.getCommPorts();
                SerialPort serialPort = null;

                if (serialPorts.length == 0) {
                    logger.warn("No serial ports detected. Retrying in " + RECONNECT_DELAY_MS / 1000 + " seconds...");
                    Thread.sleep(RECONNECT_DELAY_MS);
                    continue;
                }

                for (SerialPort port : serialPorts) {
                    logger.info("Found available port: {}", port.getSystemPortName());
                    port.setBaudRate(BAUD_RATE);
                    port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, READ_TIMEOUT_MS, 0);

                    if (port.openPort()) {
                        serialPort = port;
                        logger.info("Successfully opened serial port: {}", port.getSystemPortName());
                        break;
                    } else {
                        logger.error("Failed to open serial port: {}", port.getSystemPortName());
                    }
                }

                if (serialPort == null) {
                    logger.error("No usable serial ports found. Retrying in " + RECONNECT_DELAY_MS / 1000 + " seconds...");
                    Thread.sleep(RECONNECT_DELAY_MS);
                    continue;
                }

                InputStream inputStream = serialPort.getInputStream();
                byte[] buffer = new byte[1024];

                logger.info("Listening for data on serial port: {}", serialPort.getSystemPortName());
                while (true) {
                    try {
                        int bytesRead = inputStream.read(buffer);
                        if (bytesRead > 0)
                        {
                            String data = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8).trim();
                            logger.info("Data received from {}: {}", serialPort.getSystemPortName(), data);

                            if (!data.isEmpty()) {
                                // Perform action with the received data
                                logger.info("Processing data: {}", data);
                            }
                        } else {
                            logger.info("No data received from {}. Waiting for data...", serialPort.getSystemPortName());
                        }
                    } catch (Exception e) {
                        logger.error("Error reading data from {}: {}", serialPort.getSystemPortName(), e.getMessage());
                        break;
                    }
                }

                serialPort.closePort();
                logger.error("Serial port {} closed. Reconnecting in " + RECONNECT_DELAY_MS / 1000 + " seconds...", serialPort.getSystemPortName());

                Thread.sleep(RECONNECT_DELAY_MS);
            } catch (Exception e) {
                logger.fatal("Unexpected error in main loop: {}", e.getMessage());
                try {
                    Thread.sleep(RECONNECT_DELAY_MS);
                } catch (InterruptedException ie) {
                    logger.fatal("Thread interrupted during reconnection delay: {}", ie.getMessage());
                }
            }
        }
    }
}