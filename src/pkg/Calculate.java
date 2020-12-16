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

    int RealizeProfitLoss() {
        //所有買入價
        Stack<Integer> BUY = new Stack<>();
        //所有賣出價
        Stack<Integer> SELL = new Stack<>();
        Stack<String> SELL_ID = new Stack<>();

        for (int i = 0; i < dtm.getRowCount(); ++i) {
            if (!dtm.getValueAt(i, 3).toString().equals("")) {
                Float sell_price = Float.parseFloat(dtm.getValueAt(i, 3).toString());
                SELL.push(Math.round(sell_price * 1000));
                //取得賣出ID
                String sell_id = dtm.getValueAt(i, 0).toString();
                //同ID只搜一次
                if (SELL_ID.search(sell_id) == -1) {
                    SELL_ID.push(sell_id);
                    //搜尋所有同ID買入紀錄
                    for (int j = 0; j < dtm.getRowCount(); ++j) {
                        String tmp_ID = dtm.getValueAt(j, 0).toString();
                        if (dtm.getValueAt(j, 2).toString().equals(""))
                            continue;
                        Float tmp_buy_price = Float.parseFloat(dtm.getValueAt(j, 2).toString());
                        if (tmp_ID.equals(sell_id)) {
                            BUY.push(Math.round(tmp_buy_price * 1000));
                        }
                    }
                }
            }
        }

        int result = 0;
        while (SELL.size() != 0) {
            result += SELL.pop();
            result -= BUY.pop();
        }

        return result;
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
