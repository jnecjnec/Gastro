package cz.unicode.gastro.gastroManager;

import java.io.IOException;
import java.sql.SQLException;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import cz.unicode.gastro.configuration;
import cz.unicode.gastro.gastroManager.gastroMessage.gastroMessageType;
import cz.unicode.gastro.model.table.table;
import cz.unicode.gastro.model.table.tableMessage;
import cz.unicode.gastro.model.table.tableMessage.commandType;
import cz.unicode.gastro.model.table.tableimpl;
import cz.unicode.gastro.tcpip.message.message;
import cz.unicode.gastro.tcpip.message.message.messageType;
import cz.unicode.gastro.tcpip.message.messageDecoderEncoder;
import cz.unicode.gastro.tcpip.tcpipclient.tcpipclient;
import cz.unicode.gastro.tcpip.tcpipserver.tcpipserver;

@Singleton
public class gastroManager implements IrecievedMessageListener {

    @Inject
    private Logger logger;

    @Inject
    tcpipserver ipserver;

    @Inject
    tcpipclient client;

    private Dao<tableimpl, String> daoTables = null;

    @Inject
    public gastroManager(ConnectionSource connectionSource) {

        // instantiate the dao
        try {
            daoTables = DaoManager.createDao(connectionSource, tableimpl.class);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // if you need to create the 'accounts' table make this call
        try {
            TableUtils.createTableIfNotExists(connectionSource, tableimpl.class);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // close the connection source
        try {
            connectionSource.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public gastroManager() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean storeTable(table pTable) {
        boolean result = false;

        // persist the account object to the database
        try {
            daoTables.create((tableimpl) pTable);
            result = true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    private boolean sendGastroMessage(Object pMsg, gastroMessageType pGastroMessageType) {
        gastroMessage gMsg = new gastroMessage();
        gMsg.setMsgType(pGastroMessageType);
        gMsg.setObject(pMsg);

        message aMsg = new message();
        aMsg.setMsgType(messageType.mtGastro);
        aMsg.setObject(gMsg);

        if (configuration.isServer()) { // pokud je server musi poslat vsem
            // klientum		
            ipserver.sendMessageToAllClients(messageDecoderEncoder.encodemessageToCommand(aMsg));
        } else {// pokud je client posle jenom servrovi
            client.setOutMessage(messageDecoderEncoder.encodemessageToCommand(aMsg));
        }
        return true;
    }

    private boolean sendTableMessage(table pTable, commandType pCommandType) {
        tableMessage tMsg = new tableMessage();
        tMsg.set_commandType(pCommandType);
        tMsg.set_table(pTable);

        return sendGastroMessage(tMsg, gastroMessageType.gmtTable);
    }

    private boolean parseTableMessage(tableMessage pMessage) {
        boolean result = false;
        switch (pMessage.get_commandType()) {
            case ctAdd:
                if (configuration.isServer()) {// pokud je server ulozi
                    result = addTable(pMessage.get_table());
                } else {
                    result = storeTable(pMessage.get_table());
                }
                break;
            default:
                System.out.println("Unknown command");
                break;
        }

        return result;
    }

    public boolean parseGastroMessage(gastroMessage pMessage) {
        boolean result = false;
        switch (pMessage.getMsgType()) {
            case gmtTable:
                result = parseTableMessage((tableMessage) pMessage.getObject());
                break;

            default:
                System.out.println("Unknown command");
                break;
        }

        return result;
    }

    public boolean addTable(table pTable) {
        boolean result = true;
        if (configuration.isServer()) {// pokud je server ulozi
            result = storeTable(pTable);
        }
        if (result) {
            result = sendTableMessage(pTable, commandType.ctAdd);
        }
        return result;
    }

    public void Message(String pMessage) {
        // TODO Auto-generated catch block

        message msg;

        msg = messageDecoderEncoder.decodemessageToCommand(pMessage);

        if (msg.getMsgType() == messageType.mtGastro) {
            parseGastroMessage((gastroMessage) msg.getObject());
        }

    }

}
