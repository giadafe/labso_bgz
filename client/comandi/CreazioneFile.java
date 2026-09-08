package client.comandi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Creazione del file crittografato
 */
public class CreazioneFile {

    public boolean creazioneFile(String nomeRilevazione, String contenutoCrittografato) {
        

        Path percorsoFile = Paths.get("rilevazioni", nomeRilevazione);
        Path directory = percorsoFile.getParent();


        try {

            if (directory != null && !Files.exists(directory)) {
                System.out.println("creazione della cartella");
                Files.createDirectories(directory);
            } else {

            }

            Files.writeString(percorsoFile, contenutoCrittografato);
            
            System.out.println("File creato con successo");
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("File non creato");
            return false;
        }
    }
}