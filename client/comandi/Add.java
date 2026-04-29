package client.comandi;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Map;

public class Add {
    public String [] scomposizioneComando;
    public Map<String,String>datiRilevazione;
    public BufferedReader inServer;
    public  PrintWriter outServer;

    public Add(String[] scomposizioneComando, BufferedReader inServer, PrintWriter outServer, Map<String,String> datiRilevazione) {
        this.scomposizioneComando =scomposizioneComando;
        this.inServer=inServer;
        this.outServer=outServer;
        this.datiRilevazione=datiRilevazione;
    }

    public synchronized void addRilevazione(){
        String comandoScelto = scomposizioneComando[0];
        String nomeRilevazione = scomposizioneComando[1];
        String valoreRilevazione = scomposizioneComando[2];
        System.out.println(comandoScelto); //comando da mandare al master
        System.out.println(nomeRilevazione);
        System.out.println(valoreRilevazione);
        //mando i dati al master
        //ciclo per verificare che il nome non sia duplicato 
        for(String n : datiRilevazione.keySet() ){
            if(nomeRilevazione!=n){
                //salvataggio nella lista dei datiRilevazione 
                datiRilevazione.put(nomeRilevazione, valoreRilevazione);
                System.out.println(datiRilevazione);               
            }
        }


        //salvataggio enlla lista dei token

    }
}
