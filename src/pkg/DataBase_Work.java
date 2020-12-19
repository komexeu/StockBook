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
        SQL_Connect connect = new SQL_Connect(
                "jdbc:mysql://127.0.0.1:3306/stockbook_db?serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8"
        );
        conn = connect.conn;
        stmt = connect.stmt;
    }

    //SQL結果
    ResultSet SQL_query(String SQL_order) {
        try {
            System.out.println("query->"+ SQL_order);
            ResultSet rs = stmt.executeQuery(SQL_order);

            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            return rs;
        }
    }

    String SQL_Select_Where(String table, String order) {
        //"SELECT * FROM stock_db"
        String complax_order = "";
        if (order.length() == 0)
            complax_order = "SELECT * FROM " + table + ";";
        else
            complax_order = "SELECT * FROM " + table + " WHERE stock_ID =" + order + ";";
        return complax_order;
    }

    String SQL_Select_OrderBy(String stock_id, String Fieldname, boolean up2down) {
        String complax_order = "";
        Fieldname = Fieldname.split(" ")[0];
        if (stock_id.length() == 0)
            complax_order = "SELECT * FROM stock_db ORDER BY " + Fieldname;
        else
            complax_order = "SELECT * FROM stock_db WHERE stock_ID =" + stock_id + " ORDER BY " + Fieldname;

        if (up2down)
            complax_order += " DESC;";
        else
            complax_order += " ASC;";

        return complax_order;
    }

    String SQL_Select_OrderBy(String table, String stock_id, String Fieldname, boolean up2down) {
        String complax_order = "";
        Fieldname = Fieldname.split(" ")[0];
        if (stock_id.length() == 0)
            complax_order = "SELECT * FROM " + table + " ORDER BY " + Fieldname;
        else
            complax_order = "SELECT * FROM " + table + " WHERE stock_ID =" + stock_id + " ORDER BY " + Fieldname;

        if (up2down)
            complax_order += " DESC;";
        else
            complax_order += " ASC;";

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

    void Search(String stock_id, String Fieldname, boolean up2down) {
        try {
            String complax_order = SQL_Select_OrderBy(stock_id, Fieldname, up2down);
            System.out.println("Search->" + complax_order);
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

    void Search(String Table, String stock_id, String Fieldname, boolean up2down) {
        try {
            String complax_order = SQL_Select_OrderBy(Table, stock_id, Fieldname, up2down);
            System.out.println("Search->" + complax_order);
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

    //新增資料時將買入資料填入持有紀錄
    void Buy_Insert(int ID, String stock_ID, String NAME, String Price, String NumOfShares) {
        String sql = "INSERT INTO hold_db(ID,stock_ID,NAME,BUY,NumberOfShares) VALUES("
                + ID + ",\"" +
                stock_ID + "\",\"" +
                NAME + "\",\"" +
                Price + "\",";
        sql += (NumOfShares + ");");
        System.out.println("BUY_Insert->" + sql);
        try {
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    //新增資料時將賣出資料填入賣出紀錄
    //從stockbook_db抓資料寫入realized_db
    boolean Sell_Insert(int ID, String stock_ID, String NAME, String Price, String NumOfShares, String Fieldname) {
        try {
            //判斷是否有足夠庫存
            //插入資料
            int hold_num = 0;
            Vector<Integer> id = new Vector<>();

            String sql = SQL_Select_OrderBy("hold_db", stock_ID, "NumberOfShares", true);
            System.out.println("SELL->" + sql);
            stmt.executeQuery(sql);
            ResultSet rs = stmt.getResultSet();
            //從已有資料中找到可扣除資料
            hold_num = 0;
            while (rs.next()) {
                hold_num += Integer.parseInt(rs.getString(5));
                System.out.println(hold_num);
                id.add(Integer.parseInt(rs.getString(1)));
                if (hold_num - Integer.parseInt(NumOfShares) >= 0) {
                    //新增賣出資料
                    String str_ID = ("B" + String.valueOf(ID) + "S" + rs.getString(1));
                    sql = "INSERT INTO realized_db(ID,stock_ID,NAME,BUY,SELL,TAX,TRANSACTION_NUM,PROFIT_LOSS) VALUES(\"" +
                            str_ID + "\",\"" +
                            stock_ID + "\",\"" +
                            NAME + "\"," +
                            Float.parseFloat(rs.getString(4)) + "," +
                            Price + "," +
                            0.123 + "," +
                            NumOfShares + ",\"" +
                            String.valueOf(Float.parseFloat(Price) - Float.parseFloat(rs.getString(4))) + "\");";
                    System.out.println(sql);
                    stmt.executeUpdate(sql);
                    //刪除擁有資料
                    for (int i = 0; i < id.size(); ++i) {
                        sql = "DELETE FROM hold_db WHERE ID =" + id.elementAt(i) + ";";
                        stmt.executeUpdate(sql);
                        System.out.println(id.elementAt(i));
                    }
                    return true;
                }
            }
            System.out.println("庫存不足");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            return false;
        }
    }

    void Add_Data(String stock_ID, String NAME, String Price, String NumOfShares, String Fieldname, int mode, boolean up2down) {
        try {
            String sql = "SELECT COUNT(*) FROM stock_db";
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int ID = 0;
            while (rs.next())
                ID = Integer.parseInt(rs.getString(1));
            ID += 1;

            boolean sell_success = true;
            if (mode == 0)
                Buy_Insert(ID, stock_ID, NAME, Price, NumOfShares);
            else {
                sell_success = Sell_Insert(ID, stock_ID, NAME, Price, NumOfShares, Fieldname);
            }

            Fieldname = Fieldname.split(" ")[0];
            if (sell_success) {
                String complax_order = SQL_Insert(ID, stock_ID, NAME, Price, NumOfShares, mode);
                System.out.println("ADD_Data->" + complax_order);
                stmt.executeUpdate(complax_order);
            } else {
                System.out.println("=========FAIL============");
            }
            Search(stock_ID, Fieldname, up2down);
            System.out.println("=====================");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    DefaultTableModel GetTableModel() {
        return _tableModel;
    }
}
