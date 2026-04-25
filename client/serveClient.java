package client;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class serveClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public serveClient(Socket socketClient, BufferedReader inPeer, PrintWriter outPeer) {
        this.socket = socketClient;
        this.in = inPeer;
        this.out = outPeer;
    }

    public void esegui() {
        try {
            String richiesta = in.readLine();
            System.out.println("Il peer ha chiesto: " + richiesta);
            out.println("Dato inviato correttamente!");

            socket.close();

        } catch (Exception e) {
            System.out.println("Errore durante il servizio al client");
        }
    }
}