package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;



/**
 * ThreadSmistamentoRichieste
 * thread che si occupa di accedere alla lista di socket attivi e di eseguirli 
 * sfruttando i meccanismi di sincronizzazione
 * 
 * Gestione della coda di socket in ordine fifo
 */


public class ThreadSmistamentoRichieste extends Thread {

    private List<Socket> listaSocekt;
    private Map<String,String> datiRilevazione;
    private Map<String,String> tokenSblocco;


    public ThreadSmistamentoRichieste(List<Socket> listaSocekt, Map<String,String> datiRilevazione, Map<String,String> tokenSblocco) {
        this.listaSocekt = listaSocekt;
        this.datiRilevazione = datiRilevazione;
        this.tokenSblocco = tokenSblocco;
    }

    @Override
    public void run() {
        Boolean chiusuraForzata = false; //variabile importante per la chiusura
        Boolean statoLista = true;

        while (!chiusuraForzata) {
            

            synchronized (listaSocekt) {
                if (listaSocekt.isEmpty()) {
                    try {
                        listaSocekt.wait(); 
                        statoLista = false;  
                    } catch (InterruptedException e) {
                        System.out.println("Errore nella fase di caricamento dell'elemento in lista");
                    }
                } else {
                    statoLista = true;
                }
            }


            while (statoLista && !chiusuraForzata) { 
                try {
                    Socket socketClient = null;

                    synchronized (listaSocekt) {
                        if (!listaSocekt.isEmpty()) {
                            socketClient = listaSocekt.remove(0); 
                        }
                    }

                    // Se abbiamo preso un socket creiamo input e output Stream per servirlo
                    if (socketClient != null) {
                        DataInputStream inPeer = new DataInputStream(socketClient.getInputStream());
                        DataOutputStream outPeer = new DataOutputStream(socketClient.getOutputStream());
                        
                        serveClient serve = new serveClient(socketClient, inPeer, outPeer, datiRilevazione, tokenSblocco);
                        serve.esegui(); 
                    }

                    synchronized (listaSocekt) {
                        if (listaSocekt.isEmpty()) {
                            statoLista = false;
                        }
                    }

                } catch (Exception e) {
                    System.out.println("Errore nello smistamento");
                }
            }
        }
    }
}