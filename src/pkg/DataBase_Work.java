package pkg;

import com.mysql.cj.protocol.Resultset;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;

public class DataBase_Work {
    private static Connection conn;
    private static Statement stmt;
    private static DefaultTableModel _tableModel;

    private ResultSet rs;

    DataBase_Work() {
        SQL_Connect();
    }

    void SQL_Connect() {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/stockbook_db?serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8",
                    "root", "password");
            stmt = conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    //SQL結果
    ResultSet SQL_Order(String SQL_order) {
        try {
            ResultSet rs = stmt.executeQuery(SQL_order);

            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            return rs;
        }
    }

    String SQL_Select_Where(String order) {
        //"SELECT * FROM stock_db"
        String complax_order = "";
        if (order.length() == 0)
            complax_order = "SELECT * FROM stock_db;";
        else
            complax_order = "SELECT * FROM stock_db WHERE stock_ID =" + order + ";";
        return complax_order;
    }

    String SQL_Select_OrderBy(String order, String Fieldname, boolean up2down) {
        String complax_order = "";
        Fieldname = Fieldname.split(" ")[0];
        if (order.length() == 0)
            complax_order = "SELECT * FROM stock_db ORDER BY " + Fieldname;
        else
            complax_order = "SELECT * FROM stock_db WHERE stock_ID =" + order + " ORDER BY " + Fieldname;

        if (up2down)
            complax_order += " DESC;";
        else
            complax_order += " ASC;";

        System.out.println("HERE-> " + complax_order);
        return complax_order;
    }

    String SQL_Insert(int ID, String stock_ID, String NAME, String Price, String NumOfShares, int mode) {
        //"INSERT INTO stock_db(stock_ID,NAME,Price,SELL) VALUES();"
        String complax_order = "";
        switch (mode) {
            case 0:
                complax_order = "INSERT INTO stock_db(ID,stock_ID,NAME,BUY,SELL,NumberOfShares) VALUES("
                        + ID + ",\"" +
                        stock_ID + "\",\"" +
                        NAME + "\",\"" +
                        Price + "\",\"" +
                        "0" + "\",";

                break;
            case 1:
                complax_order = "INSERT INTO stock_db(ID,stock_ID,NAME,BUY,SELL,NumberOfShares) VALUES("
                        + ID + ",\"" +
                        stock_ID + "\",\"" +
                        NAME + "\",\"" +
                        "0" + "\",\"" +
                        Price + "\",";
                break;
        }
        complax_order += (NumOfShares + ");");
        return complax_order;
    }

    void Search(String order, String Fieldname, boolean up2down) {
        try {

            String complax_order = SQL_Select_OrderBy(order, Fieldname, up2down);
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

    //從stockbook_db抓資料寫入realized_db
    void Sell_Insert() {
        try {
            Connection conn_realized = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/realized_db?serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8",
                    "root", "password");
            Statement stmt_realized = conn_realized.createStatement();

            //判斷是否有足夠庫存
            //插入資料
            //
        } catch (Exception e) {

        }
    }

    void Add_Data(String stock_ID, String NAME, String Price, String NumOfShares, String Fieldname, int mode, boolean up2down) {
        try {
            String sql = "SELECT COUNT(*) FROM stock_db";
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int ID = 0;
            while (rs.next())
                ID =Integer.parseInt(rs.getString(1));
            System.out.println(ID);
            ID += 1;

            Fieldname = Fieldname.split(" ")[0];
            String complax_order = SQL_Insert(ID, stock_ID, NAME, Price, NumOfShares, mode);

            System.out.println(complax_order);
            stmt.executeUpdate(complax_order);
            Sell_Insert();

            Search(stock_ID, Fieldname, up2down);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    DefaultTableModel GetTableModel() {
        return _tableModel;
    }
}
