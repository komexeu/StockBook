package pkg;

import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Stack;
import java.util.Vector;

public class Calculate {
    String SumOfStock(DefaultTableModel dtm) {
        try {
            int Sum = 0;
            for (int i = 0; i < dtm.getRowCount(); ++i) {
                if (!dtm.getValueAt(i, 2).toString().equals("")) {
                    Float tmp = Float.parseFloat(dtm.getValueAt(i, 2).toString());
                    Sum += Math.round(tmp * 1000);
                }
            }

            String string_sum = String.valueOf(Sum);
            System.out.println(string_sum);
            String result = "";
            int flag = string_sum.length() % 3;
            for (int i = 1; i <= string_sum.length(); ++i) {
                result += string_sum.charAt(i - 1);
                if (i % (3 + flag) == 0 && i != 0 && i != string_sum.length())
                    result += ",";
            }
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            return "-999999";
        }
    }
}
