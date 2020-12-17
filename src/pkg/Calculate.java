package pkg;

import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Stack;
import java.util.Vector;

public class Calculate {
    DefaultTableModel dtm;

    Calculate(DefaultTableModel defaultTableModel) {
        dtm = defaultTableModel;
    }

    //投資成本
    //每筆買入價+手續費
    float SumOfStock(String ID) {
        try {
            String complax_order = "";
            if (ID.length() == 0)
                complax_order = "SELECT * FROM stock_db ORDER BY BUY DESC";
            else
                complax_order = "SELECT * FROM stock_db WHERE ID =" + ID + " ORDER BY BUY DESC;";
            System.out.println("HERE-> " + complax_order);
            DataBase_Work dtb = new DataBase_Work();
            ResultSet rs = dtb.SQL_Order(complax_order);
            ResultSetMetaData rsmd = rs.getMetaData();

            int j = 0;
            float Sum = 0;
            while (rs.next()) {
                float tmp_buy = Float.parseFloat(rs.getString(3));
                int tmp_num = Integer.parseInt(rs.getString(5));
                System.out.println(tmp_num);
                Sum += tmp_buy * tmp_num * 1.001425;
            }

            return Sum;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            return -99999;
        }
    }

    //回收金額
    int ExpensesOfStock() {
        int ex = 0;
        return ex;
    }

    //買進均價
    //投資成本/持有股數
    int averageOfBuy() {
        int n = 0;

        for (int i = 0; i < dtm.getRowCount(); ++i) {
            int tmp = Integer.parseInt(dtm.getValueAt(i, 4).toString());
            n = dtm.getValueAt(i, 2).equals("") ? n - tmp : n + tmp;
        }

        return n;
    }

    //損益試算
    float RealizeProfitLoss(String ID, String Fieldname) {
        int result = 0;
        DataBase_Work db = new DataBase_Work();
        db.SQL_Select_OrderBy(ID, Fieldname);

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
