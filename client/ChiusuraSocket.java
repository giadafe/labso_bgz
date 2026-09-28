package client;

import java.io.IOException;
import java.net.Socket;


/**
 * 
 * ChiusuraSocket
 * Classe centrale permette la chiusura del socket in qualsiasi area del codice thread compresi
 */
public class ChiusuraSocket {
    public Boolean statoChiusuraSocket;


    //punto di chiusura nel thread dei comandi del client quando il client fallisce nella condivisione del nome utente e dati al master
    public void chiusuraConnessioneThreadComando(Socket socket, Boolean primoWhile, Boolean secondoWhile){
        if(primoWhile == true){
            chiusura(socket);
            primoWhile = false;
        }

        if(secondoWhile == true){
            chiusura(socket);
            secondoWhile = false;
        }

    }

    //punto di chiusura nel main quando il client fallisce con la connessione al master
    public void chiusuraMain(Socket socket){
        chiusura(socket);
    }


    public void chiusura(Socket socket){
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("\u001B[32m[CLIENT] Comunicazione chiusa con l'Aggregator\u001B[0m");            
            }
            System.out.println("\u001B[32m[CLIENT] Chiusura del programma\u001B[0m");            
            System.exit(0);

        } catch (IOException e) {
            System.out.println("\u001B[31m[CLIENT] Impossibile chiudere la connessione\u001B[0m");        
        }
    }
}
