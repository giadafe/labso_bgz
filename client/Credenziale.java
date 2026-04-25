package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Credenziale {

    public String nomeMacchina() {
        File directory = new File("client/rilevazioni");
        File file = new File(directory, "nomeMacchina.txt");
        String nome = null;

        if (directory.exists() && file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                nome = br.readLine();
            } catch (IOException e) {
                System.out.println("Errore durante la lettura del file config");
            }
        }

        if (nome == null || nome.isEmpty()) {
            return "";
        } else {
            return nome;
        }
    }
}