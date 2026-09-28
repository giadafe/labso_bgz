package client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * SalvaggioDati
 * Classe centrale che si occupa di gestire i salvataggi del 
 * nome della macchina/nodo,
 * rilevazioni > Token
 * Token > chiave decriptazione negli appositi file 
 * Quando e richiesto.
 */

public class SalvaggioDati {
    //salvataggio del nome del nodo/macchina ]




    public void salvataggio(String nomeMacchina) {
        File directory = new File("client/rilevazioni");
        File file = new File(directory, "nomeMacchina.txt");

        try {
            if (!directory.exists()) {
                if (directory.mkdirs()) {
                    System.out.println("\u001B[32m[CLIENT] Cartella 'rilevazioni' creata.\u001B[0m");
                } else {
                    System.out.println("\u001B[31m[CLIENT] Impossibile creare la cartella.\u001B[0m");
                    return; 
                }
            }
            try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
                out.print(nomeMacchina);
                out.flush(); 
                System.out.println( "\u001B[32m[CLIENT] Nome del nodo salvato correttamente \u001B[0m");            
            }

        } catch (IOException e) {
            System.out.println("\u001B[31m[CLIENT] Errore di accesso al file \u001B[0m");       
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
                System.out.println( "\u001B[32m[CLIENT] Salvataggio delle rilevazioni avvenuto con successo \u001B[0m");            
            } catch (IOException e) {
                System.out.println("\u001B[31m[CLIENT] Errore di salvataggio \u001B[0m");       
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
                System.out.println( "\u001B[32m[CLIENT] Salvataggio dei token avvenuto con successo \u001B[0m");            
            } catch (IOException e) {
                System.out.println("\u001B[31m[CLIENT] Errore di salvataggio \u001B[0m");       
            }
        }
    }

}