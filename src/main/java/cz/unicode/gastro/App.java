package cz.unicode.gastro;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import cz.unicode.gastro.model.table.table;
import cz.unicode.gastro.model.table.tableMessage;
import cz.unicode.gastro.model.table.tableMessage.commandType;
import cz.unicode.gastro.model.table.tableimpl;
import cz.unicode.gastro.tcpip.message.message;
import cz.unicode.gastro.tcpip.message.message.messageType;
import cz.unicode.gastro.tcpip.message.messageDecoderEncoder;
import cz.unicode.gastro.tcpip.tcpipserver.tcpipserver;

/**
 * Gastro table manager
 *
 */
public class App {

	public static void main(String[] args) {

		message aMessage = new message();
		tableimpl ta = new tableimpl();
		ta.setUserId(100);

		tableMessage tm = new tableMessage();
		tm.set_table(ta);
		tm.set_commandType(commandType.ctAdd);

		aMessage.setMsgType(messageType.mtTable);
		aMessage.setObject(tm);

		// encode to xml
		String aCommand = messageDecoderEncoder.encodemessageToCommand(aMessage);

		System.out.println(aCommand);

		// decode from xml
		message bMessage = messageDecoderEncoder.decodemessageToCommand(aCommand);

		// pokud je to zprava pro stoly
		if (bMessage.getMsgType() == messageType.mtTable) {
			tableMessage tm1 = (tableMessage) bMessage.getObject();

			// je to pridani stolu
			if (tm1.get_commandType() == commandType.ctAdd) {
				table ta1 = tm1.get_table();
				// add table into list
				System.out.println(ta1);
			}
		} else {
			System.out.println("Unknown command");
		}

		
		
		// this uses h2 by default but change to match your database
		String databaseUrl = "jdbc:sqlite://C:/install/GastroTableManager.db";
		// create a connection source to our database
		ConnectionSource connectionSource = null;
		try {
			connectionSource = new JdbcConnectionSource(databaseUrl);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// instantiate the dao
		Dao<tableimpl, String> tableDao = null;
		try {
			tableDao = DaoManager.createDao(connectionSource, tableimpl.class);
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
		// Once we have configured our database objects, we can use them to
		// persist an object to the database and query for it from the database
		// by its ID:

		// create an instance of Account
		 tableimpl Table = new tableimpl();
		Table.setNumber(102);

		// persist the account object to the database
		try {
			tableDao.create(Table);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// retrieve the account from the database by its id field (name)
		List<tableimpl> Table2 = null;
		try {
			Table2 = tableDao.queryForAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Number: " + Table2.get(0).getNumber());

		// close the connection source
		try {
			connectionSource.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		new tcpipserver(11111);

	}
}
