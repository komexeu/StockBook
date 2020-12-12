package pkg;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;

public class DataBase_Work {
    private static Connection conn;
    private static Statement stmt;
    private static  DefaultTableModel _tableModel = new DefaultTableModel();

    DataBase_Work(){
        ConnectDataBase();
    }

    void ConnectDataBase(){
        try{
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/stockbook_db?serverTimezone=UTC", "root", "password");
            stmt = conn.createStatement();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e);
        }
    }

    void DoSomething(String order) {
        try {
            //"SELECT * FROM stock_db"
            ResultSet rs = stmt.executeQuery(order);
            ResultSetMetaData rsmd = rs.getMetaData();
            Vector<String> columnNames = new Vector<>();
            Vector<Vector<String>> data = new Vector<>();

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

    DefaultTableModel GetTableModel(){
        return _tableModel;
    }
}
