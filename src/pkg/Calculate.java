package pkg;

import javax.swing.table.DefaultTableModel;
import java.util.Stack;
import java.util.Vector;

public class Calculate {
    int expenses = 0;
    DefaultTableModel dtm;

    Calculate(DefaultTableModel defaultTableModel) {
        dtm = defaultTableModel;
    }

    //投資金額
    float SumOfStock() {
        try {
            float Sum = 0;
            for (int i = 0; i < dtm.getRowCount(); ++i) {
                if (!dtm.getValueAt(i, 2).toString().equals("")) {
                    Float tmp = Float.parseFloat(dtm.getValueAt(i, 2).toString());
                    Sum += tmp * Integer.parseInt(dtm.getValueAt(i, 4).toString());
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

    //回收金額
    int ExpensesOfStock() {
        try {
            for (int i = 0; i < dtm.getRowCount(); ++i) {
                if (!dtm.getValueAt(i, 3).toString().equals("")) {
                    Float tmp = Float.parseFloat(dtm.getValueAt(i, 3).toString());
                }
            }

            return expenses;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            return -99999;
        }
    }

    //剩餘股數
    int NumOfShares() {
        int n = 0;

        for (int i = 0; i < dtm.getRowCount(); ++i) {
            int tmp = Integer.parseInt(dtm.getValueAt(i, 4).toString());
            n = dtm.getValueAt(i, 2).equals("") ? n - tmp : n + tmp;
        }

        return n;
    }

    //損益
    //bug:利用剩餘股數做判斷
    float RealizeProfitLoss() {
        DataBase_Work db=new DataBase_Work();

        //所有買入價
        Stack<Float> BUY = new Stack<>();
        //所有賣出價
        Stack<Float> SELL = new Stack<>();
        Stack<String> SELL_ID = new Stack<>();

        for (int i = 0; i < dtm.getRowCount(); ++i) {
            if (!dtm.getValueAt(i, 3).toString().equals("")) {
                Float sell_price = Float.parseFloat(dtm.getValueAt(i, 3).toString());
                SELL.push(sell_price * Float.parseFloat(dtm.getValueAt(i, 4).toString()));
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
                            //用買入價*賣出股數
                            BUY.push(tmp_buy_price * Float.parseFloat(dtm.getValueAt(i, 4).toString()));
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
