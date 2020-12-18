package pkg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SQL_Connect {
    public Connection conn;
   public Statement stmt;

    SQL_Connect(String url) {
        try {
            conn = DriverManager.getConnection(url, "root", "password");
            stmt = conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
}
