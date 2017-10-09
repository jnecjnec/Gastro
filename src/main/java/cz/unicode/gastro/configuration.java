package cz.unicode.gastro;

public class configuration {

	public static boolean isServer() {
		return true;
	}

	public static int getPort() {
		return 11111;
	}

	public static String getMachineName() {
		return "localhost";
	}

	public static String getDbPath() {
		String aDbPath;
		if (isServer()) {
			aDbPath = "jdbc:sqlite://C:/install/GastroTableManagerS.db";
		} else {
			aDbPath = "jdbc:sqlite://C:/install/GastroTableManagerC.db";
		}
		return aDbPath;
	}
}
