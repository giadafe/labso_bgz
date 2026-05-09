package master;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

/*
* =================
* GestioneClient
* =================
* Thread che gestisce le richieste in entrata dai client peer.
*/


public class GestioneClient extends Thread {
    //private GestoreLog gestoreLog;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private HashMap<String, String> infoMacchinaClient;
    private HashMap<String, List<String>> infoRilevazioni;


    public GestioneClient(Socket socket, BufferedReader in, PrintWriter out,
            HashMap<String, String> infoMacchinaClient, HashMap<String, List<String>> infoRilevazioni) {
        //DA AGGIUNGERE GESTORE LOG 
    }

    @Override
    public void run() {
    //DA IMPLEMENTARE Boolean statoRecuperoLista = true; //stato per il primo while
    //da fare :
    //recuperare stato della macchina usando infomacchinaclient
    //nome ip porta
    //recupero stato client per capire se è nuovo o già esistente
    //confrontare nome hashmap infomacchinaclient con nome macchina del client
    //se esiste mandare nome già esistente e dire al client di scegliere un altro nome

    /*
    * =================
    * Fase di avvio del secondo ciclo while
    * =================
    * 
    */
    String raccoltaInput; //dichiaro variabile input

    boolean gestioneRichieste = true; //stato per il secondo while
    while (gestioneRichieste && (raccoltaInput=in.readline())) {
        if (raccoltaInput.contains("listdata local")
          out.println("accesso_lista_local");
        ) else if (raccoltaInput.contains("listdata remote")
    /*
        * =================
        * Comando listdata remote
        * =================
        * Gestione della richiesta di lista delle risorse remote condivise.
    */
        ) else if (raccoltaInput.contains("add")
     /*
        * =================
        * Comando add
        * =================
        * Gestione la fase di aggiunta di una nuova risorsa condivisa dal client.
    */
        ) else if (raccoltaInput.contains("download")
    /*
        * =================
        * Comando download
        * =================
        * Gestione della richiesta di download di una risorsa da parte del client.
    */
        ) else if (raccoltaInput.contains("quit")
     /*
        * =================
        * Comando quit
        * =================
        * Gestione della disconnessione del client.
        * 
    */ 
          gestioneRichieste = false; //esco dal ciclo while
        ) else {
            System.out.println("Comando non valido: " + raccoltaInput);
        }

















    }









}

