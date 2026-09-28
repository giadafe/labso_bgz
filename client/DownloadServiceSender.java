package client;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.Socket;

public class DownloadServiceSender {
    /**
     * DownloadServiceSender
     * Classe che si occupa del download
     * inivia al client/nodo/peer in byte 
     */
    public void downloader(Socket socketClient, String nomeFile, int dimensioneFile, byte[] contenutoDecrittato) {
        try {
            BufferedOutputStream outStream = new BufferedOutputStream(socketClient.getOutputStream());
            try (ByteArrayInputStream byteIn = new ByteArrayInputStream(contenutoDecrittato)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                //fase di download
                while ((bytesRead = byteIn.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                }
                outStream.flush();
            }
            socketClient.shutdownOutput();
            System.out.println("\u001B[32m[CLIENT PEER TO PEER]  File inviato con successo!\u001B[0m");        
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del file: " + e.getMessage());
        }
    }
}