package pkg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

public class SQL_Connect {
    public Connection _conn;
    public Statement _stmt;
    ResultSet _rs;

    public SQL_Connect() {
        try {
            _conn = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/stockbook_db?serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8"
                    , "root", "password");
            _stmt = _conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    public SQL_Connect(String url) {
        try {
            _conn = DriverManager.getConnection(url, "root", "password");
            _stmt = _conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    //-------------------GET DATA-------------------------
    public ResultSet GET_StockID(String stock_name) {
        String query = "SELECT ID FROM stockid_information WHERE NAME=\"" + stock_name + "\";";
        ResultSet rs = SQL_excuteQuery(query);
        return rs;
    }

    public ResultSet GET_StockName(String stock_id) {
        String query = "SELECT NAME FROM stockid_information WHERE ID=\"" + stock_id + "\";";
        ResultSet rs = SQL_excuteQuery(query);
        return rs;
    }

    public ResultSet GET_BUY(String stock_id) {
        String query = "SELECT BUY FROM stock_db WHERE STOCK_ID=" + stock_id + ";";
        ResultSet rs = SQL_excuteQuery(query);
        return rs;
    }

    public ResultSet GET_SELL(String ID) {
        String query = "SELECT * FROM stock_db WHERE ID=" + ID + ";";
        ResultSet rs = SQL_excuteQuery(query);
        return rs;
    }

    public ResultSet GET_PROFIT_LOSS(String ProfitLossID) {
        String query = "SELECT * FROM realized_db WHERE ID=" + ProfitLossID + ";";
        ResultSet rs = SQL_excuteQuery(query);
        return rs;
    }

    //----------------
    public ResultSet SQL_excuteQuery(String SQL_order) {
        try {
            System.out.println("query->" + SQL_order);
            _rs = _stmt.executeQuery(SQL_order);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return _rs;
    }

    public void SQL_excuteUpdate(String SQL_order) {
        try {
            System.out.println("update->" + SQL_order);
            _stmt.executeUpdate(SQL_order);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    //------------------------
    //SELECT * FROM table ;
    public String SQL_Select(String table) {
        String query = "SELECT * FROM " + table + ";";
        return query;
    }

    //SELECT * FROM table WHERE stock_ID = stock_id ;
    public String SQL_Select_sotckID(String table, String stock_id) {
        String query = "SELECT * FROM " + table + " WHERE stock_ID =" + stock_id + ";";
        return query;
    }

    //SELECT * FROM table WHERE ID = id ;
    public String SQL_Select_ID(String table, String id) {
        String query = "SELECT * FROM " + table + " WHERE ID =" + id + ";";
        return query;
    }

    //選擇特定欄位資料
    //SELECT FieldName FROM table WHERE WhereCondition ;
    public String SQL_Select_Check_sotckID(Vector<String> FieldName, String table, String WhereCondition) {
        String query = "";
        String ComplaxField = "";
        for (int i = 0; i < FieldName.size(); ++i) {
            ComplaxField += FieldName.elementAt(i);
            if (i != FieldName.size() - 1)
                ComplaxField += ",";
        }

        if (WhereCondition.equals(""))
            query = "SELECT " + ComplaxField + " FROM " + table + ";";
        else
            query = "SELECT " + ComplaxField + " FROM " + table + " WHERE " + WhereCondition + ";";
        return query;
    }
}
