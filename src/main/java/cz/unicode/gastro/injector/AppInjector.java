package cz.unicode.gastro.injector;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import cz.unicode.gastro.configuration;
import cz.unicode.gastro.gastroManager.gastroManager;
import cz.unicode.gastro.tcpip.tcpipclient.tcpipclient;
import cz.unicode.gastro.tcpip.tcpipserver.tcpipserver;

public class AppInjector extends AbstractModule {

	@Override
	protected void configure() {
		// TODO Auto-generated method stub
		ConnectionSource con = null;
		try {
			con = new JdbcConnectionSource(configuration.getDbPath());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		bind(ConnectionSource.class).toInstance(con);

		Logger aLogger = LoggerFactory.getLogger("cz.jnec");
		bind(Logger.class).toInstance(aLogger);
		bind(gastroManager.class);
                
         

		tcpipserver aTcpipserver = new tcpipserver(configuration.getPort());
		bind(tcpipserver.class).toInstance(aTcpipserver);

		tcpipclient aTcpipclient = new tcpipclient(configuration.getMachineName(), configuration.getPort());
		bind(tcpipclient.class).toInstance(aTcpipclient);

	}

}
