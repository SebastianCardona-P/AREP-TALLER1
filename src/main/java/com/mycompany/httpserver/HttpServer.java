package com.mycompany.httpserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.BufferedReader;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * HTTP Server implementation that serves static files and REST services.
 */
public final class HttpServer {

    /** Server port number. */
    private static final int PORT = 35000;
    /** Base path for resources. */
    private static final String BASEPATH = "src/main/java/resources/";
    /** HTTP 200 OK response header. */
    private static final String HTTP_200_OK = "HTTP/1.1 200 OK\r\n";
    /** HTTP 404 Not Found response header. */
    private static final String HTTP_404_NOT_FOUND = "HTTP/1.1 404 Not Found\r\n";
    /** HTTP content separator. */
    private static final String HTTP_SEPARATOR = "\r\n";
    /** Logger instance. */
    private static final Logger LOGGER = Logger.getLogger(HttpServer.class.getName());

    /**
     * Private constructor to prevent instantiation.
     */
    private HttpServer() {
        // Utility class
    }

    /**
     * The main method to create the http server.
     * @param args command line arguments
     * @throws IOException if I/O error occurs
     * @throws URISyntaxException if URI syntax error occurs
     */
    public static void main(final String[] args)
            throws IOException, URISyntaxException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Could not listen on port: {0}.", PORT);
            System.exit(1);
        }
        Socket clientSocket = null;

        boolean running = true;
            
        // Recibe más de una solicitud
        while (running) {

            try {
                // Acepta la solicitd del cliente 
                LOGGER.info("Ready to receive...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Accept failed.");
                System.exit(1);
            }
            
            // create the IO streams
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine;

            boolean isFirstLine = true;
            URI requestUri = null;

            while ((inputLine = in.readLine()) != null) {

                if (isFirstLine) {
                    // get the URI
                    requestUri = new URI(inputLine.split(" ")[1]);
                    LOGGER.log(Level.INFO, "Path: {0}", requestUri.getPath());
                    isFirstLine = false;
                }
                LOGGER.log(Level.INFO, "Received: {0}", inputLine);
                if (!in.ready()) {
                    break;
                }
            }
            
            handlerequestType(requestUri, out, clientSocket.getOutputStream());

            out.close();
            in.close();
            clientSocket.close();

        }
        serverSocket.close();
    }
    
    /**
     * Handle the request and its response by reading its file type.
     * @param requestUri the URI of the request
     * @param out the output writer
     * @param outputStream the output stream for binary data
     */
    private static void handlerequestType(final URI requestUri,
                                          final PrintWriter out,
                                          final OutputStream outputStream) {
        if (requestUri.getPath().endsWith(".html")
                || requestUri.getPath().equalsIgnoreCase("/")) {
            getHTML(requestUri, out);
        } else if (requestUri.getPath().endsWith(".css")) {
            getCSS(requestUri, out);
        } else if (requestUri.getPath().endsWith(".js")) {
            getJS(requestUri, out);
        } else if (requestUri.getPath().startsWith("/app/hello")) {
            helloService(requestUri, out);
        } else if (requestUri.getPath().endsWith(".jpeg")
                || requestUri.getPath().endsWith(".jpg")
                || requestUri.getPath().endsWith(".png")
                || requestUri.getPath().endsWith(".ico")) {
            try {
                getImage(requestUri, outputStream);
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "Error handling image request", ex);
            }
        } else {
            notFound(out);
        }
    }

    /**
     * Handle html responses.
     * @param requestUri the request URI
     * @param out the output writer
     */
    private static void getHTML(final URI requestUri, final PrintWriter out) {
        StringBuilder outputLine = new StringBuilder();
        outputLine.append(HTTP_200_OK)
                .append("Content-Type: text/html")
                .append(HTTP_SEPARATOR)
                .append(HTTP_SEPARATOR);

        // create the file path
        String file = requestUri.getPath().equalsIgnoreCase("/")
                ? BASEPATH + "index.html"
                : BASEPATH + requestUri.getPath();

        File realFile = new File(file);
        if (!realFile.exists()) {
            notFound(out);
            return;
        }

        // start reading the file
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String fileLine;
            while ((fileLine = reader.readLine()) != null) {
                outputLine.append(fileLine).append("\n");
            }
        } catch (IOException e) {
            System.getLogger(HttpServer.class.getName())
                    .log(System.Logger.Level.ERROR, "Error reading HTML file", e);
        }

        out.write(outputLine.toString());
    }

    /**
     * Handle css responses.
     * @param requestUri the request URI
     * @param out the output writer
     */
    private static void getCSS(final URI requestUri, final PrintWriter out) {
        StringBuilder outputLine = new StringBuilder();
        outputLine.append(HTTP_200_OK)
                .append("Content-Type: text/css")
                .append(HTTP_SEPARATOR)
                .append(HTTP_SEPARATOR);

        String file = BASEPATH + requestUri.getPath();

        File realFile = new File(file);
        if (!realFile.exists()) {
            notFound(out);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String fileLine;
            while ((fileLine = reader.readLine()) != null) {
                outputLine.append(fileLine).append("\n");
            }
        } catch (IOException e) {
            System.getLogger(HttpServer.class.getName())
                    .log(System.Logger.Level.ERROR, "Error reading CSS file", e);
        }

        out.write(outputLine.toString());
    }

    /**
     * Handle javascript responses.
     * @param requestUri the request URI
     * @param out the output writer
     */
    private static void getJS(final URI requestUri, final PrintWriter out) {
        StringBuilder outputLine = new StringBuilder();
        outputLine.append(HTTP_200_OK)
                .append("Content-Type: text/javascript")
                .append(HTTP_SEPARATOR)
                .append(HTTP_SEPARATOR);

        String file = BASEPATH + requestUri.getPath();

        File realFile = new File(file);
        if (!realFile.exists()) {
            notFound(out);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String fileLine;
            while ((fileLine = reader.readLine()) != null) {
                outputLine.append(fileLine).append("\n");
            }
        } catch (IOException e) {
            System.getLogger(HttpServer.class.getName())
                    .log(System.Logger.Level.ERROR, "Error reading JS file", e);
        }

        out.write(outputLine.toString());
    }

    /**
     * Handles the REST hello service.
     * @param requestUri the URI of the request
     * @param out the output stream to write response to
     */
    private static void helloService(final URI requestUri, final PrintWriter out) {
        StringBuilder response = new StringBuilder();
        response.append(HTTP_200_OK)
                .append("Content-Type: application/json")
                .append(HTTP_SEPARATOR)
                .append(HTTP_SEPARATOR);
        
        String[] queryNameValue = requestUri.getQuery().split("=");
        String queryValue = queryNameValue.length > 1 ? queryNameValue[1] : "";
        response.append("{\"mensaje\": \"Hola ").append(queryValue).append("\"}");

        out.write(response.toString());
    }

    /**
     * hanlde image response
     * @param requestUri
     * @param out
     * @throws IOException 
     */
    private static void getImage(URI requestUri, OutputStream out) throws IOException {
        String path = requestUri.getPath();
        //File extension
        String fileExtension = path.substring(path.lastIndexOf(".") + 1).toLowerCase();

        String file = path.startsWith("/images/") ? BASEPATH + path : BASEPATH + "images/" + path;

        File realFile = new File(file);

        if (!realFile.exists()) {
            PrintWriter outPrint = new PrintWriter(out, true);
            notFound(outPrint);
            return;
        }

        //response header
        StringBuilder outputLine = new StringBuilder();
        outputLine.append(HTTP_200_OK)
                .append("Content-Type: image/").append(fileExtension).append(HTTP_SEPARATOR)
                .append("Content-Length: ").append(realFile.length()).append(HTTP_SEPARATOR)
                .append(HTTP_SEPARATOR);

        try {
            //Write headers as a text
            out.write(outputLine.toString().getBytes(StandardCharsets.UTF_8));
        } catch (UnsupportedEncodingException ex) {
            System.getLogger(HttpServer.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

        //write content binary of image
        try (FileInputStream fileInputStream = new FileInputStream(realFile); BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream)) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
        out.flush();
    }

    /**
     * Handles 404 Not Found errors.
     * @param out the output stream to write response to
     */
    private static void notFound(final PrintWriter out) {
        StringBuilder response = new StringBuilder();
        response.append(HTTP_404_NOT_FOUND)
                .append("Content-Type: text/plain")
                .append(HTTP_SEPARATOR)
                .append(HTTP_SEPARATOR)
                .append("404 Not Found");
        
        out.write(response.toString());
    }
}
