import java.io.*;
import java.net.*;
//import java.util.Scanner; //non utilizzato per ora
//recarsi nella cartella src e lanciare il comando:
// javac Server.java
// poi per eseguire il server:
// java Server <porta>
public class Master {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            //controlla che il programma sia avviato con un solo parametro (la porta).
            System.out.println("Scrivere a terminale: java Master <inserire numero della porta>, esempio: java Master 1234");
            return;
        }

        int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("La porta deve essere un numero intero.");
            return;
        }

        ServerSocket ss = new ServerSocket(port);
        System.out.println("ServerSocket in ascolto sulla porta " + port + "...");
        Socket socket = ss.accept(); //accetta una connessione in ingresso
        System.out.println("Client connesso!");

        socket.close();
        ss.close();
        System.out.println("Connessione chiusa.");
    }
}