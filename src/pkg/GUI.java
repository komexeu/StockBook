package pkg;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.BoxLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
    JFrame _frame = new JFrame("First GUI");

    JPanel _text_button_panel = new JPanel();
    JTextField _text = new JTextField("",15);
    JButton _button = new JButton("BUTTON");

    JPanel _panel = new JPanel();
    JTable _tb = new JTable();
    DefaultTableModel _dtm =new DefaultTableModel();
    JScrollPane _scrollPane;
//----------------------
    DataBase_Work db_work=new DataBase_Work();

    GUI() {
        _frame.setSize(500, 500);
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        _tb.setModel(_dtm);
        _scrollPane =new JScrollPane(_tb);

        _text_button_panel.add(_text);
        _text_button_panel.add(_button);
        _button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               db_work.DoSomething(_text.getText());
               UpdateTableModel(db_work.GetTableModel());
            }
        });

        _text_button_panel.add(_scrollPane);

        _panel.setLayout(new BoxLayout(_panel, BoxLayout.Y_AXIS));
        _panel.add(_text_button_panel);
        _panel.add(_scrollPane);

        _frame.getContentPane().add(_panel);
        _frame.setVisible(true);
    }

    void UpdateTableModel(DefaultTableModel dtm){
        _dtm=dtm;
        _tb.setModel(_dtm);
    }
}
