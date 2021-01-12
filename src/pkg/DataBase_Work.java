package pkg;

import sample.MapData;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;

public class DataBase_Work {
    private static Connection conn;
    private static Statement stmt;
    private static DefaultTableModel _tableModel;
    private ResultSet rs;
    SQL_Connect connect;

    public DataBase_Work() {
        connect = new SQL_Connect(
                "jdbc:mysql://127.0.0.1:3306/stockbook_db?serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8"
        );
        conn = connect._conn;
        stmt = connect._stmt;
    }

    //SQL結果
    public ResultSet SQL_query(String SQL_order) {
        try {
            System.out.println("query->" + SQL_order);
            ResultSet rs = stmt.executeQuery(SQL_order);

            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            return rs;
        }
    }

    public String SQL_Select_Where(String table, String order) {
        //"SELECT * FROM stock_db"
        String complax_order = "";
        if (order.length() == 0)
            complax_order = "SELECT * FROM " + table + ";";
        else
            complax_order = "SELECT * FROM " + table + " WHERE STOCK_ID =" + order + ";";
        return complax_order;
    }

    public String SQL_Select_OrderBy(String stock_id, String sort_rule, boolean up2down) {
        String complax_order = "";
        sort_rule = sort_rule.split(" ")[0];
        if (stock_id.length() == 0)
            complax_order = "SELECT * FROM stock_db ORDER BY " + sort_rule;
        else
            complax_order = "SELECT * FROM stock_db WHERE STOCK_ID =" + stock_id + " ORDER BY " + sort_rule;

        if (up2down)
            complax_order += " DESC;";
        else
            complax_order += " ASC;";

        return complax_order;
    }

    public String SQL_Select_OrderBy(String table, String stock_id, String sort_rule, boolean up2down) {
        String complax_order = "";
        sort_rule = sort_rule.split(" ")[0];
        if (stock_id.length() == 0)
            complax_order = "SELECT * FROM " + table + " ORDER BY " + sort_rule;
        else
            complax_order = "SELECT * FROM " + table + " WHERE STOCK_ID =" + stock_id + " ORDER BY " + sort_rule;

        if (up2down)
            complax_order += " DESC;";
        else
            complax_order += " ASC;";

        return complax_order;
    }

    public String SQL_Insert(int ID, String STOCK_ID, String NAME, String Price, String NumOfShares, int mode) {
        //"INSERT INTO stock_db(STOCK_ID,NAME,Price,SELL) VALUES();"
        String complax_order = "";
        switch (mode) {
            case 0:
                complax_order = "INSERT INTO stock_db(ID,STOCK_ID,NAME,BUY,SELL,NUMBER_OF_SHARES) VALUES("
                        + ID + ",\"" +
                        STOCK_ID + "\",\"" +
                        NAME + "\",\"" +
                        Price + "\",\"" +
                        "0" + "\",";

                break;
            case 1:
                complax_order = "INSERT INTO stock_db(ID,STOCK_ID,NAME,BUY,SELL,NUMBER_OF_SHARES) VALUES("
                        + ID + ",\"" +
                        STOCK_ID + "\",\"" +
                        NAME + "\",\"" +
                        "0" + "\",\"" +
                        Price + "\",";
                break;
        }
        complax_order += (NumOfShares + ");");
        return complax_order;
    }

