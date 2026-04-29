package client;

import java.io.File;
import java.io.IOException;

public class ControlloDirectory {
    


    public void controlloDirectoryRilevazioni(){
        File directory = new File("client/rilevazioni");
        File fileNomeMacchina = new File(directory, "nomeMacchina.txt");
        File fileRilevazioni = new File(directory, "rilevazioni.txt");
        File fileToken = new File(directory,"token.txt");

        //check cartella
        if(!directory.exists()){
            directory.mkdir();
        }else{
            System.out.println("Cartella presente");
        }



        //check file nomeMacchina
        if(! fileNomeMacchina.exists()){
          try{
            fileNomeMacchina.createNewFile();

            }catch(IOException err){
                System.out.println("Errore nella creazione delfile ");
            }
        }else{
            System.out.println("File presente");
        }

        //check file rilevazioni
        if(!fileRilevazioni.exists()){
            try{
                fileRilevazioni.createNewFile();
            }catch(IOException err){
                System.out.println("Errore nella creazione delfile ");
            }
        }

        if(!fileToken.exists()){
            try{
                fileToken.createNewFile();
            }catch(IOException err){
                System.out.println("Errore nella creazione delfile ");
            }
        }


    }

}
