package client;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * DecrittazioneAES
 * Clase che si occupa di decrittare la rilevazione 
 * e restituisce in byte la rilevazione decifrata
 */
public class DecrittazioneAES {

    public byte[] decrittazione(String chiaveBase64, String nomeFile, File fileCifrato) {
        try {
            String testoCifrato= Files.readString(fileCifrato.toPath(), StandardCharsets.UTF_8).trim();
 
            byte[] cipherTextBytes = Base64.getDecoder().decode(testoCifrato);
            byte[] chiave = Base64.getDecoder().decode(chiaveBase64);

            SecretKeySpec chiaveSpec = new SecretKeySpec(chiave, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, chiaveSpec);

            byte[] contenutoDecrittato = cipher.doFinal(cipherTextBytes);

            return contenutoDecrittato; 

        } catch (Exception e) {
            System.out.println("Errore durante la decrittazione: " + e.getMessage());
            return null;
        }
    }
}