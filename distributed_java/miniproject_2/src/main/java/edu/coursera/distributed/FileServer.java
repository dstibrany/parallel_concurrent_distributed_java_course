package edu.coursera.distributed;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * A basic and very limited implementation of a file server that responds to GET
 * requests from HTTP clients.
 */
public final class FileServer {
    /**
     * Main entrypoint for the basic file server.
     *
     * @param socket Provided socket to accept connections on.
     * @param fs A proxy filesystem to serve files from. See the PCDPFilesystem
     *           class for more detailed documentation of its usage.
     * @throws IOException If an I/O error is detected on the server. This
     *                     should be a fatal error, your file server
     *                     implementation is not expected to ever throw
     *                     IOExceptions during normal operation.
     */
    public void run(final ServerSocket socket, final PCDPFilesystem fs)
            throws IOException {
        /*
         * Enter a spin loop for handling client requests to the provided
         * ServerSocket object.
         */
        while (true) {
            Socket clientSocket = socket.accept();
            InputStream stream = clientSocket.getInputStream();
            InputStreamReader reader = new InputStreamReader(stream);
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine();
            String[] firstLineSplit = line.split("\\s+");
            PCDPPath filePath = new PCDPPath(firstLineSplit[1]);
            String fileContents = fs.readFile(filePath);
            OutputStream out = clientSocket.getOutputStream();

            if (fileContents == null) {
                out.write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes());
            } else {
                String outputStr = "HTTP/1.0 200 OK\r\nServer: FileServer\r\n\r\n" + fileContents + "\r\n\r\n";
                out.write(outputStr.getBytes());
            }

            out.close();
            br.close();
            clientSocket.close();

        }
    }
}
