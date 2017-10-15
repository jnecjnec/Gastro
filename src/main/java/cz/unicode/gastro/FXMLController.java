package cz.unicode.gastro;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;

public class FXMLController implements Initializable {
    
    @FXML
    private Label label;
    
    @FXML
    private RadioButton radioButtonServer;
    
    @FXML
    private RadioButton radioButtonClient;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        startService(true);
        label.setText("Hello World!");
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
    }    
}
