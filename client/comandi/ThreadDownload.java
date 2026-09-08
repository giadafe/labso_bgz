package client.comandi;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * ThreadDownload 
 * Si occupa di ricevere il peer dall'Aggregator 
 * e stabilire un tentare la connessione con il peer.
 */
public class ThreadDownload extends Thread {
    BufferedReader inComunicazioneDownload;
    PrintWriter outComunicazioneDownload;
    PrintWriter outServer;
    BufferedReader inServer;
    Socket socketDownload;
    String nomeFile;


    public ThreadDownload(String nomeFile, PrintWriter outServer, BufferedReader inServer, BufferedReader inComunicazioneDownload, PrintWriter outComunicazioneDownload, Socket socketDownload) {
        this.nomeFile = nomeFile;
        //comunicazione con il controller dell'Aggregator
        this.outServer = outServer;
        this.inServer = inServer;

        //comunicazione con l'aggregator per ricevere i peer
        this.inComunicazioneDownload = inComunicazioneDownload;
        this.outComunicazioneDownload = outComunicazioneDownload;
        this.socketDownload = socketDownload;
    }


    @Override
    public void run() {

        Boolean statoDownload = true;
        while(statoDownload){  
            try {
                String peerRicevuto =  inComunicazioneDownload.readLine();                
                if(peerRicevuto == null) {
                    System.out.println("Non e stato ricevuto nessun peer");
                    break;
                }


                if(peerRicevuto.equals("NO_PEERS_DISPONIBILI")){
                    System.out.println("Peer esauriti");
                    statoDownload = false;
                    continue;
                }

                //scompattazione del peer in singoli dati 
                String[] credenzialiPeer = peerRicevuto.split(":");
                String ip = credenzialiPeer[1];
                int porta = Integer.parseInt(credenzialiPeer[2]);
                String token = credenzialiPeer[3];
                Boolean esitoDownload = false;
                //tentativo di connessione con il peer
                try{
                    Socket socketPeer = new Socket(ip, porta);
                    System.out.println("sei connesso con il peer");

                    //DataInputStream e DataOutputStream per la comunicazione con il peer
                    //mi permettono di inivare e ricevere file binari dal peer, nel nostro caso serve per scaricare le rilevazioni da byte a file
                    DataInputStream inPeer = new DataInputStream(socketPeer.getInputStream());
                    DataOutputStream outPeer = new DataOutputStream(socketPeer.getOutputStream()); 

                    //classe downloaderPeertoPeer per gestire il download dai peer in background
                    DownloaderPeer downloaderPeer = new DownloaderPeer(nomeFile, outServer, inServer, inPeer, outPeer, socketPeer, token, socketDownload);
                    esitoDownload = downloaderPeer.downloadRilevazione();

                    //fase di notifica 
                    if(!esitoDownload){
                        outComunicazioneDownload.println("DOWNLOAD_FALLITO");  // l'aggregator deve mandare un nuovo peer 
                    }else{
                        outComunicazioneDownload.println("DOWNLOAD_RIUSCITO"); // il server deve finire la fase di inivio di peer
                        statoDownload = false;
                    }

                }catch(IOException e){
                    System.out.println("Errore nella connessione con il peer" );
                    //connessione fallita con il peer 
                    outComunicazioneDownload.println("DOWNLOAD_FALLITO");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

}