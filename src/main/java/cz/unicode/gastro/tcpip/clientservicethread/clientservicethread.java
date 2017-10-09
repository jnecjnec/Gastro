package cz.unicode.gastro.tcpip.clientservicethread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

import cz.unicode.gastro.gastroManager.IrecievedMessageListener;
import java.io.DataOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class clientservicethread extends Thread {

    boolean f_RunThread = true;
    boolean f_waitForData = false;
    String completeMessage = "";
    String unexpectedMessage = "";
    char ETX = '\3';
    char STX = '\2';
    ArrayList<clientservicethread> clientlist = null;
    DataOutputStream out = null;
    BufferedReader in = null;

    protected Socket myClientSocket;
    protected IrecievedMessageListener listener = null;

    public void setListener(IrecievedMessageListener pListener) {
        listener = pListener;
    }

    public clientservicethread() {
        super();

    }

    public clientservicethread(Socket s, ArrayList<clientservicethread> pClientlist) {
        myClientSocket = s;
        clientlist = pClientlist;
    }

    public void setOutMessage(String pMessage) {
        //  dada to send
        if (!pMessage.isEmpty()) {
            try {
                out = new DataOutputStream(myClientSocket.getOutputStream());
                try {
                    out.writeBytes(STX + pMessage + ETX);
                } catch (IOException ex) {
                    Logger.getLogger(clientservicethread.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                Logger.getLogger(clientservicethread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void clientStop() {
        f_RunThread = false;
        // Clean up
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if ((myClientSocket != null) && (!myClientSocket.isClosed())) {
            try {
                myClientSocket.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        if (clientlist != null) {
            clientlist.remove(this);
        }
        System.out.println("...Client Stopped");
    }

    @Override
    public void run() {
        if (clientlist != null) {
            clientlist.add(this);
        }

        // Print out details of this connection
        System.out.println("Accepted Client Address - " + myClientSocket.getInetAddress().getHostName() + ":" + myClientSocket.getPort());

        try {
            in = new BufferedReader(new InputStreamReader(myClientSocket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(clientservicethread.class.getName()).log(Level.SEVERE, null, ex);
        }

        // At this point, we can read for input and reply with
        // appropriate output.
        // Run in a loop until m_bRunThread is set to false
        while (f_RunThread) {

            // read incoming byte
            int i = 0;
            try {
                i = in.read();
            } catch (IOException ex) {
                Logger.getLogger(clientservicethread.class.getName()).log(Level.SEVERE, null, ex);
                f_RunThread = false;
            }

            if (i > 0) {
                char readedChar = (char) i;

                if (readedChar == STX) {
                    completeMessage = "";
                    f_waitForData = true;
                    if (!unexpectedMessage.isEmpty()) {
                        System.out.println("Unexpected message " + unexpectedMessage);
                    }
                } else if (readedChar == ETX) {
                    f_waitForData = false;
                    System.out.println("Complete message " + completeMessage);
                    unexpectedMessage = "";

                    if (listener != null) {
                        try {
                            listener.Message(completeMessage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else // Process it
                 if (f_waitForData) {
                        completeMessage += readedChar;
                    } else {
                        unexpectedMessage += readedChar;
                    }
            }
        }
        clientStop();
    }
}
