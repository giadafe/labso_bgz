package client.comandi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Scanner;

public class Add {
    public String[] scomposizioneComando;
    public Map<String, String> datiRilevazione;
    public Map<String, String> tokenSblocco;
    public BufferedReader inServer;
    public PrintWriter outServer;
    public Scanner sc;

    public Add(String[] scomposizioneComando, BufferedReader inServer, PrintWriter outServer, Map<String, String> datiRilevazione, Scanner sc, Map<String, String> tokenSblocco) {
        this.scomposizioneComando = scomposizioneComando;
        this.inServer = inServer;
        this.outServer = outServer;
        this.datiRilevazione = datiRilevazione;
        this.sc = sc;
        this.tokenSblocco = tokenSblocco;
    }

    public synchronized void addRilevazione() {

        // Estraggo i parametri
        String comandoScelto = scomposizioneComando[0];
        String nomeRilevazione = scomposizioneComando[1];
        String valoreRilevazione = scomposizioneComando[2];




        outServer.println(comandoScelto);//invio al server il comando scelto 

        // Controllo inserimento nome duplicato
        if (!datiRilevazione.containsKey(nomeRilevazione)) {

            CrittografiaAES crittografia = new CrittografiaAES();
            String[] contenuto = crittografia.crittografaIlContenuto(valoreRilevazione);

            // Verifico l'esito della crittografia
            if (contenuto != null && contenuto.length == 2 && !contenuto[0].isEmpty()) {
                
                String contenutoCrittografato = contenuto[0]; 
                String chiaveDecrittazione = contenuto[1];

                // Creazione del file
                CreazioneFile creazione = new CreazioneFile();
                boolean controllo = creazione.creazioneFile(nomeRilevazione, contenutoCrittografato);

                if (!controllo) {
                    System.err.println("Errrore nella creazione del file");
                } else {
                    //inserire i dati nelle liste 
                    System.out.println(" Come vuoi chiamare il token di sblocco?");
                    AssegnaToken nuovoToken = new AssegnaToken();
                    String token = sc.nextLine();
                    Boolean checkToken = true;
                    String tokenValidato = "";

                    while(checkToken){
                        tokenValidato = nuovoToken.aggiungiToken(token, tokenSblocco).trim();
                        if(tokenValidato.equals("TOKEN_ESISTENTE")){
                            System.out.println("Token esistente, inserisci un nuovo token");
                            token = sc.nextLine();
                        }else{
                            //aggiorno la tabella dei token cosi  da non renderlo riconoscibile 
                            synchronized(tokenSblocco){
                                tokenSblocco.put(tokenValidato.toLowerCase(), chiaveDecrittazione); 
                            }
                            // aggiorno la tabella delle risorse in possesso 
                            synchronized(datiRilevazione){
                                datiRilevazione.put(nomeRilevazione, tokenValidato.toLowerCase()); 
                            }                            
                            checkToken = false;
                        }
                    }

                    //invio al server della nuova rilevazione
                    outServer.println(nomeRilevazione + ", " + tokenValidato);
                    String responseAggregator;
                    try {
                        responseAggregator = inServer.readLine();
                        if(responseAggregator.equals("RILEVAZIONE_AGGIUNTA")){
                            System.out.println("Rilevazione aggiunta correttamente");
                        }   
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                System.err.println("Errore nella fase di crittografia");
            }

        } else {
            // Caso rilevazione duplicata
            System.out.println("Questa rilevazione esiste gia");
            
            outServer.println("Rilevazione gia presente");
        }
    }
}