package client.comandi;

import java.io.*;
import java.net.Socket;

// Classe che permette al client di mandare la richiesta di download 
public class Download {

    public void scarica(String nomeFile, PrintWriter outServer, BufferedReader inServer, String comandoScelto) {
        outServer.println(comandoScelto + ", " + nomeFile);
        outServer.flush();

        try{
            String risposta = inServer.readLine();
            Boolean controllo = false;
            if(risposta.equals("RILEVAZIONE_NON_TROVATA")){
                System.out.println("Non esiste questa rilevazione");
                return;
            }else{
                controllo = true;
            }
            if(controllo && risposta.contains("DOWNLOAD_INIZIATO")){
                String credenzialiServer= inServer.readLine();

                //recupero le credenziali dell'aggregatorDownloader e creo un nuovo socket per avere una connessione dedicata 
                String[] credenziali = credenzialiServer.split(","); 
                String ip = credenziali[0].trim();
                int porta = Integer.parseInt(credenziali[1].trim());
                Socket socketDownloadAggregator = new Socket(ip, porta);

                //creo i flussi di comunicazione con il serverDownloader
                BufferedReader inDownload = new BufferedReader(new InputStreamReader(socketDownloadAggregator.getInputStream()));
                PrintWriter outDownload = new PrintWriter(socketDownloadAggregator.getOutputStream(), true);
                //avvio del thread che opera in modo separato per rcevere i peer e tentare la connessione con loro
                ThreadDownload threadDownload = new ThreadDownload(nomeFile, outServer, inServer, inDownload, outDownload, socketDownloadAggregator);
                threadDownload.start();
            }
        }catch(IOException e){
            System.out.println("Errore nella ricezione della risposta del server");
        }
    }
}
