package client.comandi;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class CrittografiaAES {

    public String[] crittografaIlContenuto(String contenuto) {
        System.out.println("=== INIZIO PROCESSO CRITTOGRAFIA AES ===");
        System.out.println("[LOG] Testo originale in ingresso: \"" + contenuto + "\"");

        try {
            // 1. Generazione della chiave
            System.out.println("[LOG] 1. Generazione chiave AES a 256 bit in corso...");
            KeyGenerator generatoreChiave = KeyGenerator.getInstance("AES");
            generatoreChiave.init(256);
            SecretKey chiave = generatoreChiave.generateKey();
            System.out.println("[LOG] -> Chiave generata con successo!");

            // 2. Inizializzazione del Cipher
            System.out.println("[LOG] 2. Inizializzazione algoritmo Cipher (AES)...");
            Cipher cipherText = Cipher.getInstance("AES");
            cipherText.init(Cipher.ENCRYPT_MODE, chiave);

            // 3. Conversione testo in byte e Cifratura
            System.out.println("[LOG] 3. Conversione del testo in byte (UTF-8) e cifratura...");
            byte[] contenutoInByte = contenuto.getBytes(StandardCharsets.UTF_8);
            byte[] cipherTextBytes = cipherText.doFinal(contenutoInByte);
            System.out.println("[LOG] -> Cifratura completata! Dimensione ciphertext: " + cipherTextBytes.length + " byte.");

            // 4. Encoding in Base64
            System.out.println("[LOG] 4. Encoding del contenuto cifrato e della chiave in Base64...");
            String contenutoCrittografato = Base64.getEncoder().encodeToString(cipherTextBytes);
            String chiaveBase64 = Base64.getEncoder().encodeToString(chiave.getEncoded());

            // Stampa dei risultati finali
            System.out.println("[LOG] -> Contenuto cifrato (Base64): " + contenutoCrittografato);
            System.out.println("[LOG] -> Chiave segreta (Base64): " + chiaveBase64);
            System.out.println("=== CRITTOGRAFIA COMPLETATA CON SUCCESSO ===\n");

            return new String[]{contenutoCrittografato, chiaveBase64};

        } catch (Exception e) {
            System.err.println("[ERRORE] Si è verificato un errore durante la crittografia!");
            e.printStackTrace();
            System.out.println("=== CRITTOGRAFIA FALLITA ===\n");
            
            return new String[]{"", ""};
        }
    }
}