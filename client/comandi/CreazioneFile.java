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
        System.out.println("=== INIZIO CREAZIONE FILE ===");
        
        // Definiamo il percorso completo del file dentro la cartella "rilevazioni"
        Path percorsoFile = Paths.get("rilevazioni", nomeRilevazione);
        Path cartellaPadre = percorsoFile.getParent();

        System.out.println("[LOG] Percorso destinazione file: " + percorsoFile.toAbsolutePath());

        try {
            // 1. Verifica ed eventuale creazione della directory madre "rilevazioni"
            if (cartellaPadre != null && !Files.exists(cartellaPadre)) {
                System.out.println("[LOG] Cartella '" + cartellaPadre + "' inesistente. Creazione in corso...");
                Files.createDirectories(cartellaPadre);
                System.out.println("[LOG] -> Cartella creata con successo!");
            } else {
                System.out.println("[LOG] Cartella '" + cartellaPadre + "' già esistente.");
            }

            // 2. Scrittura del contenuto cifrato nel file
            System.out.println("[LOG] Scrittura del contenuto crittografato nel file...");
            Files.writeString(percorsoFile, contenutoCrittografato);
            
            System.out.println("[LOG] -> File '" + nomeRilevazione + "' creato e salvato con successo!");
            System.out.println("=== FILE CREATO CON SUCCESSO ===\n");
            return true;

        } catch (IOException e) {
            System.err.println("[ERRORE] Processo di creazione del file fallito per: " + nomeRilevazione);
            e.printStackTrace();
            System.out.println("=== CREAZIONE FILE FALLITA ===\n");
            return false;
        }
    }
}