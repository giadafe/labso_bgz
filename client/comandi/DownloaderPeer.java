package client.comandi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * DownloaderPeer 
 * Si occupa di gestire il download della rilevazione da un peer.
 * e deve restituire dopo il salvataggio, creato la copia crittografata e aggiunta essa all'aggregator 
 * 
 * da finire.
 */
public class DownloaderPeer {
    private String nomeFile;
    private PrintWriter outServer;
    private BufferedReader inServer;
    private DataInputStream inPeer;
    private DataOutputStream outPeer;
    private Socket socketPeer;
    private String token;

    public DownloaderPeer(String nomeFile, PrintWriter outServer, BufferedReader inServer, DataInputStream inPeer, DataOutputStream outPeer,
            Socket socketPeer, String token, Socket socketDownload) {
        this.nomeFile = nomeFile;
        this.outServer = outServer;
        this.inServer = inServer;
        this.inPeer = inPeer;
        this.outPeer = outPeer;
        this.socketPeer = socketPeer;
        this.token = token;
    }

    public boolean downloadRilevazione(){
        try {
            outPeer.writeUTF(nomeFile + ":" + token);
            outPeer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        
        try {
            String esito = inPeer.readUTF();
            
            if(esito == null || esito.equals("DOWNLOAD_FALLITO")){
                System.out.println("Il download non è riuscito");
                return false;
            }
            
            //Creazione del file in locale
            Path pathFileScaricato = Paths.get("client/rilevazioniScaricate", nomeFile);
            File fileDestinazione = pathFileScaricato.toFile();
            
            if (fileDestinazione.getParentFile() != null) {
                fileDestinazione.getParentFile().mkdirs();
            }

            
            try (FileOutputStream download = new FileOutputStream(fileDestinazione)) {
                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = inPeer.read(buffer)) != -1) {
                    download.write(buffer, 0, bytesRead);
                }
                
                download.flush();
            }
            
            return true; 

        } catch (Exception e) {
            System.err.println("[DOWNLOAD-CLIENT] Errore critico durante il download: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}