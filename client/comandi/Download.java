package client.comandi;

import java.io.*;

// Classe per gestire il download dai nodi
public class Download {

    public void scarica(String nomeFile, PrintWriter outServer, BufferedReader inServer, String comandoScelto) {
    
        //mando il comando al server per prepararlo ad accogliere la richiesta 
        outServer.println(comandoScelto + ", " + nomeFile);
        outServer.flush();

        //devo recuperare ip porta del download server 
        //avviare il thread download manager per ottenere il donload in background 
        //ricevo le credenzaili del server per avviare il download ip porta 
        try{
            //supponiamo ricevessi una stringa formattata con "ip , porta"
            String risposta = inServer.readLine();
            String[] credenziali = risposta.split(","); // supponiamo che la risposta sia formattata come "ip,porta"
            String ip = credenziali[0].trim();
            int porta = Integer.parseInt(credenziali[1].trim());
            ConnessioneDownloadManager downloader = new ConnessioneDownloadManager(ip, porta); 
            downloader.connessioneDownloader(); // avvio la connessione al download manager
        }catch(IOException e){
            System.out.println("Errore nella ricezione della risposta del server");
        }
    }
}
