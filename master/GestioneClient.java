package master;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

/*
* ==================
* GestioneClient
* =================
* Thread che gestisce le richieste in entrata dai client peer.
*/


public class GestioneClient extends Thread {

    public GestioneClient(Socket socket, BufferedReader in, PrintWriter out,
            HashMap<String, String> infoMacchinaClient, HashMap<String, String> infoRilevazioni) {
        //DA AGGIUNGERE GESTORE LOG
    }

    @Override
    public void run() {
    //DA IMPLEMENTARE
    }









}

