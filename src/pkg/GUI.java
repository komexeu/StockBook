package pkg;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class GUI {
    String SORTRULE = "ID";
    Boolean UP2DOWN = true;

    JFrame _frame = new JFrame("Stock GUI");

    JPanel _full_panel = new JPanel();
    JPanel _left_panel = new JPanel();

    JPanel _search_panel = new JPanel();
    JLabel _search = new JLabel();
    JTextField _search_text = new JTextField("", 4);
    roundButton _search_button = new roundButton("搜尋");

    JComboBox _Jbox;
    JComboBox _JFieldNamebox;
    JPanel _top_panel = new JPanel();
    JLabel _ID_label = new JLabel("ID:");
    JTextField _ID_text = new JTextField("", 4);
    JLabel _NAME_label = new JLabel("名稱:");
    JTextField _NAME_text = new JTextField("", 12);
    JLabel _Buy_Sell_label = new JLabel("買進價:");
    JTextField _Buy_Sell_text = new JTextField("", 5);
    JLabel _Num_of_shares_label = new JLabel("交易股數:");
    JTextField _Num_of_shares_text = new JTextField("", 5);
    roundButton _newData_button = new roundButton("新增資料");

    JPanel _mid_panel = new JPanel();
    JTable _table = new JTable();
    DefaultTableModel _dtm = new DefaultTableModel();
    JScrollPane _scrollPane;

    RoundPanel first;
    RoundPanel second;
    RoundPanel third;
    //----------------------

    GUI() {
        _frame.setSize(1000, 500);
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        _top_panel.setBackground(new Color(180, 180, 255));
        String labels[] = {"買進", "賣出"};
        _Jbox = new JComboBox(labels);
        _top_panel.add(_Jbox);
        _top_panel.add(_ID_label);
        _top_panel.add(_ID_text);
        _top_panel.add(_NAME_label);
        _top_panel.add(_NAME_text);
        _top_panel.add(_Buy_Sell_label);
        _top_panel.add(_Buy_Sell_text);
        _top_panel.add(_Num_of_shares_label);
        _top_panel.add(_Num_of_shares_text);
        _newData_button.setBorderPainted(false);
        _newData_button.setBackground(new Color(200, 200, 255));
        _top_panel.add(_newData_button);

        _table.setModel(_dtm);
        _scrollPane = new JScrollPane(_table);
        _mid_panel.setLayout(new BorderLayout());
        JPanel mid_top_panel = new JPanel();
        mid_top_panel.setLayout(new GridLayout());

        first = new RoundPanel(new Color(200, 200, 255));
        first.InitTitle("投資金額");
        first.InitData("$ --.--");

        second = new RoundPanel(new Color(180, 180, 230));
        second.InitTitle("買進均價");
        second.InitData("--.--");

        third = new RoundPanel(new Color(150, 150, 200));
        third.InitTitle("已實現損益");
        third.InitData(" --.--");
        mid_top_panel.add(first);
        mid_top_panel.add(second);
        mid_top_panel.add(third);

        _mid_panel.add(mid_top_panel, BorderLayout.NORTH);
        _mid_panel.add(_scrollPane, BorderLayout.CENTER);

        String FieldName[] = {"ID v", "ID ^", "NAME v", "NAME ^",
                "BUY v", "BUY ^", "SELL v", "SELL ^", "NumberOfShares v", "NumberOfShares ^"};
        _JFieldNamebox = new JComboBox(FieldName);
        _left_panel.add(_JFieldNamebox);
        _search.setText("ID檢索 : ");
        _search_panel.add(_search);
        _search_panel.add(_search_text);
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
                String order_text = _search_text.getText();
                for (int i = 0; i < order_text.length(); ++i)
                    if (!Character.isDigit(order_text.charAt(i)))
                        return;

                DataBase_Work db_work = new DataBase_Work();
                db_work.Search(order_text, SORTRULE, UP2DOWN);
                UpdateTableModel(db_work.GetTableModel());
                UpdateTopData(order_text);
            }
        });

        _newData_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (_Num_of_shares_text.getText().equals(""))
                    return;
                for (int i = 0; i < _ID_text.getText().length(); ++i)
                    if (!Character.isDigit(_ID_text.getText().charAt(i)))
                        return;

                DataBase_Work db_work = new DataBase_Work();
                db_work.Add_Data(_ID_text.getText(), _NAME_text.getText(),
                        _Buy_Sell_text.getText(), _Num_of_shares_text.getText(),
                        "BUY", _Jbox.getSelectedIndex(), UP2DOWN);
                UpdateTableModel(db_work.GetTableModel());
                UpdateTopData(_Num_of_shares_text.getText());
            }
        });

        _Jbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mode = String.valueOf(_Jbox.getItemAt(_Jbox.getSelectedIndex()));
                switch (mode) {
                    case "買進":
                        _Buy_Sell_label.setText("買進價:");
                        break;
                    case "賣出":
                        _Buy_Sell_label.setText("賣出價:");
                        break;
                }
            }
        });

        _JFieldNamebox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mode = String.valueOf(_JFieldNamebox.getItemAt(_JFieldNamebox.getSelectedIndex()));
                if (_JFieldNamebox.getSelectedIndex() % 2 == 0)
                    UP2DOWN = true;
                else
                    UP2DOWN = false;
                SORTRULE = mode;

                String order_text = _search_text.getText();
                for (int i = 0; i < order_text.length(); ++i)
                    if (!Character.isDigit(order_text.charAt(i)))
                        return;

                DataBase_Work db_work = new DataBase_Work();
                db_work.Search(order_text, SORTRULE, UP2DOWN);
                UpdateTableModel(db_work.GetTableModel());
                UpdateTopData(order_text);
            }
        });
    }

    void UpdateTableModel(DefaultTableModel dtm) {
        _dtm = dtm;
        _table.setModel(_dtm);
    }

    void UpdateTopData(String ID) {
        DataBase_Work db_work = new DataBase_Work();
        Calculate cal = new Calculate(db_work.GetTableModel());
        String price = "$ " + cal.AddComma(cal.SumOfStock(_search_text.getText()));
        first.text.setText(price);
        second.text.setText(cal.AddComma(cal.averageOfBuy(_search_text.getText())));
        third.text.setText("$ " + cal.AddComma(cal.RealizeProfitLoss(ID, SORTRULE)));
    }
}

//p.setBorder(new RoundBorder(new Color(c.getRed(), c.getGreen(), c.getBlue())));
//p.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.WHITE),
//        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
class RoundPanel extends JPanel {
    private Color color;
    //使用函式調用會出現TABLE疊加在後面BUG
    public JLabel title;
    public JLabel text;

    RoundPanel(Color c) {
        super();
        color = c;
        setOpaque(true);
        setSize(80, 30);
        setBackground(c);

        title = new JLabel();
        text = new JLabel();

        add(title);
        add(text);
    }

    void InitTitle(String txt) {
        title.setText(txt);
        title.setForeground(Color.white);
        title.setPreferredSize(new Dimension(80, 60));
        title.setFont(new Font("Dialog", Font.BOLD, 15));
        title.setBounds(0, 0, 80, 30);
        title.setAlignmentY(0.1f);
    }

    void InitData(String txt) {
        text.setText(txt);
        text.setForeground(Color.white);
        text.setPreferredSize(new Dimension(130, 40));
        text.setFont(new Font("Dialog", Font.BOLD, 20));
        text.setBounds(0, 0, 130, 30);
        text.setAlignmentY(0.1f);
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