package client.comandi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class listDataLocal {

    public synchronized void mostraLista(Map<String,String> datiRilevazione, PrintWriter outServer, String comando, BufferedReader inServer) {
        outServer.println(comando);
        String responseServer;
        boolean  permesso = false;
        try {
            responseServer = inServer.readLine();
            if(responseServer.equals("accesso_lista_local")){
                permesso = true;
            }else{
                System.out.println("\u001B[31m[CLIENT]  permesso negato\u001B[0m");           
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



        if(permesso){
            if(datiRilevazione.size() > 0 ){
                System.out.println("Risorse: ");
                for(String n: datiRilevazione.keySet()){
                    System.out.println("- " + n);
                }
            }else{
                System.out.println("[CLIENT] Non hai rilevazioni da mostrare");
            }
        }


    }
    
}
