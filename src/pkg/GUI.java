package pkg;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.BoxLayout;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
    JFrame _frame = new JFrame("First GUI");

    JPanel _full_panel = new JPanel();
    JPanel _left_panel = new JPanel();

    JPanel _search_panel = new JPanel();
    JLabel _search = new JLabel();
    JTextField _text = new JTextField("", 4);
    JButton _search_button = new JButton("搜尋");

    JPanel _top_panel = new JPanel();
    JLabel _ID_label = new JLabel();
    JTextField _ID_text = new JTextField("", 4);
    JLabel _NAME_label = new JLabel();
    JTextField _NAME_text = new JTextField("", 12);
    JLabel _BUY_label = new JLabel();
    JTextField _BUY_text = new JTextField("", 7);
    JLabel _SELL_label = new JLabel();
    JTextField _SELL_text = new JTextField("", 7);
    JButton _newData_button = new JButton("新增資料");

    JPanel _mid_panel = new JPanel();
    JTable _tb = new JTable();
    DefaultTableModel _dtm = new DefaultTableModel();
    JScrollPane _scrollPane;
    //----------------------
    DataBase_Work db_work = new DataBase_Work();

    GUI() {
        _frame.setSize(1000, 500);
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        _ID_label.setText("ID:");
        _NAME_label.setText("名稱:");
        _BUY_label.setText("買進:");
        _SELL_label.setText("賣出:");
        _top_panel.setBackground(new Color(129, 199, 212));
        _top_panel.add(_ID_label);
        _top_panel.add(_ID_text);
        _top_panel.add(_NAME_label);
        _top_panel.add(_NAME_text);
        _top_panel.add(_BUY_label);
        _top_panel.add(_BUY_text);
        _top_panel.add(_SELL_label);
        _top_panel.add(_SELL_text);
        _newData_button.setBorderPainted(false);
        _top_panel.add(_newData_button);

        _tb.setModel(_dtm);
        _scrollPane = new JScrollPane(_tb);
        _mid_panel.setLayout(new BorderLayout());
        JPanel mid_top_panel = new JPanel();
        mid_top_panel.setLayout(new GridLayout());

        GUI_TOP_Message one = new GUI_TOP_Message("投資金額", new Color(200, 200, 255));
        GUI_TOP_Message two = new GUI_TOP_Message("投資損益", new Color(180, 180, 230));
        GUI_TOP_Message three = new GUI_TOP_Message("今日台股", new Color(150, 150, 200));
        mid_top_panel.add(one.label);
        mid_top_panel.add(two.label);
        mid_top_panel.add(three.label);
        _mid_panel.add(mid_top_panel, BorderLayout.NORTH);
        _mid_panel.add(_scrollPane, BorderLayout.CENTER);

        _search.setText("ID檢索 : ");
        _search_panel.add(_search);
        _search_panel.add(_text);
        _search_button.setBorderPainted(false);
        _search_panel.add(_search_button);
        _left_panel.setBackground(new Color(255, 255, 255));
        _search_panel.setBackground(_left_panel.getBackground());
        _left_panel.add(_search_panel);
        _left_panel.setPreferredSize(new Dimension(200, _frame.getHeight()));

        _full_panel.setLayout(new BorderLayout());
        _full_panel.add(_left_panel, BorderLayout.WEST);
        _full_panel.add(_mid_panel, BorderLayout.CENTER);
        _full_panel.add(_top_panel, BorderLayout.NORTH);

        _frame.getContentPane().add(_full_panel);
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
                if (_ID_text.getText().length() != 4)
                    return;
                for (int i = 0; i < _ID_text.getText().length(); ++i)
                    if (!Character.isDigit(_ID_text.getText().charAt(i)))
                        return;

                db_work.Add_Data(_ID_text.getText(), _NAME_text.getText(), _BUY_text.getText(), _SELL_text.getText());
                UpdateTableModel(db_work.GetTableModel());
            }
        });
    }

    void UpdateTableModel(DefaultTableModel dtm) {
        _dtm = dtm;
        _tb.setModel(_dtm);
    }
}

class GUI_TOP_Message {
    public JLabel label;

    GUI_TOP_Message(String txt, Color c) {
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());

        label = new JLabel(txt);
        label.setOpaque(true);
        label.setFont(new Font("Dialog", Font.BOLD, 40));
        label.setPreferredSize(new Dimension(100, 80));
        label.setBackground(new Color(c.getRed(), c.getGreen(), c.getBlue()));
        //label.setBorder(new RoundBorder(new Color(c.getRed(), c.getGreen(), c.getBlue())));
        p.add(label, BorderLayout.CENTER);
    }
}
//p.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.WHITE),
//        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
class RoundBorder implements Border {
    private Color color;

    public RoundBorder(Color color) {
        this.color = color;
    }

    public RoundBorder() {
        this.color = Color.BLACK;
    }

    public Insets getBorderInsets(Component c) {
        return new Insets(0, 0, 0, 0);
    }

    public boolean isBorderOpaque() {
        return true;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width,
                            int height) {
        g.setColor(color);
       // g.drawRoundRect(0, 0, c.getWidth() - 5, c.getHeight() - 5, 15, 15);
        g.fillRoundRect(3, 2, c.getWidth() - 5, c.getHeight() - 5, 15, 15);
    }
}