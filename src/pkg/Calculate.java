package pkg;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Stack;

public class Calculate {
    DefaultTableModel dtm;

    Calculate(DefaultTableModel defaultTableModel) {
        dtm = defaultTableModel;
    }

    //投資成本
    //每筆買入價+手續費
    float SumOfStock(String stock_ID) {
        float Sum = 0;
        try {
            String complax_order = "";
            if (stock_ID.length() == 0)
                complax_order = "SELECT * FROM stock_db WHERE BUY !=0 ORDER BY BUY DESC";
            else
                complax_order = "SELECT * FROM stock_db WHERE BUY !=0 && stock_ID =" + stock_ID + " ORDER BY BUY DESC;";
            DataBase_Work dtb = new DataBase_Work();
            ResultSet rs = dtb.SQL_query(complax_order);
            ResultSetMetaData rsmd = rs.getMetaData();

            int j = 0;
            while (rs.next()) {
                float tmp_buy = Float.parseFloat(rs.getString(4));
                int tmp_num = Integer.parseInt(rs.getString(6));
                Sum += tmp_buy * tmp_num * 1.001425;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return Sum;
    }

    //回收金額
    int ExpensesOfStock() {
        int ex = 0;
        return ex;
    }

    //買進均價(不含手續費)
    //投資成本(不含手續費)/持有股數
    float averageOfBuy(String stock_ID) {
        int sum_num = 0;
        float sum = 0;
        try {
            String complax_order = "";
            String count = "";
            DataBase_Work dtb = new DataBase_Work();
            if (stock_ID.length() == 0) {
                complax_order = "SELECT * FROM stock_db WHERE BUY !=0 ORDER BY BUY DESC";
                ResultSet tmp_rs = dtb.SQL_query("SELECT COUNT(*) FROM stock_db ;");
                while (tmp_rs.next())
                    count = tmp_rs.getString(1);
            } else {
                complax_order = "SELECT * FROM stock_db WHERE BUY !=0 && stock_ID =" + stock_ID + " ORDER BY BUY DESC;";
                ResultSet tmp_rs = dtb.SQL_query("SELECT COUNT(*) FROM stock_db WHERE stock_ID = " + stock_ID + ";");
                while (tmp_rs.next())
                    count = tmp_rs.getString(1);
            }
            System.out.println(count);
            if (count.equals("0"))
                return 0;

            ResultSet rs = dtb.SQL_query(complax_order);
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                float tmp_num = Float.parseFloat(rs.getString(6));
                float tmp_buy = Float.parseFloat(rs.getString(4));
                //買進股數
                sum_num += tmp_num;
                //投資成本(不含手續費)
                sum += tmp_buy * tmp_num;
                //剩餘持有股數(有賣出資料)
                //sum_num += Float.parseFloat(rs.getString(3)) == 0 ? -tmp_num : tmp_num;
            }
            return sum / sum_num;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return -99.99f;
    }

    float averageOfBuy_HandlingFee(String stock_ID) {
        int sum_num = 0;
        float sum = 0;
        try {
            DataBase_Work db = new DataBase_Work();
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/realized_db?serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8",
                    "root", "password");
            Statement stmt = conn.createStatement();

            //取得realized_db資料計算收益

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return sum / sum_num;
    }

    //損益試算
    float RealizeProfitLoss(String stock_ID) {
        Stack BUY_Price = new Stack();
        Stack SELL_Price = new Stack();
        int sum_sell_num = 0;

        try {
//            String complax_order = "";
//            if (stock_ID.length() == 0)
//                complax_order = "SELECT * FROM stock_db ORDER BY SELL DESC";
//            else
//                complax_order = "SELECT * FROM stock_db WHERE stock_ID =" + stock_ID + " ORDER BY SELL DESC;";
//            DataBase_Work dtb = new DataBase_Work();
//            ResultSet rs = dtb.SQL_Order(complax_order);
//            ResultSetMetaData rsmd = rs.getMetaData();
//
//            while (rs.next()) {
//                float tmp_num = Float.parseFloat(rs.getString(6));
//                float tmp_buy = Float.parseFloat(rs.getString(4));
//                float tmp_sell = Float.parseFloat(rs.getString(5));
//                if (tmp_buy == 0) {
//                    sum_sell_num += tmp_num;
//                    SELL_Price.push(tmp_sell);
//                } else {
//                    sum_sell_num -= tmp_num;
//                    BUY_Price.push(tmp_buy);
//                    if (sum_sell_num <= 0)
//                        break;
//                }
//            }
//
//            //SumOfStock(stock_ID) +;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

        float result = 0;

        return result;
    }

    //-----------------------------------------
    String AddComma(float _float) {
        String CommaString = "";
        String string_sum = String.valueOf(_float);
        String split_sum[] = string_sum.split("\\.");
        int flag = split_sum[0].length() % 3;
        for (int i = 1; i <= split_sum[0].length(); ++i) {
            CommaString += split_sum[0].charAt(i - 1);
            if (split_sum[0].charAt(0) == '-') {
                if (((i - flag) % 3 == 0) && i != split_sum[0].length() && i != 1)
                    CommaString += ",";
            } else {
                if (((i - flag) % 3 == 0) && i != split_sum[0].length())
                    CommaString += ",";
            }
        }
        CommaString += ("." + split_sum[1]);
        return CommaString;
    }

    String AddComma(int _int) {
        String CommaString = "";
        String string_sum = String.valueOf(_int);
        int flag = string_sum.length() % 3;
        for (int i = 1; i <= string_sum.length(); ++i) {
            CommaString += string_sum.charAt(i - 1);
            if (string_sum.charAt(0) == '-') {
                if (((i - flag) % 3 == 0) && i != string_sum.length() && i != 1)
                    CommaString += ",";
            } else {
                if (((i - flag) % 3 == 0) && i != string_sum.length())
                    CommaString += ",";
            }
        }

        return CommaString;
    }
}
