package cz.unicode.gastro.tcpip.tcpipserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class tcpipserver {

	ServerSocket myServerSocket;
	boolean ServerOn = true;

	public tcpipserver(int pPort) {
		try {
			myServerSocket = new ServerSocket(pPort);
		} catch (IOException ioe) {
			System.out.println("Could not create server socket on port " + pPort + ". Quitting.");
			System.exit(-1);
		}

		Calendar now = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
		System.out.println("It is now : " + formatter.format(now.getTime()));

		// Successfully created Server Socket. Now wait for connections.
		while (ServerOn) {
			try {
				// Accept incoming connections.
				Socket clientSocket = myServerSocket.accept();

				// accept() will block until a client connects to the server.
				// If execution reaches this point, then it means that a client
				// socket has been accepted.

				// For each client, we will start a service thread to
				// service the client requests. This is to demonstrate a
				// Multi-Threaded server. Starting a thread also lets our
				// MultiThreadedSocketServer accept multiple connections
				// simultaneously.

				// Start a Service thread

				ClientServiceThread cliThread = new ClientServiceThread(clientSocket);
				cliThread.start();

			} catch (IOException ioe) {
				System.out.println("Exception encountered on accept. Ignoring. Stack Trace :");
				ioe.printStackTrace();
			}

		}

		try {
			myServerSocket.close();
			System.out.println("Server Stopped");
		} catch (Exception ioe) {
			System.out.println("Problem stopping server socket");
			System.exit(-1);
		}
	}

	class ClientServiceThread extends Thread {
		Socket myClientSocket;
		boolean f_RunThread = true;
		boolean f_waitForData = false;
		String completeMessage = "";
		String unexpectedMessage = "";
		char ETX = '\3';
		char STX = '\2';

		public ClientServiceThread() {
			super();
		}

		ClientServiceThread(Socket s) {
			myClientSocket = s;

		}

		public void run() {
			// Obtain the input stream and the output stream for the socket
			// A good practice is to encapsulate them with a BufferedReader
			// and a PrintWriter as shown below.
			BufferedReader in = null;
			PrintWriter out = null;

			// Print out details of this connection
			System.out.println("Accepted Client Address - " + myClientSocket.getInetAddress().getHostName());

			try {
				in = new BufferedReader(new InputStreamReader(myClientSocket.getInputStream()));
				out = new PrintWriter(new OutputStreamWriter(myClientSocket.getOutputStream()));

				// At this point, we can read for input and reply with
				// appropriate output.

				// Run in a loop until m_bRunThread is set to false
				while (f_RunThread) {
					// read incoming byte
					int i = in.read();
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
							tableCommnadParser.parseCommand(completeMessage);
						} else {
							// Process it
							if (f_waitForData) {
								completeMessage += readedChar;
							} else {
								unexpectedMessage += readedChar;
							}
						}
					} else {
						System.out.println("Client disconnected");
						f_RunThread = false;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// Clean up
				try {
					in.close();
					out.close();
					myClientSocket.close();
					System.out.println("...Stopped");
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}

	}
}
