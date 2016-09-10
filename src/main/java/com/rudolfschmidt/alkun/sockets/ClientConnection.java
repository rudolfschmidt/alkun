package com.rudolfschmidt.alkun.sockets;

import com.rudolfschmidt.alkun.constants.HttpStatus;

import java.io.*;
import java.net.Socket;

public class ClientConnection implements Runnable {

    private final Socket clientSocket;

    public ClientConnection(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
                InputStream inputStream = clientSocket.getInputStream();
                OutputStream outputStream = clientSocket.getOutputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                PrintStream printStream = new PrintStream(outputStream, true);
        ) {
            String line;
            while (!(line = bufferedReader.readLine()).isEmpty()) {
                System.out.println(line);
            }
            String data = "<H1>Welcome to the Ultra Mini-WebServer</H2>";
            write(printStream, data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void write(PrintStream printStream, String str) throws IOException {
        HttpStatus httpStatus = HttpStatus.OK_200;
        int statusCode = httpStatus.status();
        String reasonPhrase = httpStatus.getReasonPhrase();
        printStream.printf("%s %s %s %n", "HTTP/1.1", statusCode, reasonPhrase);

        byte[] data = str.getBytes();
        printStream.println("Content-Length: " + data.length);
        printStream.println("");
        printStream.write(data);
    }
}