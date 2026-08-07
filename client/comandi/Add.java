package client.comandi;

import java.io.BufferedReader;
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
        System.out.println("========== [INIZIO ESECUZIONE COMANDO ADD] ==========");

        // Estraggo i parametri
        String comandoScelto = scomposizioneComando[0];
        String nomeRilevazione = scomposizioneComando[1];
        String valoreRilevazione = scomposizioneComando[2];

        System.out.println("[INFO] Comando ricevuto: " + comandoScelto);
        System.out.println("[INFO] Nome Rilevazione: '" + nomeRilevazione + "'");
        System.out.println("[INFO] Valore Rilevazione: '" + valoreRilevazione + "'");

        // Controllo duplicati in Mappa
        System.out.println("[CHECK] Verifica presenza della rilevazione nella mappa locale...");
        if (!datiRilevazione.containsKey(nomeRilevazione)) {
            System.out.println("[OK] -> Rilevazione NON presente in locale. Avvio processo di crittografia...");

            CrittografiaAES crittografia = new CrittografiaAES();
            String[] contenuto = crittografia.crittografaIlContenuto(valoreRilevazione);

            // Verifico l'esito della crittografia PRIMA di accedere all'array
            if (contenuto != null && contenuto.length == 2 && !contenuto[0].isEmpty()) {
                
                String contenutoCrittografato = contenuto[0]; 
                String chiaveDecrittazione = contenuto[1];

                System.out.println("[AES] Contenuto crittografato (Base64): " + contenutoCrittografato);
                System.out.println("[AES] Chiave di decrittazione (Base64): " + chiaveDecrittazione);

                // Creazione del file su disco
                System.out.println("[FILE] Avvio creazione del file locale per: '" + nomeRilevazione + "'...");
                CreazioneFile creazione = new CreazioneFile();
                boolean controllo = creazione.creazioneFile(nomeRilevazione, contenutoCrittografato);

                if (!controllo) {
                    System.err.println("[ERRORE] Errore critico durante la creazione del file su disco!");
                } else {
                    System.out.println("[OK] -> File creato con successo su disco!");
                    //inserire i dati nelle liste 

                    System.out.println("[INPUT] Come vuoi chiamare il token di sblocco per questa rilevazione?");
                    AssegnaToken nuovoToken = new AssegnaToken();
                    String token = sc.nextLine();
                    Boolean checkToken = true;

                    while(checkToken){
                        String tokenValidato = nuovoToken.aggiungiToken(token, tokenSblocco).trim();
                        if(tokenValidato.equals("TOKEN_ESISTENTE")){
                            System.out.println("[WARNING] Token già esistente! Inserire un nuovo token nel terminale:");
                            token = sc.nextLine();
                        }else{
                            
                            synchronized(tokenSblocco){
                                tokenSblocco.put(tokenValidato.toLowerCase(), chiaveDecrittazione); //aggiorno la tabella dei token cosi  da non renderlo riconoscibile 
                            }

                            synchronized(datiRilevazione){
                                datiRilevazione.put(nomeRilevazione, tokenValidato.toLowerCase()); // aggiorno la tabella delle risorse in possesso 
                            }                            
                            checkToken = false;
                        }
                    }


                    System.out.println("[LOG] Stato attuale Mappa Rilevazioni: " + datiRilevazione);

                    //invio al server della nuova rilevazione
                    outServer.println(nomeRilevazione + ", " + token);
                    System.out.println("[NET] -> Notifica inviata al Server: \"" + nomeRilevazione + ", " + token + "\"");

                }
            } else {
                System.err.println("[ERRORE] Fallimento del processo di crittografia AES.");
            }

        } else {
            // Caso rilevazione duplicata
            System.out.println("[WARNING] Rilevazione già presente nella mappa locale!");
            System.out.println("[NET] Invio notifica di errore al Server...");
            
            outServer.println("Rilevazione gia presente");
            System.out.println("[NET] -> Messaggio inviato al server: \"Rilevazione gia presente\"");
        }

        System.out.println("========== [FINE ESECUZIONE COMANDO ADD] ==========\n");
    }
}