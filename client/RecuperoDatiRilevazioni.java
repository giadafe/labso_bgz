package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class RecuperoDatiRilevazioni {
    public Map<String,String> datiRilevazione;
    public Map<String,String> tokenSblocco;

    public RecuperoDatiRilevazioni(Map<String,String> datiRilevazione, Map<String,String> tokenSblocco){
        this.datiRilevazione= datiRilevazione;
        this.tokenSblocco = tokenSblocco;
    }




    //metodo che serve a caricare la lista nella fase di accensione del client
    public void caricamentoLista(){
        //accedo al file rilecazioni/rilevazioni.txt
        //per ogni riga recuperare "nome valore"
        //dividere "nome valore" in nome e valore, iul valore sara crittografato in futuro
        File directory = new File("client/rilevazioni");    //accesso al file 
        File rilevazioni = new File(directory,"rilevazioni.txt");
        File token = new File(directory,"token.txt");    //accesso al file 
        //test di ottimizzazione 
        inserimentoNellaLista(directory, rilevazioni, datiRilevazione);
        inserimentoNellaLista(directory,token,tokenSblocco);
    }





    //nuova funzione per ottimizzare il processo
    public void inserimentoNellaLista(File directory, File tipologia, Map<String,String> listaDati){
        try(BufferedReader letturaRighe = new BufferedReader(new FileReader(tipologia))){
            String rilevazione;
            while((rilevazione = letturaRighe.readLine())!=null){
                String [] scomposizione = rilevazione.split(":"); // creo un array "nome valore"
                String nomeRilevazione = scomposizione[0]; //nome 
                String valore= scomposizione[1];//valore
                listaDati.put(nomeRilevazione, valore);
            }


        }catch(IOException expt){
            System.out.println("Errore nella lettura del file");
        }
    }
}
