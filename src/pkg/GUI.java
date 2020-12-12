package pkg;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.BoxLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
    JFrame _frame = new JFrame("First GUI");

    JPanel _search_panel = new JPanel();
    JLabel _search = new JLabel();
    JTextField _text = new JTextField("", 15);
    JButton _search_button = new JButton("搜尋");

    JPanel _newData_panel = new JPanel();
    JLabel _ID_label = new JLabel();
    JTextField _ID_text = new JTextField("", 10);
    JLabel _NAME_label = new JLabel();
    JTextField _NAME_text = new JTextField("", 15);
    JLabel _BUY_label = new JLabel();
    JTextField _BUY_text = new JTextField("", 10);
    JLabel _SELL_label = new JLabel();
    JTextField _SELL_text = new JTextField("", 10);
    JButton _newData_button = new JButton("新增資料");

    JPanel _panel = new JPanel();
    JTable _tb = new JTable();
    DefaultTableModel _dtm = new DefaultTableModel();
    JScrollPane _scrollPane;
    //----------------------
    DataBase_Work db_work = new DataBase_Work();

    GUI() {
        _frame.setSize(1000, 500);
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        _search.setText("ID檢索 : ");
        _search_panel.add(_search);
        _search_panel.add(_text);
        _search_panel.add(_search_button);

        _ID_label.setText("ID : ");
        _NAME_label.setText("名稱 : ");
        _BUY_label.setText("買進 : ");
        _SELL_label.setText("賣出 : ");
        _newData_panel.add(_ID_label);
        _newData_panel.add(_ID_text);
        _newData_panel.add(_NAME_label);
        _newData_panel.add(_NAME_text);
        _newData_panel.add(_BUY_label);
        _newData_panel.add(_BUY_text);
        _newData_panel.add(_SELL_label);
        _newData_panel.add(_SELL_text);
        _newData_panel.add(_newData_button);

        _tb.setModel(_dtm);
        _scrollPane = new JScrollPane(_tb);

        _panel.setLayout(new BoxLayout(_panel, BoxLayout.Y_AXIS));
        _panel.add(_search_panel);
        _panel.add(_newData_panel);
        _panel.add(_scrollPane);

        _frame.getContentPane().add(_panel);
        _frame.setVisible(true);

        Action();
    }

    void Action() {
        _search_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String order_text = _text.getText();
                if (order_text.length() != 4)
                    return;
                for (int i = 0; i < order_text.length(); ++i)
                    if (!Character.isDigit(order_text.charAt(i)))
                        return;

                db_work.Search(_text.getText());
                UpdateTableModel(db_work.GetTableModel());
            }
        });

        _newData_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                db_work.Add_Data(_ID_text.getText(),_NAME_text.getText(),_BUY_text.getText(),_SELL_text.getText());
                UpdateTableModel(db_work.GetTableModel());
            }
        });
    }

    void UpdateTableModel(DefaultTableModel dtm) {
        _dtm = dtm;
        _tb.setModel(_dtm);
    }
}
