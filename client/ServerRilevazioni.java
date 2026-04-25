package client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
public class ServerRilevazioni extends Thread {
    private int portaAggregator;

    public ServerRilevazioni(int portaAggregator, BufferedReader inServer, PrintWriter outServer) {
        this.portaAggregator=portaAggregator;
    }

    @Override 

    public void run(){
        System.out.println("Server avviato");
        try{
            ServerSocket ss = new ServerSocket(3000);
            LinkedBlockingQueue<Socket> listaSocekt = new LinkedBlockingQueue<>();
            ThreadSmistamentoRichieste smistamento = new ThreadSmistamentoRichieste(listaSocekt);
            smistamento.start();
            while(true){
                Socket clientAccettato = ss.accept();
                try{
                    listaSocekt.put(clientAccettato);
                }catch(InterruptedException e){
                    System.out.println("Errore nella fase di caricamento dellélemento in lista");
                }
            }
          }catch(IOException e){
               System.out.println("Errore di appertura del server" );
               e.setStackTrace(getStackTrace());
            }
        }
}
