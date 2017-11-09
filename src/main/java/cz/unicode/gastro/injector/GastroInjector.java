/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.unicode.gastro.injector;

import com.google.inject.AbstractModule;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import cz.unicode.gastro.configuration;
import cz.unicode.gastro.gastroManager.gastroManager;
import cz.unicode.gastro.tcpip.tcpipclient.tcpipclient;
import cz.unicode.gastro.tcpip.tcpipserver.tcpipserver;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jnec
 */
public class GastroInjector extends AbstractModule {

    @Override
    protected void configure() {
        //Logger aLogger = LoggerFactory.getLogger("cz.jnec");
       // bind(Logger.class).toInstance(aLogger);
        ConnectionSource con = null;
        try {
            con = new JdbcConnectionSource(configuration.getDbPath());
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        bind(ConnectionSource.class).toInstance(con);
        AnnotatedBindingBuilder<gastroManager> bind = bind(gastroManager.class);
        tcpipserver aTcpipserver = new tcpipserver(configuration.getPort());
        bind(tcpipserver.class).toInstance(aTcpipserver);

        tcpipclient aTcpipclient = new tcpipclient(configuration.getMachineName(), configuration.getPort());
        bind(tcpipclient.class).toInstance(aTcpipclient);

    }

}
