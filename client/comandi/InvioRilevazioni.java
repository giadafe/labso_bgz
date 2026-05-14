package client.comandi;

import java.io.PrintWriter;
import java.util.Map;


 // Classe dedicata alla registrazione automatica delle risorse locali 
 // presso l'Aggregator subito dopo l'autenticazione.

public class InvioRilevazioni {

    public void invia(PrintWriter outServer, Map<String, String> datiRilevazione) {
            System.out.println("metodo invia attivato");

        // Verifichiamo che ci siano effettivamente dei dati da inviare
        if (datiRilevazione != null && !datiRilevazione.isEmpty()) {
            // Estraiamo i nomi delle risorse (es. R1, R2, ecc.) usando le chiavi della mappa
            // Usiamo String.join per creare una stringa separata da virgole
            //String listaNomi = String.join(",", datiRilevazione.keySet());
            
            
            for(String valoreDaInviare : datiRilevazione.keySet()){
                System.out.println("valore inviato");
                String val = valoreDaInviare + "," + datiRilevazione.get(valoreDaInviare);
                outServer.println(val); //invio (nome, val) [0],[1]
            }
            //outServer.println("REGISTER_RESOURCES;" + listaNomi);

        }
        outServer.println("fine_condivisione");
        // Svuotiamo il buffer per assicurarci che il messaggio parte subito
        outServer.flush();
    }
}


