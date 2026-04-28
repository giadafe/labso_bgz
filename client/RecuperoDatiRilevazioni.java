package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class RecuperoDatiRilevazioni {
    public Map<String,String> datiRilevazione;


    public RecuperoDatiRilevazioni(Map<String,String> datiRilevazione){
        this.datiRilevazione= datiRilevazione;
    }




    //metodo che serve a caricare la lista nella fase di accensione del client
    public void caricamentoLista(){
        //accedo al file rilecazioni/rilevazioni.txt
        //per ogni riga recuperare "nome valore"
        //dividere "nome valore" in nome e valore, iul valore sara crittografato in futuro
        System.out.println("Lanciato il metodo di aggiornamento della Lista");

        File directory = new File("client/rilevazioni/rilevazioni.txt");    //accesso al file 
        try(BufferedReader letturaRighe = new BufferedReader(new FileReader(directory))){
            String rilevazione;
            while((rilevazione = letturaRighe.readLine())!=null){
                String [] scomposizione = rilevazione.split(" "); // creo un array "nome valore"
                String nomeRilevazione = scomposizione[0]; //nome 
                String valoreRilevazione = scomposizione[1];//valore
                datiRilevazione.put(nomeRilevazione, valoreRilevazione);
            }
            System.out.println("Non ci sono piu righe da fetchare");
            System.out.println("Dati recuperati" + datiRilevazione);

        }catch(IOException expt){
            System.out.println("Errore nella lettura del file");
        }
    }
}
