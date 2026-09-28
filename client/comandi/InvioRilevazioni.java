package client.comandi;

import java.io.PrintWriter;
import java.util.Map;


 // Classe dedicata alla registrazione automatica delle risorse locali 
 // presso l'Aggregator subito dopo l'autenticazione.

public class InvioRilevazioni {

    public void invia(PrintWriter outServer, Map<String, String> datiRilevazione) {
        // Verifichiamo che ci siano effettivamente dei dati da inviare
        if (datiRilevazione != null && !datiRilevazione.isEmpty()) {
            for(String valoreDaInviare : datiRilevazione.keySet()){
                System.out.println("\u001B[32m[CLIENT]  valore inviato\u001B[0m");                
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


