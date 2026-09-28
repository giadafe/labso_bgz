package client.comandi;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class CrittografiaAES {

    public String[] crittografaIlContenuto(String contenuto) {

        try {
            // Generazione della chiave
            KeyGenerator generatoreChiave = KeyGenerator.getInstance("AES");
            generatoreChiave.init(256);
            SecretKey chiave = generatoreChiave.generateKey();
            //Inizializzazione della crittografia con la chiave
            Cipher cipherText = Cipher.getInstance("AES");
            cipherText.init(Cipher.ENCRYPT_MODE, chiave);

            // converisone in byte del testo da cifrare
            byte[] contenutoInByte = contenuto.getBytes(StandardCharsets.UTF_8);
            byte[] cipherTextBytes = cipherText.doFinal(contenutoInByte);

            // conversione in base64 per la lettura 
            String contenutoCrittografato = Base64.getEncoder().encodeToString(cipherTextBytes);
            String chiaveBase64 = Base64.getEncoder().encodeToString(chiave.getEncoded());
            System.out.println("\u001B[32m[CLIENT] Crittografia completata\u001B[0m");
            return new String[]{contenutoCrittografato, chiaveBase64};

        } catch (Exception e) {
            System.err.println("\u001B[31m[CLIENT] Errore nella fase di crittografia: " + e.getMessage() + "  Crittografia fallita\u001B[0m");            
            return new String[]{"", ""};
        }
    }
}