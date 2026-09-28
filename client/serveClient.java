package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * serveClient
 * Classe che si occupa di servire il client
 * si occupa di ricevere un sito sulla decriptazione del file
 * e notificare  lo stato del download 
 */


public class serveClient {
    private Socket socketClient;
    private DataInputStream inPeer;
    private DataOutputStream outPeer;
    private Map<String,String> datiRilevazione;
    private Map<String,String> tokenSblocco;

    public serveClient(Socket socketClient, DataInputStream inPeer, DataOutputStream outPeer, Map<String,String> datiRilevazione, Map<String,String> tokenSblocco) {
        this.socketClient = socketClient;
        this.inPeer = inPeer;
        this.outPeer = outPeer;
        this.datiRilevazione = datiRilevazione;
        this.tokenSblocco = tokenSblocco;
    }

    public void esegui() {
        try {
            String richiesta = inPeer.readUTF();

            if (richiesta == null || !richiesta.contains(":")) {
                outPeer.writeUTF("DOWNLOAD_FALLITO");
                outPeer.flush();
                return;
            }

            String [] partiRichiesta = richiesta.split(":");
            String nomeFile = partiRichiesta[0];
            String token = partiRichiesta[1];

            if(datiRilevazione.containsKey(nomeFile)){
                String tokenAssociato = datiRilevazione.get(nomeFile);

                if(token.equals(tokenAssociato)){
                    String chiave = tokenSblocco.get(token);

                    Path pathFileCifrato = Paths.get("rilevazioni", nomeFile);
                    File fileCifrato = pathFileCifrato.toFile();

                    if(!fileCifrato.exists()){
                        outPeer.writeUTF("DOWNLOAD_FALLITO");
                        outPeer.flush();
                    }else{
                        DecrittazioneAES decrittazione = new DecrittazioneAES();
                        byte[] contenutoDecrittato = decrittazione.decrittazione(chiave, nomeFile, fileCifrato);

                        if(contenutoDecrittato == null){
                            outPeer.writeUTF("DOWNLOAD_FALLITO");
                            outPeer.flush();
                        }else{
                            outPeer.writeUTF("DOWNLOAD_RIUSCITO");
                            outPeer.flush();

                            DownloadServiceSender downloadServiceSender = new DownloadServiceSender();
                            downloadServiceSender.downloader(socketClient, nomeFile, contenutoDecrittato.length, contenutoDecrittato);
                        }
                    }
                }else{
                    outPeer.writeUTF("DOWNLOAD_FALLITO");
                    outPeer.flush();
                }
            }else{
                outPeer.writeUTF("DOWNLOAD_FALLITO");
                outPeer.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (socketClient != null && !socketClient.isClosed()) {
                    socketClient.close();
                }
            } catch (Exception e) {
                System.out.println("\u001B[31m[CLIENT PEER TO PEER] Errore nella chiusura del socket \u001B[0m");            
            }
        }
    }
}