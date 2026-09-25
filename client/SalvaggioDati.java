package client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SalvaggioDati {
    //salvataggio del nome del nodo/macchina ]




    public void salvataggio(String nomeMacchina) {
        File directory = new File("client/rilevazioni");
        File file = new File(directory, "nomeMacchina.txt");

        try {
            if (!directory.exists()) {
                if (directory.mkdirs()) {
                    System.out.println("Cartella 'rilevazioni' creata.");
                } else {
                    System.out.println("Impossibile creare la cartella.");
                    return; 
                }
            }
            try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
                out.print(nomeMacchina);
                out.flush(); 
                System.out.println("Nome '" + nomeMacchina + "' salvato con successo in: " + file.getPath());
            }

        } catch (IOException e) {
            System.out.println("Errore durante l'accesso al file: " + e.getMessage());
        }
    }







    //salvataggio delle rilevazioni in locale
    public void salvataggioDatiRilevazioni(Map<String,String> datiRilevazione){ //nome > token
        List<String> stringheSalvataggio = new ArrayList<>();
        synchronized(datiRilevazione){
            for(String k : datiRilevazione.keySet()){
                String valore = datiRilevazione.get(k);
                String concatenzaione = k + ":"+ valore;
                stringheSalvataggio.add(concatenzaione);
            }
            File drectory = new File("client/rilevazioni");
            File file = new File(drectory, "rilevazioni.txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String riga : stringheSalvataggio) {
                    writer.write(riga);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }






    //salvataggio dei token in locale
    public void salvataggioTokenRilevazioni(Map<String,String> tokenSblocco){ //token > chiave
        List<String> listaToken  = new ArrayList<>();
        synchronized(tokenSblocco){
            for(String t : tokenSblocco.keySet()){
                String valore = tokenSblocco.get(t);
                String concatenzaione = t + ":"+ valore;
                listaToken.add(concatenzaione);
            }

            File drectory = new File("client/rilevazioni");
            File file = new File(drectory, "token.txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String riga : listaToken) {
                    writer.write(riga);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}