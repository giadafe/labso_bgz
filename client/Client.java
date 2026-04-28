package client;
import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
//recarsi nella cartella src e lanciare il comando:
// javac Client.java
// poi per eseguire il client:
// java Client 
// mi raccomando: avviare prima il server (Master) in un'altra finestra del terminale.
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

        Map<String,String> datiRilevazione  = new ConcurrentHashMap<>();
        RecuperoDatiRilevazioni recuperoRilevazioni = new RecuperoDatiRilevazioni(datiRilevazione);
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
        int porta = portaAggregator;

        //recupero indirizzo IP
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
            System.out.println("Il mio IP: " + ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        try {
            socket = new Socket(ipAggregator, portaAggregator);
            System.out.println("Connesso! Ora puoi inviare messaggi al master");

            //fase di controllo
            if(controlloNome.isEmpty()){
                System.out.println("Inserisci il nome della macchina");
                nomeMacchina= sc.nextLine();
                stato = "NUOVA_MACCHINA";
                salvataggio.salvataggio(nomeMacchina);//salvo il nome della macchina in locale
            }else{
                stato = "MACCHINA_ESISTENTE";
                nomeMacchina = controlloNome;
                System.out.println("Nome della macchina esistente" + stato);
            }
            
            //creazione buffer reader e writer
            BufferedReader inServer = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            PrintWriter outServer = new PrintWriter(socket.getOutputStream(),true);
            System.out.println("Sei connesso al master");

            //avvio del thread dei comandi
            ComandiClient comandi = new ComandiClient(inServer, outServer, nomeMacchina, sc, stato, ip, porta, chiusura, socket, datiRilevazione);
            comandi.start();
            //avvio del server per effettuare la connessione P2P
            ServerRilevazioni serverRilevazioni = new ServerRilevazioni(portaAggregator,inServer,outServer);
            serverRilevazioni.start();
        } catch (IOException e) {
            System.err.println("Errore: impossibile connettersi al master all'indirizzo porta o indirizzo sbagliato");
            chiusura.chiusuraMain(socket);
        }
    }
}