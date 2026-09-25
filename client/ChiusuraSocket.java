package client;

import java.io.IOException;
import java.net.Socket;

public class ChiusuraSocket {
    public Boolean statoChiusuraSocket;

    //implmentazione di una chiusura centralinizzata

    //punto di chiusura nel thread dei comandi del client quando il client fallisce nella condivisione del nome utente e dati al master
    public void chiusuraConnessioneThreadComando(Socket socket, Boolean primoWhile, Boolean secondoWhile){
        if(primoWhile == true){
            chiusura(socket);
        }

        if(secondoWhile == true){
            chiusura(socket);
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
                System.out.println("Comunicazione chiusa con il master");
            }
            System.out.println("Chiusura del programma");
            System.exit(0);

        } catch (IOException e) {
            System.out.println("Impossibile chiudere la connessione");
        }
    }
}
