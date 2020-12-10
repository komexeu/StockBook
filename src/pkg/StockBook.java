package pkg;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;
import java.util.Vector;

//cmd here:C:\Windows\System32\cmd.exe
public class StockBook {
    private static Connection conn;
    private static Statement stmt;

    public static void main(String[] args) {

        StockBookUI UI = new StockBookUI();
        System.out.println("Start");
        try {
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/stockbook_db?serverTimezone=UTC", "root", "password");
            stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM stock_db");
            ResultSetMetaData rsmd = rs.getMetaData();
            Vector<String> columnNames = new Vector<>();
            Vector<Vector<String>> data = new Vector<>();

            System.out.println(rsmd.getColumnCount());
            DefaultTableModel tableModel = new DefaultTableModel();
            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                columnNames.add(rsmd.getColumnName(i + 1));
                tableModel.addColumn(rsmd.getColumnName(i + 1));
            }

            while (rs.next()) {
                Vector<String> v = new Vector<>();
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    v.add(rs.getString(i + 1));
                }
                data.add(v);
                tableModel.addRow(v);
            }
            System.out.println(rsmd.getColumnCount());
            UI.table1.setModel(tableModel);
           // UI.table1=new JTable(data,columnNames);
//            JScrollPane scrollPane=new JScrollPane(UI.table1);
//            UI.frame.add(scrollPane);
//            UI.frame.validate();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
}
