package master;

import java.io.*;
import java.net.*;
import java.util.*;
/*
 * ===================================
 * masterComandi
 * ===================================
 * Classe che gestisce i comandi da terminale del master
 */
public class masterComandi implements Runnable {
    public ServerSocket ss;
    private Map<String, List<String>> infoRilevazioni;

    public masterComandi(ServerSocket ss, Map<String, List<String>> infoRilevazioni) {
        this.ss = ss;
        this.infoRilevazioni = infoRilevazioni;
    }
    /**
     * ==========
     * Fase di avvio dei comandi del master
     * ==========
     */
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        boolean stato = true;

        while (stato) {
            System.out.println("[SERVER:] scegliere fra uno dei seguenti comandi:");
            System.out.print(">listData");
            System.out.print(">log");
            System.out.print(">quit");

            String comando = scanner.nextLine();
            // Gestione dei comandi con lo switch else al posto di if else 
            switch (comando) {
                /**
                 * =========
                 * Comando listdata
                 * =========
                 * Mostra la lista delle risorse disponibili nel sistema
                 */
                case "listdata":
                    System.out.println("[SERVER:] A breve riceverai la lista delle risorse");
                    // Logica per listdata: mostra le chiavi delle risorse disponibili
                    if (infoRilevazioni == null || infoRilevazioni.isEmpty()) {
                        System.out.println("[SERVER:] Non ci sono risorse disponibili.");
                    } else {
                        System.out.println("[SERVER:] Risorse disponibili:");
                        for (String risorsa : infoRilevazioni.keySet()) {
                            System.out.println("- " + risorsa);
                        }
                    }

                    break;

                /**
                 * ===========
                 * Comando log
                 * ===========
                 * Mostra i log dei download effettuati
                 */
                case "log":
                    System.out.println("[SERVER:] Ecco i log:");

                    //Recupero dei log

                    break;
                /**
                 * ===========
                 * Comando quit
                 * ===========
                 * Chiude il server master
                 */
                case "quit": 
                    System.out.println("[SERVER:] server disconnesso");
                    //chiusura del ciclo while e del server socket
                    stato = false;
                    try {
                        ss.close();
                        System.out.println("[SERVER:] Connessione chiusa.");
                        System.exit(0);
                    } catch (IOException e) {
                        System.out.println("[SERVER:] Errore durante la chiusura del ServerSocket: " + e.getMessage());
                    }
                    break;

                default:
                    System.out.println("[SERVER:] Comando non riconosciuto");
                    break;
            }
        }
    }

}