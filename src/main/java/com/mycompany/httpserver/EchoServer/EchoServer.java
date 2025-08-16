package com.mycompany.httpserver.EchoServer;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Echo server that accepts client connections and processes numeric input.
 */
public final class EchoServer {

    /** Server port. */
    private static final int PORT = 35000;
    /** Logger instance. */
    private static final Logger LOGGER = Logger.getLogger(EchoServer.class.getName());

    /**
     * Private constructor to prevent instantiation.
     */
    private EchoServer() {
        // Utility class
    }

    /**
     * Main method to start the echo server.
     * @param args command line arguments (not used)
     * @throws IOException if an I/O error occurs
     */
    public static void main(final String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            LOGGER.log(Level.INFO, "Echo server listening on port {0}", PORT);
            
            try (Socket clientSocket = serverSocket.accept();
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(
                         new InputStreamReader(clientSocket.getInputStream()))) {
                
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    LOGGER.log(Level.INFO, "Message: {0}", inputLine);
                    
                    try {
                        double inputLineNumber = Double.parseDouble(inputLine.trim());
                        double result = inputLineNumber * inputLineNumber;
                        String outputLine = "Respuesta: " + result;
                        out.println(outputLine);
                        if (outputLine.equals("Respuesta: Bye.")) {
                            break;
                        }
                    } catch (NumberFormatException e) {
                        LOGGER.log(Level.WARNING, "Invalid number format: {0}", inputLine);
                        out.println("Error: Invalid number format");
                    }
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Accept failed.", e);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Could not listen on port: {0}.", PORT);
        }
    }
}
