package client.comandi;

import java.util.Map;

public class AssegnaToken {
    public String aggiungiToken(String token, Map<String, String> tokenSblocco){
        System.out.println("[CHECK] Controllo disponibilità token: '" + token + "'...");
        
        if(tokenSblocco.containsKey(token)){
            System.out.println("[WARNING] Token '" + token + "' già presente nella mappa tokenSblocco!");
            return "TOKEN_ESISTENTE";
        }else{
            System.out.println("[OK] Token '" + token + "' valido e disponibile.");
            return token;
        }
    }
}