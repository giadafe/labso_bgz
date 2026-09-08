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

		//risorse condivise traq i thread.
		// creazione di un hashmap per la gestione dei socket 
		HashMap<String, Socket> socketClient = new HashMap<>(); // nomeMacchina > socket
		//creazione di  un hashmap risorsa disponobilita 
		HashMap<String, String> disponibilitaRilevazione = new HashMap<>(); // nomeRisorsa > DISPONIBILE | OCCUPATO
		//creazione di una hasmap per salvare il nome della macchina, ip e porta
		HashMap<String, String> infoMacchinaClient = new HashMap<>(); // nomeMacchina > ip, porta, online | offline

		//creazione hashmap per le rilevazioni CHIAVE PRIMARIA nome risorsa
		HashMap<String, List<String>> infoRilevazioni = new HashMap<>();
		GestoreLog gestoreLog = new GestoreLog();

		// Creazione del ServerSocket
		ServerSocket ss = new ServerSocket(port);
		System.out.println("ServerSocket in ascolto sulla porta " + port + "...");

		// Fase di avvio del thread per la gestione dei comandi del Master
		Thread masterComandi = new Thread(new masterComandi(ss, infoRilevazioni, gestoreLog));
		masterComandi.start();

		//Fase di accettazione delle connessioni in ingresso dai client peer
		try {
			while (true) {
				Socket socket = ss.accept(); // accetta una connessione in ingresso
				System.out.println("Client connesso!");

				// preparazione dei BufferedReader e BufferedWriter per permettere al Master di leggere e
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				
				//Fase di avvio del thread per la gestione delle richieste dei client peer
				
				GestioneClient ThreadRichieste = new GestioneClient(socket, in, out, infoMacchinaClient, infoRilevazioni,
						gestoreLog,disponibilitaRilevazione ,socketClient);
				
				ThreadRichieste.start();
			}

		} catch (SocketException e) {
			System.out.println("Server terminato: " + e.getMessage());
		}
	}
}