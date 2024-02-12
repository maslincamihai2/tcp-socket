package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread{
    @Override
    public void run(){
        try {
            ServerSocket serverSocket = new ServerSocket(Main.port);

            while (!Thread.interrupted()){
                Socket predecesor = serverSocket.accept();

                BufferedReader in = new BufferedReader(new InputStreamReader(predecesor.getInputStream()));
                PrintWriter out = new PrintWriter(predecesor.getOutputStream(), true);

                String mesajPrimit = in.readLine();
                out.println("mesaj primit de catre succesor");

                in.close();
                out.close();
                predecesor.close();

                // exista cazul in care un mesaj se trimite in bucla la infinit
                String[] split = mesajPrimit.split(" ");
                boolean ok = true;
                for(String string: split) {
                    if (string.equals(String.valueOf(Main.port))) {
                        ok = false;
                        break;
                    }
                }
                if(ok){
                    Main.trimiteMesaj(Main.remoteIp, Main.remotePort, mesajPrimit + ' ' + Main.port);
                }
                else{
                    System.out.println("Mesajul a parcurs tot inelul");
                }
            }
        } catch (IOException e) {
            System.out.println("eroare server thread");
            throw new RuntimeException(e);
        }
    }
}
