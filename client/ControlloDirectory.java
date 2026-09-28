package client;

import java.io.File;
import java.io.IOException;
/**
 * ControlloDirectory
 * Classe centrale che si occupa di gestire le directory e file necessarie per l'esecuzione del progetto
 * se non esistono le crea prima cheil progetto continui con le prossime fasi
 */
public class ControlloDirectory {
    


    public void controlloDirectoryRilevazioni(){
        File directory = new File("client/rilevazioni");
        File fileNomeMacchina = new File(directory, "nomeMacchina.txt");
        File fileRilevazioni = new File(directory, "rilevazioni.txt");
        File fileToken = new File(directory,"token.txt");
        File directoryFileScaricati = new File("client/rilevazioniScaricate");


        //check cartella
        if(!directoryFileScaricati.exists()){
            directoryFileScaricati.mkdir();
        }else{
            System.out.println("\u001B[32m[CLIENT] Cartella presente\u001B[0m");        
        }


        //check cartella
        if(!directory.exists()){
            directory.mkdir();
        }else{
            System.out.println("\u001B[32m[CLIENT] Cartella presente\u001B[0m");        
        }





        //check file nomeMacchina
        if(! fileNomeMacchina.exists()){
          try{
            fileNomeMacchina.createNewFile();
            }catch(IOException err){
                System.out.println("\u001B[31mErrore nella creazione del file\u001B[0m");            
            }
        }else{
            System.out.println("\u001B[32m[CLIENT] File esistente\u001B[0m");        
        }




        //check file rilevazioni
        if(!fileRilevazioni.exists()){
            try{
                fileRilevazioni.createNewFile();
            }catch(IOException err){
                System.out.println("\u001B[32m[CLIENT] File esistente\u001B[0m");        
            }
        }else{
            System.out.println("\u001B[32m[CLIENT] File esistente\u001B[0m");        
        }





        if(!fileToken.exists()){
            try{
                fileToken.createNewFile();
            }catch(IOException err){
                System.out.println("\u001B[32m[CLIENT] File esistente\u001B[0m");        
            }
        }else{
            System.out.println("\u001B[32m[CLIENT] File esistente\u001B[0m");        
        }


    }

}
