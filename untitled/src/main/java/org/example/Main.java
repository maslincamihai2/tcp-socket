package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static java.lang.System.exit;

public class Main {
    public static int port;
    public static String remoteIp;
    public static int remotePort;

    public static void main(String[] args) {
        if (args.length == 0){
            exit(-1);
        }

        port = Integer.parseInt(args[0]);
        // ip-ul si portul pe care ruleaza nodul succesor
        remoteIp = args[1];
        remotePort = Integer.parseInt(args[2]);

        Thread serverThread = new ServerThread();
        serverThread.start();

        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        String mesaj;
        do{
            try {
                mesaj = consoleReader.readLine();
            } catch (IOException e) {
                System.out.println("eroare citire comanda");
                throw new RuntimeException(e);
            }

            trimiteMesaj(remoteIp, remotePort, mesaj + ' ' + port);
        }while (!mesaj.equals("exit"));
    }

    /**
     * Metoda pentru trimiterea unui mesaj catre un alt peer cand se cunoaste ip-ul si portul
     * @param ipDestinatar este ip-ul acelui peer caruia i se trimite mesajul
     * @param portDestinatar este portul pe care asculta destinatarul cererile de la alti peers
     * @param mesaj este continutul mesajului
     * @return raspunsul primit in urma mesajului sau sir gol ("") in caz de eroare
     */
    public static String trimiteMesaj(String ipDestinatar, int portDestinatar, String mesaj) {
        try {
            // deschide socket pentru comunicarea cu succesorul
            Socket socketClient = new Socket(ipDestinatar, portDestinatar);

            // fluxuri pentru citire si scriere
            BufferedReader in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
            PrintWriter out = new PrintWriter(socketClient.getOutputStream(), true);

            out.println(mesaj);
            String raspuns = in.readLine();

            System.out.println("Raspuns pentru mesajul " + mesaj + " catre " + ipDestinatar + ":" + portDestinatar);
            System.out.println(raspuns);
            System.out.println("-----------------------------------------------");
            return raspuns;
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("eroare trimitere mesaj " + mesaj + " catre " + ipDestinatar + ":" + portDestinatar);
            System.out.println("************************************************");
            return "";
        }
    }
}