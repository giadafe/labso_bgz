package client;

import client.comandi.Add;
import client.comandi.Download;
import client.comandi.InvioRilevazioni;
import client.comandi.ListDataRemote;
import client.comandi.listDataLocal;//test chiamata classe da un altra cartella

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;

/**
 * 
 * ComandiClient
 * Classe che si occupa dello scambio di risorse e aggiornamento delle credenziali in caso di indirizzi ip dinamici
 * permette la comunicazione con il Client/Nodo all'Aggregator
 */

public class ComandiClient extends Thread {

    public BufferedReader inServer;
    public PrintWriter outServer;
    public String nomeMacchina;
    public Scanner sc; 
    public String stato;
    public String ip;
    public int porta;
    private volatile boolean primowhile = true;
    private volatile boolean secondowhile = false;
    public ChiusuraSocket chiusura;
    public Socket socket;
    public Map<String,String> datiRilevazione;
    public Map<String,String> tokenSblocco;
    public SalvaggioDati salvataggio;

    public ComandiClient(BufferedReader inServer, PrintWriter outServer, String nomeMacchina, Scanner sc, String stato, String ip, int porta, ChiusuraSocket chiusura, Socket socket, Map<String,String> datiRilevazione, Map<String,String> tokenSblocco, SalvaggioDati salvataggio) {
        this.nomeMacchina = nomeMacchina;
        this.inServer = inServer;
        this.outServer = outServer;
        this.sc= sc;  
        this.stato=stato;  
        this.ip = ip;
        this.porta =porta;
        this.chiusura=chiusura;
        this.socket=socket;
        this.datiRilevazione= datiRilevazione;
        this.tokenSblocco= tokenSblocco;
        this.salvataggio = salvataggio;
    }
    
