package cz.unicode.gastro;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;


import cz.unicode.gastro.Injectors.AppInjector;
import cz.unicode.gastro.Injectors.GastroInjector;
import cz.unicode.gastro.gastroManager.gastroManager;
import cz.unicode.gastro.model.table.tableimpl;
import cz.unicode.gastro.tcpip.tcpipclient.tcpipclient;
import cz.unicode.gastro.tcpip.tcpipserver.tcpipserver;
import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;


public class FXMLController implements Initializable {

    static Injector injector = null;
    tcpipserver server = null;
    tcpipclient client = null;
    gastroManager gastromanager = null;
    Logger logger = null;

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
        // gastromanager = injector.getInstance(gastroManager.class);
        label.setText("Initialize");
        injector = Guice.createInjector(new AppInjector());
        logger = injector.getInstance(Logger.class);
        logger.info("Start application");

    }

    private void startService(boolean pServer) {
        configuration.setIsServer(pServer);

        injector = Guice.createInjector(Modules.override(new AppInjector()).with(new GastroInjector()));

        logger = injector.getInstance(Logger.class);
        gastromanager = injector.getInstance(gastroManager.class);

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

        if (configuration.isServer()) {
            tableimpl ta = new tableimpl();
            ta.setUserId(123);
            gastromanager.addTable(ta);
        }

    }

    public void shutdown() {

        if (server != null) {
            server.serverStop();
        }
        if (client != null) {
            client.clientStop();
        }
        logger.info("Close application");
    }
}
