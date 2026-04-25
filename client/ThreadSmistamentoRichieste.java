package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadSmistamentoRichieste extends Thread {
    public LinkedBlockingQueue<Socket> listaSocekt;



    public ThreadSmistamentoRichieste(LinkedBlockingQueue<Socket> listaSocekt) {
        this.listaSocekt = listaSocekt;
    }

    public void run(){

    while (true) { 
            try {
                Socket socketClient = listaSocekt.take();
                BufferedReader inPeer = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
                PrintWriter outPeer = new PrintWriter(socketClient.getOutputStream(), true);
                serveClient serve = new serveClient(socketClient, inPeer, outPeer );
                //classe che si occupa di servire il client
                serve.esegui();
            } catch (Exception e) {
                System.out.println("Errore nello smistamento");
            }
        }
    }
}
