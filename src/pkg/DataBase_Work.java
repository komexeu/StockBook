package pkg;

import javax.naming.Name;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;

public class DataBase_Work {
    private static Connection conn;
    private static Statement stmt;
    private static DefaultTableModel _tableModel;

    DataBase_Work() {
        ConnectDataBase();
    }

    void ConnectDataBase() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/stockbook_db?serverTimezone=UTC", "root", "password");
            stmt = conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    void Search(String order) {
        try {
            //"SELECT * FROM stock_db"
            String complax_order = "SELECT * FROM stock_db WHERE ID =" + order + ";";
            System.out.println(complax_order);
            ResultSet rs = stmt.executeQuery(complax_order);
            ResultSetMetaData rsmd = rs.getMetaData();
            Vector<String> columnNames = new Vector<>();
            Vector<Vector<String>> data = new Vector<>();

            _tableModel = new DefaultTableModel();
            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                columnNames.add(rsmd.getColumnName(i + 1));
                _tableModel.addColumn(rsmd.getColumnName(i + 1));
            }

            while (rs.next()) {
                Vector<String> v = new Vector<>();
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    v.add(rs.getString(i + 1));
                }
                data.add(v);
                _tableModel.addRow(v);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    void Add_Data(String ID, String NAME, String BUY, String SELL) {
        try {
            //"INSERT INTO stock_db(ID,NAME,BUY,SELL) VALUES();"
            String complax_order = "INSERT INTO stock_db(ID,NAME,BUY,SELL) VALUES(\""
                     + ID + "\",\"" + NAME + "\",\"" + BUY + "\",\"" + SELL + "\");";
            System.out.println(complax_order);
            stmt.executeUpdate(complax_order);

            Search(ID);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    DefaultTableModel GetTableModel() {
        return _tableModel;
    }
}
