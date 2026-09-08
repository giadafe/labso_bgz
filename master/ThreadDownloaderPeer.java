package master;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

/**
 * DownloaderPeer
 * Thread gestore che scorre la lista dei peer candidati e li invia
 * uno alla volta al client tramite il canale dedicato.
 */
public class ThreadDownloaderPeer extends Thread {

    private String risorsa;
    private List<String> peers;
    private HashMap<String, String> infoMacchinaClient;

    // Buffer del canale dedicato (Porta dinamica)
    private BufferedReader inDownload;
    private PrintWriter outDownload;

    // Buffer del canale principale di controllo
    private BufferedReader in;
    private PrintWriter out;

    public ThreadDownloaderPeer(String risorsa, List<String> peers, HashMap<String, String> infoMacchinaClient,
                          BufferedReader inDownload, PrintWriter outDownload, 
                          BufferedReader in, PrintWriter out) {
        
        this.risorsa = risorsa;
        this.peers = peers;
        this.infoMacchinaClient = infoMacchinaClient;
        this.inDownload = inDownload;
        this.outDownload = outDownload;
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        // Implementazione dello scorrimento della lista dei candidati
        while(!peers.isEmpty()){
            String peer = peers.get(0); //ordine FIFO
            System.out.println("[SERVER] Invio del peer al client: " + peer);
            outDownload.println(peer);

            //se il download non va a buon fine, rimuovo il peer e procedo con il prossimo e rimuovo quel peer dalla lista di infoRilevazioni
            //peers.remove(peer);
            //rimozione da infoRilevazioni
            //...
            try {
            System.out.println("Sto in attesa della risposta del client");
                String rispostaClient = inDownload.readLine();
                //caso fallimento del download
                if(rispostaClient.equals("DOWNLOAD_FALLITO")){
                    System.out.println("[SERVER] Download fallito dal client!");

                    peers.remove(peer);
                    //rimozione da infoRilevazioni
                }

                if (rispostaClient.equals("DOWNLOAD_RIUSCITO")) {
                    System.out.println("[SERVER] Download completato con successo dal client!");
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            


            if (peers.isEmpty()) {
                System.out.println("[SERVER] Tutti i peer hanno fallito. Avviso il client.");
                outDownload.println("NO_PEERS_DISPONIBILI");
            }
            
        }
    }
}