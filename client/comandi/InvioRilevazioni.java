package client.comandi;

import java.io.PrintWriter;
import java.util.Map;

/**
 * Classe dedicata alla registrazione automatica delle risorse locali 
 * presso l'Aggregator subito dopo l'autenticazione.
 */
public class InvioRilevazioni {

    public void invia(PrintWriter outServer, Map<String, String> datiRilevazione) {
        // Verifichiamo che ci siano effettivamente dei dati da inviare
        if (datiRilevazione != null && !datiRilevazione.isEmpty()) {
            
            // Estraiamo i nomi delle risorse (es. R1, R2, ecc.) usando le chiavi della mappa
            // Usiamo String.join per creare una stringa separata da virgole
            String listaNomi = String.join(",", datiRilevazione.keySet());
            
            // Inviamo il comando di Registrazione Risore all'aggregator
            outServer.println("REGISTER_RESOURCES;" + listaNomi);
            
            // Svuotiamo il buffer per assicurarci che il messaggio parte subito
            outServer.flush();
            
           package client.comandi;

import java.io.PrintWriter;
import java.util.Map;

/**
 * Classe dedicata alla registrazione automatica delle risorse locali 
 * presso l'Aggregator subito dopo l'autenticazione.
 */
public class RegistrazioneRisorse {

    public void invia(PrintWriter outServer, Map<String, String> datiRilevazione) {
        // Verifichiamo che ci siano effettivamente dei dati da inviare
        if (datiRilevazione != null && !datiRilevazione.isEmpty()) {
            
            // Estraiamo i nomi delle risorse (es. R1, R2) usando le chiavi della mappa
            // Usiamo String.join per creare una stringa separata da virgole
            String listaNomi = String.join(",", datiRilevazione.keySet());
            
            // Inviamo il comando al Master seguendo il protocollo
            outServer.println("REGISTER_RESOURCES;" + listaNomi);
            
            // Svuotiamo il buffer per assicurarci che il messaggio parta subito
            outServer.flush();
            
        
         System.out.println("[DEBUG] Rilevazioni inviate al server: " + listaNomi);
        }
    }
}
