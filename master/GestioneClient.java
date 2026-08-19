package master;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
* =================
* GestioneClient
* =================
* Thread che gestisce le richieste in entrata dai client peer.
*/


public class GestioneClient extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private HashMap<String, String> infoMacchinaClient;
    private HashMap<String, List<String>> infoRilevazioni;
    private GestoreLog gestoreLog;
    private String nomeMacchina = "";


    public GestioneClient(Socket socket, BufferedReader in, PrintWriter out,
            HashMap<String, String> infoMacchinaClient, HashMap<String, List<String>> infoRilevazioni,
            GestoreLog gestoreLog) {
        //inizializzo variabili
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.infoMacchinaClient = infoMacchinaClient;
        this.infoRilevazioni = infoRilevazioni;
        this.gestoreLog = gestoreLog;
    }

    @Override
    public void run() {
    //da fare :
    //recuperare stato della macchina usando infomacchinaclient
    
    //nome ip porta
    //recupero stato client per capire se è nuovo o già esistente
    //confrontare nome hashmap infomacchinaclient con nome macchina del client
    //se esiste mandare nome già esistente e dire al client di scegliere un altro nome


    /*
    * =================
    * Fase di avvio del primo ciclo while
    * =================
    * Il ciclo permette láutenticazione della macchina/nodo 
    * -se il nodo esiste, allora procede ad aggiornare le credenziali di quel nodo, quindi IP e PORTA
    * -se il nodo e nuovo allora crea la entry, come new nomeNodo IP PORTA
    */

    GestioneMacchine gestione = new GestioneMacchine(infoMacchinaClient); // classe che si occupa di salvare i dati della macchina allínterno della struttura dati
    boolean primoWhile = true;
    while(primoWhile){
            try {
                System.out.println("[SERVER] In attesa dello stato dal client...");
                String statoClient = in.readLine();
                System.out.println(statoClient);

                if(statoClient == null) {
                    System.out.println("[SERVER] Connessione interrotta dal client.");
                    break;
                }

                if(statoClient.equals("NUOVA_MACCHINA")){
                    System.out.println("[SERVER] LA MACCHINA E NUOVA, DEVE ESSERE REGISTRATA");
                    try {
                        String outnomeMacchina = in.readLine();
                        System.out.println("[SERVER] Ricevuto nome macchina da verificare: " + outnomeMacchina);
                        //controllo il nome cone le chiavi degli utenti esistenti nella lista concorrente
                        if(infoMacchinaClient.containsKey(outnomeMacchina)){
                            System.out.println("[SERVER] LA MACCHINA E NUOVA, MA CON IL NOME DUPLICATO: " + outnomeMacchina);
                            out.println("nomeMacchina_DUPLICATO");
                        }else{
                            System.out.println("[SERVER] LA MACCHINA E NUOVA, MA IL NOME E NUOVO: " + outnomeMacchina);
                            out.println("nomeMacchina_VALIDA"); 
                            this.nomeMacchina = outnomeMacchina;
                            System.out.println("[SERVER] In attesa delle credenziali per " + outnomeMacchina + "...");
                            String credenziali = in.readLine();
                            String [] divisione = credenziali.split(" ");
                            System.out.println("[SERVER] Ricevute credenziali: IP=" + divisione[0] + " PORTA=" + divisione[1]);
                            String credenzialiFormattate = divisione[0] + ":" + divisione[1]; //unione delle credenziali con :per la leggibilita
                            //salvataggio nella hashmap 
                            infoMacchinaClient.put(outnomeMacchina, credenzialiFormattate);
                            //TEST salvataggio in locale
                            gestione.salvaMacchine();
                            System.out.println("[DATABASE] Macchina registrata con successo: " + outnomeMacchina);
                            primoWhile = false;
                        }
                    } catch (IOException e) {
                        System.err.println("[ERRORE] Errore durante la lettura del nome o delle credenziali.");
                        e.printStackTrace();
                    }
                }else if(statoClient.equals("MACCHINA_ESISTENTE")){
                    //recuperiamo nome ip porta
                    String outnomeMacchinaDaAggiornare = in.readLine();
                    System.out.println("[SERVER] Ricevuto nome macchina da verificare: " + outnomeMacchinaDaAggiornare);
                    this.nomeMacchina = outnomeMacchinaDaAggiornare;
                    //aggioriamo ip e porta del nome per via dell' ip dinamico delle nostre reti
                    String credenzialiDaAggiornare = in.readLine();
                    // Legge le credenziali ricevute dal client e le trasforma in formato "ip:porta"
                    // Se la stringa non è nulla, pulisce eventuali spazi e la divide in parti
                    String[] divisioneCredenziali = credenzialiDaAggiornare != null
                            ? credenzialiDaAggiornare.trim().split("\\s+")
                            : new String[0];
                    if (divisioneCredenziali.length >= 2) {
                        // Se sono presenti entrambi i valori, li unisce
                        credenzialiDaAggiornare = divisioneCredenziali[0] + ":" + divisioneCredenziali[1];
                    } else {
                        // Se i dati sono incompleti, usa un valore di default 
                        credenzialiDaAggiornare = "0.0.0.0:0";
                    }
                    infoMacchinaClient.put(outnomeMacchinaDaAggiornare, credenzialiDaAggiornare);
                    gestione.salvaMacchine();
                    primoWhile = false;

                }
            } catch (IOException e) {
                System.err.println("[ERRORE] Errore di comunicazione con il client.");
                e.printStackTrace();
            } 
        }



        System.out.println("[SERVER] Fase di registrazione conclusa, pronto per il recupero dei dati");




    /*
        * =================
        * Fase di avvio del secondo ciclo while 
        * =================
        * Il ciclo permette l'acquisizione delle risorse
        * -se il client notifica con fine_condivisione il ciclo termina e sblocca il controller 
        * -se ci sono i dati da ricevere allora il tutto viene revisionato in modod da ottenere esattamente nomeRilevazione e contenuto
        * -il contenuto viene riscritto in un unica stringa se nomeRilevazione esiste gia come chiave,
        *  allora viene inserita la lista con al suo interno i valori come nomeMacchina, token, disponibilita.
        *  se non esiste allora verra inserita la chiave e una lista vuota che ad ogn iiterazione verra riempita con il nuovo valore.
        * 
    */
        out.println("inizio_fase_rilevazioni");
        out.flush();

        Boolean whileRiceviDati = true;
        String letturaRilevazioni;

        try {
            while ((letturaRilevazioni = in.readLine()) != null && whileRiceviDati) {
                //condizione d'uscita
                if (letturaRilevazioni.equals("fine_condivisione")) {
                    whileRiceviDati = false;
                    break;
                }



                String[] scomposizione = letturaRilevazioni.split(",");
                if (scomposizione.length < 2) {
                  continue;
                }

                String nomeRilevazione = scomposizione[0];
                String contenuto = scomposizione[1];
                String composizioneStringa = this.nomeMacchina + ":" + contenuto + ":DISPONIBILE";

                List<String> lista = infoRilevazioni.get(nomeRilevazione);

                if (lista == null) {
                    lista = new ArrayList<>();
                    infoRilevazioni.put(nomeRilevazione, lista);
                }
                lista.add(composizioneStringa);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("[SERVER] Fase di acquisizione dei dati conclusa, pronto perrendere disponibile le sue risorse ad altri client");





        //fase per mettere a disponibile tutte le risorse di un determinato client connesso.
        for(String chiave : infoRilevazioni.keySet()) {
            List<String> valoriDaMettereDisponibili = infoRilevazioni.get(chiave); //riferimento della lista
            for(int i = 0; i< valoriDaMettereDisponibili.size(); i++){
                String valore = valoriDaMettereDisponibili.get(i);
                String divisioneValori [] = valore.split(":");
                String nomeMAcchinaCheck = divisioneValori[0];
                String statoDisponibilita = divisioneValori[2];
                if(nomeMAcchinaCheck.equals(this.nomeMacchina) && statoDisponibilita.equals("NON_DISPONIBILE")){
                    //imposto a DISPONIBILE la rilevazione 
                    String valoreAggironato  = nomeMAcchinaCheck +":"+ divisioneValori[1]+":"+ "DISPONIBILE";
                    //aggiornamento
                    valoriDaMettereDisponibili.set(i, valoreAggironato);
                }
            }
        }
    System.out.println("[SERVER] Fase di aggiornamento dei dati conclusa, pronto per attivare i comandi per il client");









    
    /*
    * =================
    * Fase di avvio del secondo ciclo while
    * =================
    * 
    */
        String raccoltaInput; //dichiaro variabile input

        boolean gestioneRichieste = true; //stato per il secondo while
        try {
            System.out.println("Controller attivo");
            while (gestioneRichieste && (raccoltaInput=in.readLine())!=null) {
                System.out.println("Comando ricevuto:  "+ raccoltaInput);

                if(raccoltaInput.equals("listdata local")){
                    out.println("accesso_lista_local");
                    out.flush();
                } else if (raccoltaInput.contains("listdata remote")){
                    /*
                     * =================
                     * Comando listdata remote
                     * =================
                     * Invia al client la lista delle risorse condivise (chiavi di infoRilevazioni)
                     */
                    if (infoRilevazioni == null || infoRilevazioni.isEmpty()) {
                        out.println("FINE_LISTA");
                        out.flush();
                    } else {
                        for (String risorsa : infoRilevazioni.keySet()) {
                            out.println(risorsa);
                        }
                        out.println("FINE_LISTA");
                        out.flush();
                    }
                }else if (raccoltaInput.contains("add")){
            /*
                * =================
                * Comando add
                * =================
                * Gestione la fase di aggiunta di una nuova risorsa condivisa dal client.
            */
                }else if (raccoltaInput.contains("download")){
            /*
                * =================
                * Comando download
                * =================
                * Gestione della richiesta di download di una risorsa da parte del client.
            */
                    String[] richiesta = raccoltaInput.split(",", 2);
                    String risorsa = richiesta.length == 2 ? richiesta[1].trim() : "";
                    if (!risorsa.isEmpty()) {
                        String peerPossessore = trovaPossessore(risorsa);
                        gestoreLog.logDownload(risorsa, nomeMacchina, peerPossessore);
                    }
                }else if (raccoltaInput.contains(",")) {
                    String[] nuovaRisorsa = raccoltaInput.split(",", 2);
                    String nomeRisorsa = nuovaRisorsa[0].trim();
                    String token = nuovaRisorsa[1].trim();
                    if (!nomeRisorsa.isEmpty() && !token.isEmpty()) {
                        synchronized (infoRilevazioni) {
                            List<String> risorse = infoRilevazioni.get(nomeRisorsa);
                            if (risorse == null) {
                                risorse = new ArrayList<>();
                                infoRilevazioni.put(nomeRisorsa, risorse);
                            }
                            risorse.add(nomeMacchina + ":" + token + ":DISPONIBILE");
                        }
                    }
                }else if (raccoltaInput.contains("quit")){
            /*
                * =================
                * Comando quit
                * =================
                * Gestione della disconnessione del client.
                * 
            */ 
                gestioneRichieste = false; //esco dal ciclo while
                }else{
                    System.out.println("Comando non valido: " + raccoltaInput);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String trovaPossessore(String nomeRisorsa) {
        String nomeRisorsaNormalizzato = nomeRisorsa.trim();
        List<String> risorse = infoRilevazioni.get(nomeRisorsaNormalizzato);
        if (risorse == null) {
            for (String nomeRegistrato : infoRilevazioni.keySet()) {
                if (nomeRegistrato.trim().equalsIgnoreCase(nomeRisorsaNormalizzato)) {
                    risorse = infoRilevazioni.get(nomeRegistrato);
                    break;
                }
            }
        }
        if (risorse == null) {
            return "risorsa non trovata";
        }

        for (String risorsa : risorse) {
            String[] dati = risorsa.split(":");
            if (dati.length >= 3 && !dati[0].trim().isEmpty()) {
                return dati[0];
            }
        }
        return "nessun peer disponibile";
    }
}
