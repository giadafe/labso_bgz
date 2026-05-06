package client.comandi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

// Classe per gestire il comando listdata remote
public class ListDataRemote {

    // Metodo per chiedere la lista al server e stamparla
    public void esegui(PrintWriter outServer, BufferedReader inServer) {
        // Inviamo il comando al Aggregator
        outServer.println("listdata remote");
        outServer.flush();

        try {
            System.out.println("Risorse:");

            String rigaRicevuta;
            // Leggiamo la risposta riga per riga dal server
            // Il server deve mandare FINE_LISTA quando ha finito
            while ((rigaRicevuta = inServer.readLine()) != null) {
                if (rigaRicevuta.equals("FINE_LISTA")) {
                    break; 
                }
                
                System.out.println("- " + rigaRicevuta);
            }
        } catch (IOException e) {
            // Gestione errore se il server si disconnette
            System.err.println("[ERRORE] Problema con il server durante la listdata remote");
        }
    }
}
