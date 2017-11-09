package cz.unicode.gastro;

import com.google.inject.Guice;
import com.google.inject.Injector;
import cz.unicode.gastro.injector.AppInjector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import static javafx.application.Application.launch;
import org.slf4j.Logger;

/**
 * Gastro table manager
 *
 */
public class MainApp extends Application {

    static Injector appinjector;
    static Logger logger;

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
        appinjector = Guice.createInjector(new AppInjector());
        logger = appinjector.getInstance(Logger.class);
        logger.info("Start application");

        launch(args);

        logger.info("Finish application");

    }

}
