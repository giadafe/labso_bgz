package aggregator;

import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
/*
    * ==================
    * GestioneRichiesteClient
    * =================
    * Thread che gestisce le richieste in entrata dai client peer.
*/
public class GestoreLog {
    private List<String> downloadLogs;
    private HashMap<String, List<String>> risorseRemote;

    public GestoreLog() {
        this.downloadLogs = new ArrayList<>(); // Inizializza la lista dei log dei download
        this.risorseRemote = new HashMap<>();
    }
    /*
        * ==================
        * logDownload
        * =================
        * Metodo sincronizzato per registrare un log di download.
    */
    public synchronized void logDownload(String nomeRisorsa, String peerRichiedente, String peerPossessore) {
        LocalTime tempo = LocalTime.now(); // ottengo l'ora
        String orario = tempo.format(DateTimeFormatter.ofPattern("HH:mm")); // formatto in ore e minuti

        // creo la stringa del log del download
        String Log = orario + "  " + nomeRisorsa + " Da: " + peerPossessore + " a: " + peerRichiedente;
        downloadLogs.add(Log); // Aggiungo il log alla lista dei download
    }

    /*
    * ==================
    * getDownloadLogs
    * =================
    * Metodo sincronizzato per ottenere i log dei download.
    */
    public synchronized List<String> getDownloadLogs() {
        return new ArrayList<>(downloadLogs); // Ritorna una copia per sicurezza
    }

    public synchronized void stampa() {
        if (downloadLogs.isEmpty()) {
            System.out.println("[SERVER:] Nessun download registrato.");
            return;
        }

        for (String log : downloadLogs) {
            System.out.println("- " + log);
        }
    }

    public synchronized HashMap<String, List<String>> getRisorseRemote() {
        return new HashMap<>(risorseRemote); // Ritorna una copia per sicurezza
    }
}