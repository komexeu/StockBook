package pkg;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class GUI {
    JFrame _frame = new JFrame("Stock GUI");

    JPanel _full_panel = new JPanel();
    JPanel _left_panel = new JPanel();

    JPanel _search_panel = new JPanel();
    JLabel _search = new JLabel();
    JTextField _text = new JTextField("", 4);
    roundButton _search_button = new roundButton("搜尋");

    JPanel _top_panel = new JPanel();
    JLabel _ID_label = new JLabel();
    JTextField _ID_text = new JTextField("", 4);
    JLabel _NAME_label = new JLabel();
    JTextField _NAME_text = new JTextField("", 12);
    JLabel _BUY_label = new JLabel();
    JTextField _BUY_text = new JTextField("", 7);
    JLabel _SELL_label = new JLabel();
    JTextField _SELL_text = new JTextField("", 7);
    roundButton _newData_button = new roundButton("新增資料");

    JPanel _mid_panel = new JPanel();
    JTable _tb = new JTable();
    DefaultTableModel _dtm = new DefaultTableModel();
    JScrollPane _scrollPane;

    RoundPanel one;
    RoundPanel two;
    RoundPanel three;
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
        _newData_button.setBackground(new Color(200, 200, 255));
        _top_panel.add(_newData_button);

        _tb.setModel(_dtm);
        _scrollPane = new JScrollPane(_tb);
        _mid_panel.setLayout(new BorderLayout());
        JPanel mid_top_panel = new JPanel();
        mid_top_panel.setLayout(new GridLayout());

        one = new RoundPanel(new Color(200, 200, 255));
        one.SetTitle("投資金額");
        one.SetData("$ 1,200,000");

        two = new RoundPanel(new Color(180, 180, 230));
        two.SetTitle("投資損益");
        two.SetData("$ -20,000");

        three = new RoundPanel(new Color(150, 150, 200));
        three.SetTitle("今日台股");
        three.SetData(" 14261.69 ");
        mid_top_panel.add(one);
        mid_top_panel.add(two);
        mid_top_panel.add(three);

        _mid_panel.add(mid_top_panel, BorderLayout.NORTH);
        _mid_panel.add(_scrollPane, BorderLayout.CENTER);

        _search.setText("ID檢索 : ");
        _search_panel.add(_search);
        _search_panel.add(_text);
        _search_button.setBorderPainted(false);
        _search_button.setBackground(new Color(200, 200, 255));
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
                for (int i = 0; i < order_text.length(); ++i)
                    if (!Character.isDigit(order_text.charAt(i)))
                        return;

                db_work.Search(_text.getText());
                UpdateTableModel(db_work.GetTableModel());
                Calculate cal = new Calculate();
                String price = cal.SumOfStock(db_work.GetTableModel());
                one.SetData(price);
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

//p.setBorder(new RoundBorder(new Color(c.getRed(), c.getGreen(), c.getBlue())));
//p.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.WHITE),
//        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
class RoundPanel extends JPanel {
    private Color color;
    JLabel title;
    JLabel data;

    RoundPanel(Color c) {
        super();
        setOpaque(true);
        setSize(80, 30);
        setBackground(c);

        title = new JLabel();
        data = new JLabel();

        add(title);
        add(data);
    }

    void SetTitle(String txt) {
        title.setText(txt);
        title.setForeground(Color.white);
        title.setPreferredSize(new Dimension(80, 60));
        title.setFont(new Font("Dialog", Font.BOLD, 15));
        title.setBounds(0, 0, 80, 30);
        title.setAlignmentY(0.1f);
    }

    void SetData(String txt) {
        data.setText(txt);
        data.setForeground(Color.white);
        data.setPreferredSize(new Dimension(130, 40));
        data.setFont(new Font("Dialog", Font.BOLD, 20));
        data.setBounds(0, 0, 130, 30);
        data.setAlignmentY(0.1f);
    }

    @Override
    public void paint(Graphics g) {
        int fieldX = 0;
        int fieldY = 0;
        int fieldWeight = getSize().width;
        int fieldHeight = getSize().height;
        RoundRectangle2D rect = new RoundRectangle2D.Double(fieldX + 3, fieldY + 2, fieldWeight - 3, fieldHeight - 4, 20, 20);
        g.setClip(rect);
        super.paint(g);
    }
}

class roundButton extends JButton {
    roundButton(String txt) {
        super();
        this.setPreferredSize(new Dimension(txt.length() * 30, 20));
        this.setText(txt);
    }

    @Override
    public void paint(Graphics g) {
        int fieldX = 0;
        int fieldY = 0;
        int fieldWeight = getSize().width;
        int fieldHeight = getSize().height;
        RoundRectangle2D rect = new RoundRectangle2D.Double(fieldX, fieldY, fieldWeight, fieldHeight, 5, 5);
        g.setClip(rect);
        super.paint(g);
    }
}