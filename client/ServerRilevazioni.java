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


    public ServerRilevazioni(int portaAggregator, BufferedReader inServer, PrintWriter outServer, Map<String,String> datiRilevazione, Map<String,String> tokenSblocco) {
        this.portaAggregator=portaAggregator;
        this.datiRilevazione=datiRilevazione;
        this.tokenSblocco=tokenSblocco;
    }

    @Override 

    public void run(){
        Boolean chiusuraForzata = false; //elemento importante per la fase di chiusura
        try{
            ServerSocket ss = new ServerSocket(3000);
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
               System.out.println("Errore di appertura del server" );
               e.setStackTrace(getStackTrace());
            }
        }
}
