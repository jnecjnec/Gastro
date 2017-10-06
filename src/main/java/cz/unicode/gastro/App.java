package cz.unicode.gastro;

import java.io.IOException;

import org.slf4j.Logger;

import com.google.inject.Guice;
import com.google.inject.Injector;

import cz.unicode.gastro.gastroManager.gastroManager;
import cz.unicode.gastro.injector.AppInjector;
import cz.unicode.gastro.model.table.tableimpl;
import cz.unicode.gastro.tcpip.tcpipclient.tcpipclient;
import cz.unicode.gastro.tcpip.tcpipserver.tcpipserver;

/**
 * Gastro table manager
 *
 */
public class App {

	public static void main(String[] args) {

		Injector injector = Guice.createInjector(new AppInjector());
		Logger logger = injector.getInstance(Logger.class);
		logger.info("Start application");
        
		gastroManager gastromanager = injector.getInstance(gastroManager.class);
		
		tcpipserver server = null;
		tcpipclient client = null;
		if (configuration.isServer()) {
			server = injector.getInstance(tcpipserver.class);
			server.setListener(gastromanager);
			server.serverRun();
		} else {
			client = injector.getInstance(tcpipclient.class);
			client.setListener(gastromanager);
			client.clientRun();
		}

		if (!configuration.isServer()) {
			tableimpl ta = new tableimpl();
			ta.setUserId(123);
			gastromanager.addTable(ta);
		}

		//if (configuration.isServer()) {
		   try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		//}

		if (configuration.isServer()) {
			server.serverStop();
		} else {
			client.clientStop();
		}

		logger.info("Finish application");
	}
}
