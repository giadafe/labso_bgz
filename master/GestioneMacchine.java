package master;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestioneMacchine {
    public Map<String, String> registroMacchina;
    public HashMap<String,List<String>> infoRilevazioni;


    public GestioneMacchine(Map<String, String> registroMacchina, HashMap<String,List<String>> infoRilevazioni){
        this.registroMacchina = registroMacchina;
        this.infoRilevazioni = infoRilevazioni;
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




    //quando il server si accende fa a prendere i dati delle macchine registrate con nome -> (ip porta, stato)
    public void recuperoMacchine(){
        File file = new File("master/dataMacchine/macchineRegistrate.txt");
        if (file.exists()) {
            try (BufferedReader letturaFile = new BufferedReader(new FileReader(file))) {
                    String riga;
                    while((riga = letturaFile.readLine())!= null){
                        String [] macchineSalvate = riga.split(":");
                        String chiave = macchineSalvate[0]; //nomeMacchina
                        String valore = macchineSalvate[1]+":"+macchineSalvate[2]+":"+macchineSalvate[3] ; //ip[1] porta[2] stato[3]
                        registroMacchina.put(chiave, valore); //salvataggio diretto
                    }
            System.out.println("[BACKUP] Ripristinate " + registroMacchina.size() + " macchine.");
        } catch (IOException e) {
                System.out.println("Errore durante la lettura del file config");
            }
        }
    }









    //salvataggio della hashmap in locale delle rilevazioni
    public synchronized void salvataggioDatiRilevazioni(HashMap<String,List<String>> infoRilevazioni){
        List<String> listaPeerLocale = new ArrayList<>();
        for(String chiave : infoRilevazioni.keySet()){
            //recupero la lista di peer associati
            List<String> listaPeer = infoRilevazioni.get(chiave);
            String rigaSalvataggio = chiave + "|" + String.join("|", listaPeer);
            listaPeerLocale.add(rigaSalvataggio);
        }
        //salvataggio nel file locale
        File file = new File("master/dataMacchine/infoRilevazione.txt");
        try (BufferedWriter  scritturaFile = new BufferedWriter (new FileWriter(file))) {
            for(String riga : listaPeerLocale){
                scritturaFile.write(riga);
                scritturaFile.newLine();
            }
            System.out.println("Rilevazioni salvate in locale");
        }catch(IOException expt){
            expt.printStackTrace();
        }

    }


















        // recupero delle rilevazioni in locale
        public void recuperoDatiRilevazioni(){
            File file = new File("master/dataMacchine/infoRilevazioni.txt");
            if(file.exists()){
                try (BufferedReader letturaFile = new BufferedReader(new FileReader(file))) {

                    String riga;

                    while((riga = letturaFile.readLine()) != null){
                        String[] rilevazioniRecuperate = riga.split("\\|");
                        String chiave = rilevazioniRecuperate[0];
                        List<String> listaPeer = new ArrayList<>();
                        for(int i = 1; i < rilevazioniRecuperate.length; i++){
                            listaPeer.add(rilevazioniRecuperate[i]);
                        }
                        infoRilevazioni.put(chiave, listaPeer);
                    }
                } catch(IOException expt){
                    expt.printStackTrace();
                }
            }
        }








}
