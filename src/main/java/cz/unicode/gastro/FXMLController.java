package cz.unicode.gastro;

import static cz.unicode.gastro.MainApp.injector;
import cz.unicode.gastro.gastroManager.gastroManager;
import cz.unicode.gastro.tcpip.tcpipclient.tcpipclient;
import cz.unicode.gastro.tcpip.tcpipserver.tcpipserver;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.stage.WindowEvent;

public class FXMLController implements Initializable {

    tcpipserver server = null;

    tcpipclient client = null;

    gastroManager gastromanager = null;

    @FXML
    private Label label;

    @FXML
    private RadioButton radioButtonServer;

    @FXML
    private RadioButton radioButtonClient;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        startService(radioButtonServer.selectedProperty().getValue());
    }

    @FXML
    private void handleCheckBoxServer(ActionEvent event) {
        radioButtonServer.selectedProperty().setValue(Boolean.TRUE);
        radioButtonClient.selectedProperty().setValue(Boolean.FALSE);
    }

    @FXML
    private void handleCheckBoxClient(ActionEvent event) {
        radioButtonServer.selectedProperty().setValue(Boolean.FALSE);
        radioButtonClient.selectedProperty().setValue(Boolean.TRUE);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        gastromanager = injector.getInstance(gastroManager.class);
    }

  
 

    private void startService(boolean pServer) {
        if (pServer) {
            label.setText("Run as server");
            server = injector.getInstance(tcpipserver.class);
            server.setListener(gastromanager);
            server.serverRun();

        } else {
            label.setText("Run as client");
            client = injector.getInstance(tcpipclient.class);
            client.setListener(gastromanager);
            client.clientRun();

        }

        /*
        if (!configuration.isServer()) {
            tableimpl ta = new tableimpl();
            ta.setUserId(123);
            gastromanager.addTable(ta);
        }
         */
    }
    
    public void shutdown () {
       if (server != null){
           server.serverStop();
       } 
       if (client != null){
           client.clientStop();
       }
    }
}
