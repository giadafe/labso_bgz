package client.comandi;

import java.util.Map;

public class listDataLocal {

    public synchronized void mostraLista(Map<String,String> datiRilevazione) {

        System.out.println("Risorse: ");
        for(String n: datiRilevazione.keySet()){
            System.out.println("- " + n);
        }

    }
    
}
