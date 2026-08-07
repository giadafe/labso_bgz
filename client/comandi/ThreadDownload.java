package client.comandi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * ThreadDownload
 */
public class ThreadDownload extends Thread {
    BufferedReader inComunicazioneDownload;
    PrintWriter outComunicazioneDownload;
    public ThreadDownload(BufferedReader inComunicazioneDownload, PrintWriter outComunicazioneDownload) {
        this.inComunicazioneDownload = inComunicazioneDownload;
        this.outComunicazioneDownload = outComunicazioneDownload;
    }

    @Override
    public void run() {
        //il thread rimane ad ascoltare il master download in modo tale da ricevere il peer 
        //si riceve una stringa "ip,porta,token"
        String peerRicevuto;
        try {
            peerRicevuto = inComunicazioneDownload.readLine();
            while(peerRicevuto != null){
                String[] datiPeer = peerRicevuto.split(",");
                String ipPeer = datiPeer[0].trim();
                int portaPeer = Integer.parseInt(datiPeer[1].trim());
                String tokenPeer = datiPeer[2].trim();
                System.out.println("Ricevuto peer: " + ipPeer + ":" + portaPeer + " con token: " + tokenPeer);
                //tenttativo di connessione al peer ricevuto
                try{
                    Socket connessionePeer = new Socket(ipPeer, portaPeer);
                    System.out.println("Connessione al peer " + ipPeer + ":" + portaPeer + " avvenuta con successo");
                    //apertura dei buffer di comunicazione con il peer
                    BufferedReader inComunicazioneDownload = new BufferedReader( new InputStreamReader(connessionePeer.getInputStream()));
                    PrintWriter outComunicazioneDownload = new PrintWriter(connessionePeer.getOutputStream(),true); 
                    outComunicazioneDownload.println(tokenPeer); //invio del token al peer per autenticazione
                    //attesa di una risposta dal peer








                }catch(IOException e){
                    System.out.println("Errore nella connessione al peer ricevuto");
                }

                peerRicevuto = inComunicazioneDownload.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
