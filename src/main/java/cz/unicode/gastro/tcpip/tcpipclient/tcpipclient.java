package cz.unicode.gastro.tcpip.tcpipclient;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.google.inject.Singleton;

import cz.unicode.gastro.tcpip.clientservicethread.clientservicethread;

@Singleton
public class tcpipclient extends clientservicethread {

	String machineName;
	int port;

	public tcpipclient(String pMachineName, int pPort) {
		machineName = pMachineName;
		port = pPort;
	}

	public void clientRun() {
		try {
			myClientSocket = new Socket(machineName, port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		start();
	}

}
