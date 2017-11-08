package cz.unicode.gastro;

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

    //static Injector injector;
   // static Logger logger;
  
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Scene.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        FXMLController controller = loader.getController();

        stage.setScene(scene);
      
        stage.setOnHidden(e -> {
            controller.shutdown();
        });
        stage.show();
    }

    public static void main(String[] args) {
       // injector = Guice.createInjector(new AppInjector());

       // logger = injector.getInstance(Logger.class);

        //logger.info("Start application");

        launch(args);
/*
        if (configuration.isServer()) {
            server.serverStop();
        } else {
            client.clientStop();
        }
*/
        //logger.info("Finish application");
        
    }


}
