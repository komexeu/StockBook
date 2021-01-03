package sample;

import pkg.DataBase_Work;

import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.DecimalFormat;


public class Calculate {
    //+
    public static String add(String s1, String s2) {
        BigDecimal f1 = new BigDecimal(s1);
        BigDecimal f2 = new BigDecimal(s2);
        DecimalFormat result = new DecimalFormat("0.00");
        return result.format(f1.add(f2));
    }
    //-
    public static String minus(String s1, String s2) {
        BigDecimal f1 = new BigDecimal(s1);
        BigDecimal f2 = new BigDecimal(s2);
        DecimalFormat result = new DecimalFormat("0.00");
        return result.format(f1.subtract(f2));
    }

    //*
    public static String mul(String s1, String s2) {
        BigDecimal f1 = new BigDecimal(s1);
        BigDecimal f2 = new BigDecimal(s2);
        DecimalFormat result = new DecimalFormat("0.00");
        return result.format(f1.multiply(f2));
    }

    public static String mul(String s1, String s2, String s3) {
        BigDecimal f1 = new BigDecimal(s1);
        BigDecimal f2 = new BigDecimal(s2);
        BigDecimal f3 = new BigDecimal(s3);
        DecimalFormat result = new DecimalFormat("0.00");
        return result.format(f1.multiply(f2).multiply(f3));
    }

    // /
    public static String div(String s1, String s2) {
        System.out.println(s1 + "/" + s2);
        BigDecimal f1 = new BigDecimal(s1);
        BigDecimal f2 = new BigDecimal(s2);
        System.out.println("DIV" + f1.divide(f2, BigDecimal.ROUND_HALF_UP));
        return f1.divide(f2, BigDecimal.ROUND_HALF_UP).toString();
    }

    public static String div(String s1, String s2, int scale) {
        System.out.println(s1 + "/" + s2);
        BigDecimal f1 = new BigDecimal(s1);
        BigDecimal f2 = new BigDecimal(s2);
        System.out.println("DIV->" + f1.divide(f2, scale, BigDecimal.ROUND_HALF_UP));
        return f1.divide(f2, scale, BigDecimal.ROUND_HALF_UP).toString();
    }

    //-----------------------------
    public static String calTax(String full_price) {
        return mul(full_price, "0.001425");
    }
    public static String calHandlingFee(String full_price) {
        return mul(full_price, "0.003");
    }

    //-----------------------------
    private String percent = "";

    Calculate(DefaultTableModel defaultTableModel) {

    }

    //投資成本
    //每筆買入價+手續費
    String SumOfStock(String stock_ID) {
        String Sum = "0";
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
                String tmp_buy = rs.getString(4);
                String tmp_num = rs.getString(6);
                String tmp_result = mul(tmp_buy, tmp_num, String.valueOf(1.001425));
                System.out.print(Sum + ",");
                Sum = add(Sum, tmp_result);
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
    String averageOfBuy(String stock_ID) {
        String sum_num = "0";
        String sum = "0";
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
                return "0";

            ResultSet rs = dtb.SQL_query(complax_order);
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                String tmp_num = rs.getString(6);
                String tmp_buy = rs.getString(4);
                //買進股數
                sum_num = add(sum_num, tmp_num);
                //投資成本(不含手續費)
                sum = add(sum, mul(tmp_buy, tmp_num));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        if (sum_num.equals("0"))
            return "0";
        else
            return div(sum, sum_num);
    }

    //損益試算
    String RealizeProfitLoss(String stock_ID) {
        String profit_loss = "0";
        String buy_cost = "0";

        try {
            String complax_order = "";
            if (stock_ID.length() == 0)
                complax_order = "SELECT PROFIT_LOSS,BUY FROM realized_db ";
            else
                complax_order = "SELECT PROFIT_LOSS,BUY FROM realized_db WHERE stock_ID =" + stock_ID + ";";
            DataBase_Work dtb = new DataBase_Work();
            ResultSet rs = dtb.SQL_query(complax_order);
            while (rs.next()) {
                profit_loss = add(profit_loss, rs.getString(1));
                buy_cost = add(buy_cost, rs.getString(2));
            }
            percent = percent_caculate(profit_loss, buy_cost);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return profit_loss;
    }

    //趴數計算顯示
    private String percent_caculate(String profit_loss, String buy_price) {
        buy_price = mul(buy_price, "1000");
        String tmp_result = mul(div(profit_loss, buy_price, 5), "100");
        String result = (profit_loss.charAt(0) == '-' ? "" : "+ ") + tmp_result + "%";
        return result;
    }

    String GetPercent() {
        return percent;
    }

    //-----------------------------------------
    String AddComma(String s) {
        System.out.println(s);
        String CommaString = "";
        String split_sum[] = s.split("\\.");
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
