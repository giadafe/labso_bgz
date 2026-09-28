package client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * ServerRilevazioni
 * Thread che funge da server, ascolte le richieste di download da parte di altri peer/client/nodi
 */

public class ServerRilevazioni extends Thread {
    private int portaAggregator;
    private Map<String,String> datiRilevazione;
    private Map<String,String> tokenSblocco;
    private ServerSocket ss;


    public ServerRilevazioni(ServerSocket ss, int portaAggregator, BufferedReader inServer, PrintWriter outServer, Map<String,String> datiRilevazione, Map<String,String> tokenSblocco) {
        this.portaAggregator=portaAggregator;
        this.datiRilevazione=datiRilevazione;
        this.tokenSblocco=tokenSblocco;
        this.ss = ss;
    }

    @Override 

    public void run(){
        Boolean chiusuraForzata = false; //elemento importante per la fase di chiusura
        try{
            //lista di socket 
            List<Socket> listaSocekt = new ArrayList<>();
            
            ThreadSmistamentoRichieste smistamento = new ThreadSmistamentoRichieste(listaSocekt,datiRilevazione,tokenSblocco);
            smistamento.start();
            while(!chiusuraForzata){
                Socket clientAccettato = ss.accept();
                synchronized(listaSocekt){
                    listaSocekt.add(clientAccettato);
                    listaSocekt.notify();
                }
            }
          }catch(IOException e){
            System.out.println("\u001B[31m[CLIENT] Errore apertura del server \u001B[0m");            
               e.setStackTrace(getStackTrace());
            }
        }
}
