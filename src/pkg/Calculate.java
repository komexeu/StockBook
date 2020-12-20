package pkg;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Stack;

public class Calculate {
    String percent = "";

    Calculate(DefaultTableModel defaultTableModel) {

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
            }
            return sum / sum_num;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return -99.99f;
    }

    //損益試算
    float RealizeProfitLoss(String stock_ID) {
        float profit_loss = 0;
        float buy_cost = 0;

        try {
            String complax_order = "";
            if (stock_ID.length() == 0)
                complax_order = "SELECT PROFIT_LOSS,BUY FROM realized_db ";
            else
                complax_order = "SELECT PROFIT_LOSS,BUY FROM realized_db WHERE stock_ID =" + stock_ID + ";";
            DataBase_Work dtb = new DataBase_Work();
            ResultSet rs = dtb.SQL_query(complax_order);
            ResultSetMetaData rsmd = rs.getMetaData();

            while (rs.next()) {
                profit_loss += Float.parseFloat(rs.getString(1));
                buy_cost += Float.parseFloat(rs.getString(2));
            }
            percent = percent_caculate(profit_loss, buy_cost);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return profit_loss;
    }

    //趴數計算顯示
    private String percent_caculate(float profit_loss, float buy_price) {
        buy_price*=1000;
        String tmp_result = String.valueOf((profit_loss / buy_price)*100);
        String result = (profit_loss < 0 ? "" : "+ ") + tmp_result + "%";
        return result;
    }

    String GetPercent() {
        return percent;
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
