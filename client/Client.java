package client;
import java.io.*;
import java.net.*;
import java.util.Scanner;
//recarsi nella cartella src e lanciare il comando:
// javac Client.java
// poi per eseguire il client:
// java Client 
// mi raccomando: avviare prima il server (Master) in un'altra finestra del terminale.
public class Client {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Inserisci l'indirizzo IP a cui connetterti: ");
        String ip = scanner.nextLine();
        System.out.print("Inserisci la porta a cui connetterti: ");
        int port = scanner.nextInt();
        scanner.nextLine();

        Socket socket = null;
        try {
            socket = new Socket(ip, port);
            System.out.println("Connesso! Ora puoi inviare messaggi al master. Digita 'quit' per terminare.");

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            while (true) {
                System.out.print("Scrivi il messaggio da inviare al master: ");
                String messaggio = scanner.nextLine();
                out.println(messaggio); // invia il messaggio come riga di testo
                if (messaggio.equalsIgnoreCase("quit")) {
                    break;
                }
            }

            out.close();
            System.out.println("Connessione chiusa.");
        } catch (IOException e) {
            System.err.println("Errore: impossibile connettersi al master all'indirizzo " + ip + " e porta " + port);
        } finally {
            if (socket != null && !socket.isClosed()) {
                try {
                    socket.close();
                } catch (IOException ex) {
                    // Ignora eventuali errori di chiusura
                }
            }
        }
    }
}