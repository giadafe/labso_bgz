package client.comandi;

import java.util.Map;
import java.util.Random;

public class AssegnaToken {

    //generatore di token casuale per mascherare la chiave AES
    public String generaTokenCasuale(){
        String lettere = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toLowerCase();
        Random random = new Random();
        String token = ""; 
        for (int i = 0; i < 5; i++) {
            int index = random.nextInt(lettere.length());
            token = token + lettere.charAt(index); // Aggiunge il carattere scelto
        }

        System.out.println("Token: " + token); 
        // Esempio output: "esatd", "rtsda", "eetad"
        return token;
    }



    public String aggiungiToken(String token, Map<String, String> tokenSblocco){
        if(tokenSblocco.containsKey(token)){
            return "TOKEN_ESISTENTE";
        }else{
            return token;
        }
    }
}