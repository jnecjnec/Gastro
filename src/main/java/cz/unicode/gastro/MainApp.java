package cz.unicode.gastro;

import org.slf4j.Logger;

import com.google.inject.Guice;
import com.google.inject.Injector;

import cz.unicode.gastro.gastroManager.gastroManager;
import cz.unicode.gastro.injector.AppInjector;
import cz.unicode.gastro.model.table.tableimpl;
import cz.unicode.gastro.tcpip.tcpipclient.tcpipclient;
import cz.unicode.gastro.tcpip.tcpipserver.tcpipserver;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import static javafx.application.Application.launch;

/**
 * Gastro table manager
 *
 */
public class MainApp extends Application {

        static Injector injector;
        static tcpipserver server = null;
        static tcpipclient client = null;
        static Logger logger;
        static gastroManager gastromanager = null;

    
     
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
         injector = Guice.createInjector(new AppInjector());
       
        logger = injector.getInstance(Logger.class);
    
        logger.info("Start application");
        
         gastromanager = injector.getInstance(gastroManager.class);
          
        launch(args);

    
        if (configuration.isServer()) {
            server.serverStop();
        } else {
            client.clientStop();
        }

        logger.info("Finish application");
    }
    
    public void startService(boolean pServer){
        
       
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
        
    }

}
