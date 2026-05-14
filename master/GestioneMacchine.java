package master;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class GestioneMacchine {
    public Map<String, String> registroMacchina;



    public GestioneMacchine(Map<String, String> registroMacchina){
        this.registroMacchina = registroMacchina;
    }



    //metodo che serve per salvare il dato nella fase di controllo, si attiva una volta per nuova connessione
    public synchronized void salvaMacchine(){
        File file = new File("master/dataMacchine/macchineRegistrate.txt");
        // Usa FileWriter per scrivere e PrintWriter per avere il metodo println()
        try (PrintWriter scrittura = new PrintWriter(new FileWriter(file))) {
                for (Map.Entry<String, String> n : registroMacchina.entrySet()) {            
                    String nomeMacchina = n.getKey();
                    String credenziali = n.getValue();
                    scrittura.println(nomeMacchina +":"+ credenziali);
                }
        } catch (IOException e) {
            System.err.println("[ERRORE] Impossibile scrivere il file di backup");
            e.printStackTrace();
        }
    }




    //quando il server si accende fa a prendere i dati delle macchine registrate con nome -> (ip porta)
    public void recuperoMacchine(){
        File file = new File("master/dataMacchine/macchineRegistrate.txt");
        if (file.exists()) {
            try (BufferedReader letturaFile = new BufferedReader(new FileReader(file))) {
                    String riga;
                    while((riga = letturaFile.readLine())!= null){
                        String [] macchineSalvate = riga.split(":");
                        String chiave = macchineSalvate[0]; //nomeMacchina
                        String valore = macchineSalvate[1]+":"+macchineSalvate[2]; //ip[1] porta[2]
                        registroMacchina.put(chiave, valore); //salvataggio diretto
                    }
            System.out.println("[BACKUP] Ripristinate " + registroMacchina.size() + " macchine.");
        } catch (IOException e) {
                System.out.println("Errore durante la lettura del file config");
            }
        }
    }


    //metodo per il recupero dei dati delle rilevazioni 
    public void salvataggioDatiRilevazioni(){

    }

    public void recuperoDatiRilevazioni(){
        
    }

}
