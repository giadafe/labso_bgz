package master;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.List;

/*
    * ==================
    * Master
    * =================
	* Classe principale che avvia il server Master per la gestione delle risorse condivise tra i peer.	
*/

public class Master {
	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			// controlla che il programma sia avviato con un solo parametro (la porta).
			System.out.println("Scrivere a terminale: java master.Master <inserire numero della porta>, esempio: java master.Master 9000"); 
			return;
		}
		int port;
		try {
			port = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			System.out.println("La porta deve essere un numero intero.");
			return;
		}

		// Creazione del ServerSocket
		ServerSocket ss = new ServerSocket(port);
		System.out.println("ServerSocket in ascolto sulla porta " + port + "...");

		// Fase di avvio del thread per la gestione dei comandi del Master
		Thread masterComandi = new Thread(new masterComandi(ss));
		masterComandi.start();

		//Fase di accettazione delle connessioni in ingresso dai client peer
		try {
			while (true) {
				Socket socket = ss.accept(); // accetta una connessione in ingresso
				System.out.println("Client connesso!");

				
			}
		} catch (SocketException e) {
			System.out.println("Server terminato: " + e.getMessage());
		}
	}
}