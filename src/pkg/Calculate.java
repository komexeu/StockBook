package pkg;

import javax.swing.table.DefaultTableModel;
import java.util.Stack;
import java.util.Vector;

public class Calculate {
    int Sum = 0;
    int expenses = 0;
    DefaultTableModel dtm;

    Calculate(DefaultTableModel defaultTableModel) {
        dtm = defaultTableModel;
    }

    int SumOfStock() {
        try {
            for (int i = 0; i < dtm.getRowCount(); ++i) {
                if (!dtm.getValueAt(i, 2).toString().equals("")) {
                    Float tmp = Float.parseFloat(dtm.getValueAt(i, 2).toString());
                    Sum += Math.round(tmp * 1000);
                }
            }

            String result = AddComma(Sum);
            return Sum;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            return -99999;
        }
    }

    int ExpensesOfStock() {
        try {
            for (int i = 0; i < dtm.getRowCount(); ++i) {
                if (!dtm.getValueAt(i, 3).toString().equals("")) {
                    Float tmp = Float.parseFloat(dtm.getValueAt(i, 3).toString());
                    expenses += Math.round(tmp * 1000);
                }
            }

            return expenses;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            return -99999;
        }
    }

    int MINUS() {
        Stack<String> ID = new Stack<>();
        Stack<Integer> BUY=new Stack<>();
        int sell = 0;
        int buy = 0;

        //不重複選取
        for (int i = 0; i < dtm.getRowCount(); ++i) {
            if (!dtm.getValueAt(i, 3).toString().equals("")) {
                Float tmp = Float.parseFloat(dtm.getValueAt(i, 3).toString());
                sell += Math.round(tmp * 1000);
                ID.add(dtm.getValueAt(i, 0).toString());
            }
        }

        while (ID.size()!=0) {
            String _id = ID.pop();
            for (int j = 0; j < dtm.getRowCount(); ++j) {
                if (!dtm.getValueAt(j, 0).toString().equals(_id))
                    continue;
                if (!dtm.getValueAt(j, 2).toString().equals("")) {
                    float tmp = Float.parseFloat(dtm.getValueAt(j, 2).toString());
                    buy += Math.round(tmp * 1000);
                    break;
                }
            }
        }

        return sell - buy;
    }

    String AddComma(int s) {
        String CommaString = "";

        String string_sum = String.valueOf(s);
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
