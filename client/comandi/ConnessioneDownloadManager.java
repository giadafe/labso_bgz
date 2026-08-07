package client.comandi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * DownloadManager
 */
public class ConnessioneDownloadManager extends Thread {
    public String ip;
    public int porta;
    public ConnessioneDownloadManager(String ip, int porta) {
        this.ip = ip;
        this.porta = porta;
    }

    public void connessioneDownloader() {
        //qui va il codice per scaricare il file dal server
        System.out.println("Scaricamento in corso da " + ip + ":" + porta);
        //connessione al server di download 
        try{
            Socket socketDownload = new Socket(ip, porta);
            //implementazione dei buffer di lettura e scrittura per per ricevere i peer dal server
            BufferedReader inComunicazioneDownload = new BufferedReader( new InputStreamReader(socketDownload.getInputStream()));
            PrintWriter outComunicazioneDownload = new PrintWriter(socketDownload.getOutputStream(),true); 
            System.out.println("Sei connesso al download manager del  server");
            //avvio il thread che si occupa di smistare il peer ricevuto dal server di download
            ThreadDownload threadDownload = new ThreadDownload(inComunicazioneDownload, outComunicazioneDownload);
            threadDownload.start();
        }catch(IOException e){
            System.out.println("Errore nella connessione al server di download");
        }
    }
}
