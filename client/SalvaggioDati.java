package client;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SalvaggioDati {

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


    // aggiungere il metodo che permette il salvataggio della risorsa in  un hashmap String String => nomeRilevazioni : nomeMacchina, risultatoRilevazione.
}