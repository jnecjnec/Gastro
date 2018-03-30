package cz.unicode.gastro;

public class configuration {

    private static boolean FisServer = false;
    private static int FclientNumber = 1;
    

    public static boolean isServer() {
        return FisServer;
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
            aDbPath = "jdbc:sqlite://C:/install/GastroTableManagerC" + FclientNumber + ".db";
        }
        return aDbPath;
    }

    static void setIsServer(boolean pServer) {
        FisServer = pServer;
    }
    
    static void setClientNumber(int pClientNumber) {
        FclientNumber = pClientNumber;
    }
}
