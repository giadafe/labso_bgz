package client;

import client.comandi.Add;
import client.comandi.InvioRilevazioni;
import client.comandi.listDataLocal;//test chiamata classe da un altra cartella

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;


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


    public ComandiClient(BufferedReader inServer, PrintWriter outServer, String nomeMacchina, Scanner sc, String stato, String ip, int porta, ChiusuraSocket chiusura, Socket socket, Map<String,String> datiRilevazione) {
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
    }
    
    @Override
    public void run(){
        InvioRilevazioni inviaDati = new InvioRilevazioni();
        System.out.println("Comandi avviati");
        //mandare la lista  delle sue riprese da un file di testo

    while(primowhile){
            //mando lo stato della macchina 
            System.out.println(nomeMacchina);
            try {
                if(stato.equals("NUOVA_MACCHINA")){
                    System.out.println("[DEBUG] Stato: NUOVA_MACCHINA. Invio stato e nome: " + nomeMacchina);
                    outServer.println(stato);
                    outServer.println(nomeMacchina);
                    
                    System.out.println("[DEBUG] In attesa di risposta dall'Aggregator...");
                    String responseAggregator = inServer.readLine();
                    System.out.println("[DEBUG] Risposta ricevuta: " + responseAggregator);
                    
                    if(responseAggregator.equals("nomeMacchina_DUPLICATO")){
                        System.out.println("[ATTENZIONE] Nome duplicato! Inserire un nuovo nome nel terminale:");
                        //reinserisci il nome
                        nomeMacchina = sc.nextLine();
                        System.out.println("[DEBUG] Nuovo nome inserito: " + nomeMacchina + ". Riprovo registrazione...");
                    }else if(responseAggregator.equals("nomeMacchina_VALIDA")){
                        System.out.println("[OK] Nome valido. Invio credenziali: " + ip + " " + porta);
                        outServer.println(ip +" "+porta);

                        System.out.println("[DEBUG] Registrazione completata con successo.");

                        primowhile = false;
                        secondowhile = true;
                    }
                    
                }else if(stato.equals("MACCHINA_ESISTENTE")){
                    System.out.println("[DEBUG] Stato: MACCHINA_ESISTENTE. Invio aggiornamento dati per: " + nomeMacchina);
                    outServer.println(stato);
                    outServer.println(nomeMacchina);
                    outServer.println(ip +" "+ String.valueOf(porta));   //aggiornamento ip porta per IP dinamici             
                    primowhile = false;
                    System.out.println("[DEBUG] Dati aggiornati sul server.");
                    secondowhile = true;
                }
            } catch (IOException e) {
                System.err.println("[ERRORE] Problema di comunicazione durante il ciclo primowhile:");
                //chiusura del socket
                chiusura.chiusuraConnessioneThreadComando(socket,primowhile,secondowhile);
            }
        }

        System.out.println("[CLIENT] Fase di registrazione conclusa, pronto per mandare i dati");
        String start;
        try {
            start = inServer.readLine();
            if(!"inizio_fase_rilevazioni".equals(start)) {
                System.out.println("Protocollo desincronizzato");
            return;
        }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //serve per mandare i dati della rilevazione al master sfruttando la calsse che ha creato blerta
        inviaDati.invia(outServer, datiRilevazione);
        










        //comandi
        while(secondowhile){

            System.out.println("[COMANDI] Comandi attivi");
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
            }else if(comando.contains("add")){
                //aggiugnere registraizone + valore tutto in String
                String scomposizioneComando [] = comando.split(" ",3);// salvo in blocchi di 3 le stringhe 
                Add addRilevazioni = new Add(scomposizioneComando,inServer,outServer, datiRilevazione);
                addRilevazioni.addRilevazione();
            }else if(comando.contains("download")){
                //implementare la crittografia AES, usare la chiave per sbloccare il cyphertext
                //mandare il nome e la chiave al master
            }else if(comando.equals("quit")){
                //chiudere tutti i thread  e terminare il main
            }else{
                System.out.println("comando inserito sbagliato");
            }
        }  
    }
}