    public void Search(String stock_id, String sort_rule, boolean up2down) {
        try {
            String complax_order = SQL_Select_OrderBy(stock_id, sort_rule, up2down);
            System.out.println("Search->" + complax_order);
            ResultSet rs = stmt.executeQuery(complax_order);

            ResultSetMetaData rsmd = rs.getMetaData();
            Vector<String> columnNames = new Vector<>();
            Vector<Vector<String>> data = new Vector<>();

            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                String tmp_name = MapData.map_tableTitle.get(rsmd.getColumnName(i + 1));
                System.out.println("Name->" + tmp_name);
                columnNames.add(tmp_name);
                _tableModel.addColumn(tmp_name);
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

    public void Search(String Table, String stock_id, String sort_rule, boolean up2down) {
        try {
            String complax_order = SQL_Select_OrderBy(Table, stock_id, sort_rule, up2down);
            System.out.println("Search->" + complax_order);
            ResultSet rs = stmt.executeQuery(complax_order);

            ResultSetMetaData rsmd = rs.getMetaData();
            Vector<String> columnNames = new Vector<>();
            Vector<Vector<String>> data = new Vector<>();

            _tableModel = new DefaultTableModel();
            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                String tmp_name = MapData.map_tableTitle.get(rsmd.getColumnName(i + 1));
                columnNames.add(tmp_name);
                _tableModel.addColumn(tmp_name);
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

    //測試v
    public DefaultTableModel Search_USERS_HOLD(String stock_id) {
        //資料庫提取資料，組合新TABLE
        try {
            //標題
            _tableModel = new DefaultTableModel();
            _tableModel.addColumn("名稱");
            _tableModel.addColumn("買入");

            ResultSet rs2 = connect.GET_StockName(stock_id);
            ResultSetMetaData rsmd1 = rs2.getMetaData();
            String stock_name = "";
            while (rs2.next())
                stock_name = rs2.getString(1);

            ResultSet rs1 = connect.GET_BUY(stock_id);
            ResultSetMetaData rsmd = rs1.getMetaData();
            while (rs1.next()) {
                Vector<String> v = new Vector<>();
                v.add(rs1.getString(1));
                v.add(stock_name);
                _tableModel.addRow(v);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return _tableModel;
    }

    //新增資料時將買入資料填入持有紀錄
    public void Buy_Insert(int ID, String STOCK_ID, String NAME, String Price, String NumOfShares) {
        String sql = "INSERT INTO hold_db(ID,STOCK_ID,NAME,BUY,NUMBER_OF_SHARES) VALUES("
                + ID + ",\"" +
                STOCK_ID + "\",\"" +
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
    //約略值，僅供參考
    public boolean Sell_Check(int ID, String STOCK_ID, String NAME, String price, String NumOfShares, String Fieldname) {
        try {
            //判斷是否有足夠庫存
            //插入資料
            int hold_num = 0;
            Vector<Integer> id = new Vector<>();

            String sql = SQL_Select_OrderBy("hold_db", STOCK_ID, "NUMBER_OF_SHARES", true);
            System.out.println("SELL->" + sql);
            stmt.executeQuery(sql);
            ResultSet rs = stmt.getResultSet();
            //從已有資料中找到可扣除資料
            hold_num = 0;
            while (rs.next()) {
                hold_num += Integer.parseInt(rs.getString(5));
                if (hold_num - Integer.parseInt(NumOfShares) >= 0) {
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

    public boolean Sell_Insert(int ID, String STOCK_ID, String NAME, String price, String NumOfShares, String Fieldname) {
        try {
            //判斷是否有足夠庫存
            //插入資料
            int hold_num = 0;
            Vector<Integer> id = new Vector<>();

            String sql = SQL_Select_OrderBy("hold_db", STOCK_ID, "NUMBER_OF_SHARES", true);
            System.out.println("SELL->" + sql);
            stmt.executeQuery(sql);
            ResultSet rs = stmt.getResultSet();
            //從已有資料中找到可扣除資料
            hold_num = 0;
            while (rs.next()) {
                hold_num += Integer.parseInt(rs.getString(5));
                id.add(Integer.parseInt(rs.getString(1)));
                if (hold_num - Integer.parseInt(NumOfShares) >= 0) {
                    //新增賣出資料
                    int remark = Integer.parseInt(rs.getString(1));
                    float buy_price = Float.parseFloat(rs.getString(4));
                    float sell_price = Float.parseFloat(price);
                    int num_of_shares = Integer.parseInt(NumOfShares);
                    float tax_price = (buy_price * 0.001425f + sell_price * 0.001425f + sell_price * 0.003f) * num_of_shares;
                    float profit_loss = sell_price * num_of_shares - buy_price * num_of_shares - tax_price;
                    float percent_profit_loss = (profit_loss / (buy_price * num_of_shares + tax_price)) * 100;

                    sql = "INSERT INTO realized_db(ID,REMARKS,STOCK_ID,NAME,BUY,SELL," +
                            "TAX,NUMBER_OF_SHARES,PROFIT_LOSS,PERCENT_PROFIT_LOSS) VALUES(" +
                            ID + "," +
                            remark + ",\"" +
                            STOCK_ID + "\",\"" +
                            NAME + "\"," +
                            buy_price + "," +
                            price + "," +
                            tax_price+ "," +
                            NumOfShares + ",\"" +
                            String.valueOf(profit_loss) + "\",\"" +
                            String.valueOf(percent_profit_loss) + "%" + "\");";
                    System.out.println(sql);
                    stmt.executeUpdate(sql);
                    //刪除擁有資料
                    for (int i = 0; i < id.size(); ++i) {
                        sql = "DELETE FROM hold_db WHERE ID =" + id.elementAt(i) + ";";
                        stmt.executeUpdate(sql);
                    }

                    //對stock_db做"買"資料備註
                    sql = "UPDATE stock_db SET REMARKS=" + ID + " WHERE ID=" + remark + ";";
                    System.out.println(sql);
                    stmt.executeUpdate(sql);

                    //對stock_db做"賣"資料備註
                    sql = "UPDATE stock_db SET REMARKS=" + remark + " WHERE ID=" + ID + ";";
                    System.out.println(sql);
                    stmt.executeUpdate(sql);
                    sql = "UPDATE realized_db SET REMARKS=" + remark + " WHERE ID=" + ID + ";";
                    System.out.println(sql);
                    stmt.executeUpdate(sql);

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

    public boolean Add_Data(String STOCK_ID, String NAME, String Price, String NumOfShares, String Fieldname, int mode, boolean up2down) {
        try {
            String sql = "SELECT COUNT(*) FROM stock_db";
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int ID = 0;
            while (rs.next())
                ID = Integer.parseInt(rs.getString(1));
            ID += 1;

            boolean sell_success = false;
            if (mode == 0) {
                String complax_order = SQL_Insert(ID, STOCK_ID, NAME, Price, NumOfShares, mode);
                System.out.println("ADD_Data->" + complax_order);
                stmt.executeUpdate(complax_order);
                Buy_Insert(ID, STOCK_ID, NAME, Price, NumOfShares);
                return true;
            } else {
                if (!Sell_Check(ID, STOCK_ID, NAME, Price, NumOfShares, Fieldname))
                    return false;
                String complax_order = SQL_Insert(ID, STOCK_ID, NAME, Price, NumOfShares, mode);
                System.out.println("ADD_Data->" + complax_order);
                stmt.executeUpdate(complax_order);
                Sell_Insert(ID, STOCK_ID, NAME, Price, NumOfShares, Fieldname);
                //Fieldname = Fieldname.split(" ")[0];
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return false;
    }

    public boolean editData(String Table, String ID, String ColumnName, String Changed_result) {
        try {
            //交易紀錄的更新
            String sql = "UPDATE stock_db SET " + ColumnName + " = " + Changed_result + " WHERE " + Table + ".ID=" + ID + ";";
            stmt.executeUpdate(sql);

            //持有股票的更新
            // UPDATE hold_db
            //SET hold_db.NUMBER_OF_SHARES = (SELECT NUMBER_OF_SHARES FROM stock_db WHERE ID="*ID")
            //WHERE ID ="*ID";
            sql = "UPDATE hold_db SET hold_db." + ColumnName +
                    " = (SELECT " + ColumnName + " FROM stock_db WHERE ID=" + ID + ")" +
                    " Where ID=" + ID + ";";
            stmt.executeUpdate(sql);
            //連動更新買或賣的交易股數資料
            //判定買或賣資訊
            int SellID = 0;
            if (ColumnName.equals("NUMBER_OF_SHARES")) {
                sql = "SELECT REMARKS,BUY FROM stock_db WHERE ID =" + ID + ";";
                ResultSet rs = stmt.executeQuery(sql);
                rs.next();
                SellID = (rs.getString(1).equals("0.00")) ?
                        Integer.parseInt(ID) :
                        Integer.parseInt(rs.getString(1));

                String remark_id = rs.getString(1);
                if (!remark_id.equals("")) {
                    sql = "UPDATE stock_db SET " + ColumnName + " = " + Changed_result + " WHERE " + Table + ".ID=" + remark_id + ";";
                    stmt.executeUpdate(sql);

                    sql = "UPDATE hold_db SET hold_db." + ColumnName +
                            " = (SELECT " + ColumnName + " FROM stock_db WHERE ID=" + ID + ")" +
                            " Where ID=" + remark_id + ";";
                    stmt.executeUpdate(sql);
                }
            }

            //獲利結算的更新
            //UPDATE realized_db
            //SET realized_db.NUMBER_OF_SHARES = (SELECT NUMBER_OF_SHARES FROM stock_db WHERE ID="*ID");
            sql = "UPDATE realized_db SET realized_db." + ColumnName +
                    " = (SELECT " + ColumnName + " FROM stock_db WHERE ID=" + ID + ")" +
                    "Where ID=" + SellID + ";";
            stmt.executeUpdate(sql);

            //重新計算損益
            sql = "SELECT BUY,SELL,NUMBER_OF_SHARES FROM realized_db WHERE ID =" + SellID + ";";
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            float tax_price = 0;
            float profit_loss = 0;
            float percent_profit_loss = 0;
            float buy_price = Float.parseFloat(rs.getString(1));
            float sell_price = Float.parseFloat(rs.getString(2));
            int num_of_shares = Integer.parseInt(rs.getString(3));
            tax_price = (buy_price * 0.001425f + sell_price * 0.001425f + sell_price * 0.003f) * num_of_shares;
            profit_loss = sell_price * num_of_shares - buy_price * num_of_shares - tax_price;
            percent_profit_loss = (profit_loss / (buy_price * num_of_shares + tax_price)) * 100;
            //損益更新
            sql = "UPDATE realized_db SET TAX =" + tax_price +
                    ",PROFIT_LOSS =" + profit_loss +
                    ",PERCENT_PROFIT_LOSS =\"" + String.valueOf(percent_profit_loss) + "%\"" +
                    " Where ID=" + SellID + ";";
            stmt.executeUpdate(sql);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return false;
    }

    public DefaultTableModel GetTableModel() {
        return _tableModel;
    }
}