    @Override
    public void run(){
        InvioRilevazioni inviaDati = new InvioRilevazioni();
        System.out.println("\u001B[32m[CLIENT] Comandi avviati\u001B[0m");        
        while(primowhile){
            //mando lo stato della macchina 
            System.out.println(nomeMacchina);
            try {
                if(stato.equals("NUOVA_MACCHINA")){
                    outServer.println(stato);
                    outServer.println(nomeMacchina);
                    
                    String responseAggregator = inServer.readLine();
                    
                    if(responseAggregator.equals("nomeMacchina_DUPLICATO")){
                        System.out.println("\u001B[33m[CLIENT] Nome duplicato, inserire un nuovo nome:\u001B[0m");                        
                        //reinserisci il nome
                        nomeMacchina = sc.nextLine();
                    }else if(responseAggregator.equals("nomeMacchina_VALIDA")){
                        outServer.println(ip +" "+porta);
                        System.out.println("\u001B[32m[CLIENT] Registrazione avvenuta con successo\u001B[0m");

                        primowhile = false;
                        secondowhile = true;
                    }
                    
                }else if(stato.equals("MACCHINA_ESISTENTE")){
                    outServer.println(stato);
                    outServer.println(nomeMacchina);
                    outServer.println(ip +" "+ String.valueOf(porta));   //aggiornamento ip porta per IP dinamici             
                    primowhile = false;
                    System.out.println("\u001B[32m[CLIENT] Aggiornamento delle credenziali avvenuta correttamente\u001B[0m");
                    secondowhile = true;
                }
            } catch (IOException e) {
                System.err.println("\u001B[31m[CLIENT] Errore nella fase di aggiornamento \u001B[0m");                
                //chiusura del socket
                chiusura.chiusuraConnessioneThreadComando(socket,primowhile,secondowhile);
            }
        }

        System.out.println("\u001B[32m[CLIENT] Fase di registrazione conclusa, pronto per mandare i dati\u001B[0m");        
        String start;
        try {
            start = inServer.readLine();
            if(!"inizio_fase_rilevazioni".equals(start)) {
                System.out.println("\u001B[31m[CLIENT]Errore di desincronizzaione\u001B[0m");            
                return;
            }
        } catch (IOException e) {
                System.out.println("\u001B[31m[CLIENT]Errore di desincronizzaione\u001B[0m");            
        }

        //serve per mandare i dati della rilevazione al master sfruttando la calsse che ha creato blerta
        inviaDati.invia(outServer, datiRilevazione);
        










        //comandi
        while(secondowhile){
            System.out.println("\u001B[32m[COMANDI] Comandi attivi\u001B[0m");
            System.out.println("Inserisci i comandi:");
            System.out.println("> listdata local");
            System.out.println("> listdata remote");
            System.out.println("> download");
            System.out.println("> quit");



            String comando = sc.nextLine();

            if(comando.contains("listdata local")){
                //accedere alle registrazioniu locali
                    listDataLocal listalocale = new listDataLocal();
                    listalocale.mostraLista(datiRilevazione, outServer,comando, inServer);
                /**
                 * 
                 * 
                //INIZIO TEST per vedere se la chiusura del programma avviene se il socket viene chiuso dal server 
                outServer.println(comando);
                String response;
                try {
                    response = inServer.readLine();
                    if(response==null){
                        chiusura.chiusuraConnessioneThreadComando(socket,primowhile,secondowhile);
                    }
                } catch (IOException e) {
                        chiusura.chiusuraConnessioneThreadComando(socket,primowhile,secondowhile);
                }
                //FINE TEST
                 * 
                 * 
                 * 
                 * 
                 * 
                 */





            }else if(comando.contains("listdata remote")){
                //fare una get al server
                ListDataRemote dataRemote = new ListDataRemote();
                dataRemote.esegui(outServer, inServer ,comando);



            }else if(comando.contains("add")){
                //aggiungere nome registrazione + valore tutto in String
                String scomposizioneComando [] = comando.split(" ",3);// salvo in blocchi di 3 le stringhe 
                if(scomposizioneComando.length != 3){
                    System.out.println("\u001B[31m[CLIENT] comando errato\u001B[0m");                    
                    continue;
                }else{
                    Add addRilevazioni = new Add(scomposizioneComando,inServer,outServer, datiRilevazione, sc, tokenSblocco); //aggiugnere le liste
                    addRilevazioni.addRilevazione();
                }                

            }else if(comando.contains("download")){
                String scomposizioneComando [] = comando.split(" ",2);
                String comandoScelto = scomposizioneComando[0];
                String nomeFile = scomposizioneComando[1];
                if (datiRilevazione.containsKey(nomeFile)) {
                    System.out.println("\u001B[31m[CLIENT] rilevazione '" + nomeFile + "' esiste gia in locale.\u001B[0m");
                    continue;
                }
                Download downloadRisorsa = new Download();
                downloadRisorsa.scarica(nomeFile, outServer, inServer, comandoScelto, datiRilevazione, tokenSblocco, sc);

            }else if(comando.equals("quit")){
                //chiudere tutti i thread  e terminare il main
                outServer.println(comando);
                String risposta;
                try {
                    risposta = inServer.readLine();
                    if(risposta.contains("disconnesso")){
                        //salvataggio della hashmap in locale 
                        salvataggio.salvataggioDatiRilevazioni(datiRilevazione);
                        salvataggio.salvataggioTokenRilevazioni(tokenSblocco);
                        chiusura.chiusuraConnessioneThreadComando(socket, primowhile, secondowhile);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (comando.contains("test")) {
                    System.out.println("\n========== [TEST] Stato delle liste ==========");

                    System.out.println("--- datiRilevazione (NOMEFILE -> TOKEN) ---");
                    if (datiRilevazione.isEmpty()) {
                        System.out.println("  (vuota)");
                    } else {
                        for (Map.Entry<String, String> entry : datiRilevazione.entrySet()) {
                            System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
                        }
                    }

                    System.out.println("--- tokenSblocco (TOKEN -> CHIAVE) ---");
                    if (tokenSblocco.isEmpty()) {
                        System.out.println("  (vuota)");
                    } else {
                        for (Map.Entry<String, String> entry : tokenSblocco.entrySet()) {
                            System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
                        }
                    }

                    System.out.println("================================================\n");
                }else{
                System.out.println("comando inserito sbagliato");
            }
        }  
    }
}
