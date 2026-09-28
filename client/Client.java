package client;
import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Client
 * Classe main che permette l'esecuzione del client/Nodo
 * 
 * Questa classe ha la responsabilita di inizializzare le liste di rilevazioni token controllare l'esistenza o meno di cartelle importanti 
 * Si occupa di avviare i thread dei comandi e il trhead che funge da Server per ascoltare le richieste di download.
 */


public class Client {
    public static void main(String[] args) {


        //istanziamento delle classi e oggetti
        Scanner sc = new Scanner(System.in);
        Credenziale credenziale = new Credenziale(); // classe che si occupa di recuperare le credenzaili
        SalvaggioDati salvataggio = new SalvaggioDati();// classe che si occupa di salvare le credenzaili in locale
        ControlloDirectory controlloDirectory = new ControlloDirectory();//classe che si occupa di controllare le directory del progetto
        ChiusuraSocket chiusura = new ChiusuraSocket(); //classe che si occupa di chiudere il socket thread e programma

        controlloDirectory.controlloDirectoryRilevazioni();

        //creazione struttura dati condivisa thread safe
        Map<String,String> datiRilevazione  = new ConcurrentHashMap<>();//NOMEFILE > TOKEN
        Map<String,String> tokenSblocco  = new ConcurrentHashMap<>();//TOKEN > CHIAVE DECRITTAZIONE
        RecuperoDatiRilevazioni recuperoRilevazioni = new RecuperoDatiRilevazioni(datiRilevazione,tokenSblocco);
        recuperoRilevazioni.caricamentoLista();// carico la lista di rilevazioni locali nella struttura dati



        //creazione varaibili
        String nomeMacchina = "";
        String ip = "";
        String stato = "";
        Socket socket = null;


        //fase di acquisizione ip porta da java client/client.java 192.168.1.12 9000
        String [] credenziali = args;
        String ipAggregator= credenziali[0];
        int portaAggregator = Integer.parseInt(credenziali[1]);
        
        //fase di verifica della macchina
        //se la macchina ha un suo nome salvato, allora salto la fase di registrazione della macchina con il Master

        String controlloNome = credenziale.nomeMacchina();


        //recupero indirizzo IP
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            System.err.println("\u001B[31m[CLIENT] Impossibile recuperare l'inidrizzo ip\u001B[0m");
        }


        int portaServerRilevazione = 0;
        ServerSocket ss = null;            
        try {
            ss = new ServerSocket(0);
            portaServerRilevazione = ss.getLocalPort();
        } catch (IOException e) {
            System.err.println("\u001B[31m[CLIENT] Errore nell'apertura del server per stabilire connessioni peer to peer\u001B[0m");
            return;
        }
            
        try {
            socket = new Socket(ipAggregator, portaAggregator);
            System.out.println("\u001B[32m[CLIENT] connessione stabilita con l'Aggregator\u001B[0m");
            //fase di controllo
            if(controlloNome.isEmpty()){
                System.out.println("[CLIENT]: Inserisci il nome della macchina");
                nomeMacchina= sc.nextLine();
                stato = "NUOVA_MACCHINA";
                salvataggio.salvataggio(nomeMacchina);//salvo il nome della macchina in locale
            }else{
                stato = "MACCHINA_ESISTENTE";
                nomeMacchina = controlloNome;
                //System.out.println("Nome della macchina esistente" + stato);
            }
            
            //creazione buffer reader e writer
            BufferedReader inServer = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            PrintWriter outServer = new PrintWriter(socket.getOutputStream(),true);
            System.out.println("\u001B[32m[CLIENT] Macchina/nodo pronto all'esecuzione dei comandi \u001B[0m");

            //avvio del thread dei comandi per comunicare le azioni all'Aggregator
            ComandiClient comandi = new ComandiClient(inServer, outServer, nomeMacchina, sc, stato, ip, portaServerRilevazione, chiusura, socket, datiRilevazione, tokenSblocco, salvataggio);
            comandi.start();


            //avvio del server per effettuare la connessione P2P
            ServerRilevazioni serverRilevazioni = new ServerRilevazioni(ss,portaAggregator,inServer,outServer, datiRilevazione, tokenSblocco);
            serverRilevazioni.start();

        } catch (IOException e) {
            System.out.println("\u001B[31m[CLIENT] Impossibile stabilire una connessione con l'aggregator, indirizzo ip o porta errata\u001B[0m");            
            chiusura.chiusuraMain(socket);
        }
    }
}