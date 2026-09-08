package client.comandi;

import java.util.Map;

public class AssegnaToken {
    public String aggiungiToken(String token, Map<String, String> tokenSblocco){
        if(tokenSblocco.containsKey(token)){
            return "TOKEN_ESISTENTE";
        }else{
            return token;
        }
    }
}