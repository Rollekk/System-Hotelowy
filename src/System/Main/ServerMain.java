package System.Main;

import java.io.IOException;
import java.sql.SQLException;

public class ServerMain {

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        Serwer s = new Serwer();
        s.Server();
    }

}
