package client.comandi;

import java.io.*;
import java.net.Socket;

// Classe per gestire il download dai nodi
public class Download {

    public void avvia(String nomeFile, PrintWriter outServer, BufferedReader inServer) {
        boolean successo = false;

        // Ciclo robusto - riprova finché non scarica o finiscono i nodi
        while (!successo) {
            // Chiediamo al Master: "Chi ha questo file?"
            outServer.println("download " + nomeFile);
            outServer.flush();

            try {
                String risposta = inServer.readLine(); // Esempio: "192.168.1.10:9000:CHIAVE123"

                if (risposta == null || risposta.equals("NON_TROVATO")) {
                    System.out.println("Risorsa non disponibile nella rete.");
                    return; // Esce se il Master non ha più nessuno da suggerire
                }

                // Separiamo IP, Porta e Token ricevuti
                String[] dati = risposta.split(":");
                String ipNodo = dati;
                int portaNodo = Integer.parseInt(dati[3]);
                String token = dati[4];

                // Proviamo a connetterci direttamente all'altro nodo
                try (Socket peer = new Socket(ipNodo, portaNodo);
                     PrintWriter outPeer = new PrintWriter(peer.getOutputStream(), true);
                     BufferedReader inPeer = new BufferedReader(new InputStreamReader(peer.getInputStream()))) {

                    // Inviamo richiesta e token al nodo
                    outPeer.println("DAMMI " + nomeFile + " " + token);
                    String contenuto = inPeer.readLine(); // Riceviamo i dati
                    
                    System.out.println("Download completato: " + contenuto);
                    successo = true; // Fine del ciclo

                } catch (IOException e) {
                    // Se il nodo non risponde, avvisiamo il Master e il ciclo ricomincia
                    System.out.println("Nodo " + ipNodo + " offline. Segnalo al Master e riprovo...");
                    outServer.println("DOWNLOAD_FALLITO " + nomeFile + ":" + ipNodo);
                }

            } catch (IOException e) {
                System.out.println("Errore di rete con il Master.");
                break;
            }
        }
    }
}
