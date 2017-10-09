package cz.unicode.gastro.tcpip.tcpipserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.google.inject.Singleton;

import cz.unicode.gastro.gastroManager.IrecievedMessageListener;
import cz.unicode.gastro.tcpip.clientservicethread.clientservicethread;

@Singleton
public class tcpipserver extends Thread {

    ServerSocket myServerSocket;
    int port;
    boolean ServerOn = true;
    IrecievedMessageListener listener = null;

    ArrayList<clientservicethread> clientList = new ArrayList<clientservicethread>();

    private void stopAllClients() {
        for (clientservicethread cli : clientList) {
            cli.clientStop();
        }
    }

    public void sendMessageToAllClients(String pMessage) {
        for (clientservicethread cli : clientList) {
            cli.setOutMessage(pMessage);
        }
    }

    public void serverRun() {
        start();
    }

    public void serverStop() {
        ServerOn = false;
        stopAllClients();
        if ((myServerSocket != null) && (!myServerSocket.isClosed())) {
            try {
                myServerSocket.close();
            } catch (Exception ioe) {
                System.out.println("Problem stopping server socket");
            }
        }
        System.out.println("Server Stopped");
    }

    public void setListener(IrecievedMessageListener pListener) {
        listener = pListener;
    }

    public tcpipserver() {
        super();
    }

    public tcpipserver(int pPort) {
        port = pPort;
        Calendar now = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
        System.out.println("It is now : " + formatter.format(now.getTime()));

    }

    @Override
    public void run() {
        try {
            myServerSocket = new ServerSocket(port);
        } catch (IOException ioe) {
            System.out.println("Could not create server socket on port " + port + ". Quitting.");
        }
        // Successfully created Server Socket. Now wait for connections.
        while (ServerOn) {
            // Accept incoming connections.
            Socket clientSocket = null;
            try {
                clientSocket = myServerSocket.accept();
            } catch (Exception e) {
                // pokud to ukoncim z venku atk aby to nehlasilo chybu
                // e.printStackTrace();
            }

            // accept() will block until a client connects to the server.
            // If execution reaches this point, then it means that a client
            // socket has been accepted.
            // For each client, we will start a service thread to
            // service the client requests. This is to demonstrate a
            // Multi-Threaded server. Starting a thread also lets our
            // MultiThreadedSocketServer accept multiple connections
            // simultaneously.
            // Start a Service thread
            
            if ((ServerOn) && (clientSocket != null)) {
                clientservicethread cliThread = new clientservicethread(clientSocket, clientList);
                cliThread.setListener(listener);
                cliThread.start();
            }
        }
        serverStop();
    }
}
