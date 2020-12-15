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

            String result= AddComma(Sum);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            return "-999999";
        }
    }

    String AddComma(int s){
        String CommaString="";

        String string_sum = String.valueOf(s);
        int flag = string_sum.length() % 3;
        for (int i = 1; i <= string_sum.length(); ++i) {
            CommaString += string_sum.charAt(i - 1);
            if (((i - flag) % 3 == 0) && i != string_sum.length())
                CommaString += ",";
        }
        System.out.println(CommaString);

        return CommaString;
    }
}
