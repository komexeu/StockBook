package pkg;

import sample.MapData;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Objects;

public class GUI {
    String TABLENAME = "stock_db";
    String SORTRULE = "ID";
    int CanEditedRow = -1;
    Boolean UP2DOWN = false;

    JFrame _frame = new JFrame("Stock GUI");

    JPanel _full_panel = new JPanel();
    JPanel _left_panel = new JPanel();

    JPanel _search_panel = new JPanel();
    JLabel _search = new JLabel();
    JTextField _search_text = new JTextField("", 4);
    JButton _search_button = new JButton("搜尋");

    JComboBox _JBuySell;
    String labels[] = {"買進", "賣出"};
    JComboBox _JTableSelect;
    String table_string[] = {"交易紀錄", "庫存股", "獲利結算"};
    JComboBox _JSortRule;
    String Sort_string[] = {"ID ^", "ID v", "stock_ID ^", "stock_ID v", "NAME ^", "NAME v",
            "BUY ^", "BUY v", "SELL ^", "SELL v", "NumberOfShares ^", "NumberOfShares v", "TIME ^", "TIME v"};

    JPanel _top_panel = new JPanel();
    JLabel _ID_label = new JLabel("股票代號:");
    JTextField _ID_text = new JTextField("", 4);
    JLabel _NAME_label = new JLabel("名稱:");
    JTextField _NAME_text = new JTextField("", 12);
    JLabel _Buy_Sell_label = new JLabel("買進價:");
    JTextField _Buy_Sell_text = new JTextField("", 5);
    JLabel _Num_of_shares_label = new JLabel("交易股數:");
    JTextField _Num_of_shares_text = new JTextField("", 5);
    roundButton _newData_button = new roundButton("新增資料");

    JPanel _mid_panel = new JPanel();
    JTable _table = new JTable() {
        public boolean isCellEditable(int row, int column) {
            return row == CanEditedRow;
        }

        public void columnSelectionChanged(ListSelectionEvent event) {
            CanEditedRow = -1;
        }
    };
    JPopupMenu table_popmenu;
    DefaultTableModel _dtm = new DefaultTableModel();
    JScrollPane _scrollPane;

    RoundPanel first;
    RoundPanel second;
    RoundPanel third;

    //----------------------
    void Init_table() {
        _table.setRowHeight(25);
    }


    void UpdateTableRowColor() {
        DefaultTableCellRenderer tableRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (row % 2 == 0)
                    setBackground(new Color(230, 230, 255));
                else if (row % 2 == 1)
                    setBackground(Color.white);
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        };
        for (int i = 0; i < _table.getColumnCount(); i++) {
            _table.getColumn(_table.getColumnName(i)).setCellRenderer(tableRenderer);
        }
    }

    GUI() {
        _frame.setSize(1000, 500);
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        _top_panel.setBackground(new Color(180, 180, 255));
        _JBuySell = new JComboBox(labels);
        _top_panel.add(_JBuySell);
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


        _JTableSelect = new JComboBox(table_string);
        _left_panel.add(_JTableSelect);
        _JSortRule = new JComboBox(Sort_string);
        _left_panel.add(_JSortRule);
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
        Init_table();
    }

    void Action() {
        _ID_text.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent documentEvent) {
                _top_panel.setBackground(new Color(180, 180, 255));
            }

            public void insertUpdate(DocumentEvent documentEvent) {
                _top_panel.setBackground(new Color(180, 180, 255));
                String id = _ID_text.getText();
                SQL_Connect sql = new SQL_Connect();
                ResultSet rs = sql.GET_StockName(id);
                try {
                    if (rs.getRow() != 1) {
                        _NAME_text.setText("");
                    }
                    while (rs.next()) {
                        _NAME_text.setText(rs.getString(1));
                    }
                } catch (Exception e) {

                }
            }

            public void removeUpdate(DocumentEvent documentEvent) {
                _top_panel.setBackground(new Color(180, 180, 255));
                String id = _ID_text.getText();
                SQL_Connect sql = new SQL_Connect();
                ResultSet rs = sql.GET_StockName(id);
                try {
                    if (rs.getRow() != 1) {
                        _NAME_text.setText("");
                    }
                    while (rs.next()) {
                        _NAME_text.setText(rs.getString(1));
                    }
                } catch (Exception e) {

                }
            }
        });

        _Buy_Sell_text.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                _top_panel.setBackground(new Color(180, 180, 255));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                _top_panel.setBackground(new Color(180, 180, 255));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                _top_panel.setBackground(new Color(180, 180, 255));
            }
        });

        _Num_of_shares_text.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                _top_panel.setBackground(new Color(180, 180, 255));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                _top_panel.setBackground(new Color(180, 180, 255));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                _top_panel.setBackground(new Color(180, 180, 255));
            }
        });

        _search_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UpdateTopData();
            }
        });

        _newData_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mode = String.valueOf(_JSortRule.getItemAt(_JSortRule.getSelectedIndex()));

                if (_Num_of_shares_text.getText().equals(""))
                    return;
                for (int i = 0; i < _ID_text.getText().length(); ++i)
                    if (!Character.isDigit(_ID_text.getText().charAt(i)))
                        return;

                DataBase_Work db_work = new DataBase_Work();
                boolean success_addData = db_work.Add_Data(_ID_text.getText(), _NAME_text.getText(), _Buy_Sell_text.getText(),
                        _Num_of_shares_text.getText(), mode, _JBuySell.getSelectedIndex(), UP2DOWN);
                ChangeColor(success_addData);
                db_work.Search(TABLENAME, _ID_text.getText(), SORTRULE, UP2DOWN);
                EditTable(db_work.GetTableModel());
                String stock_id = _ID_text.getText();
                UpdateTopData(stock_id);
            }
        });

        _JBuySell.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mode = String.valueOf(_JBuySell.getItemAt(_JBuySell.getSelectedIndex()));
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

        _JSortRule.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //todo (X):三種表排序規則，變更資料表時，排序下拉清單內容變更
                //                  OR
                //todo (X):直接在名稱上進行選取

                //todo (X):賣出時判定股票數量，將剩餘股數與成本重新計算並加入新資料
                //todo (O):建立所有股票ID/NAME TABLE
                //todo (O):自動填入股票名稱
                //todo (X):除權成本為0，資料判定方法變更
                //todo (O):已實現總損益計算
                //todo (O):計算用BigDecimal
                //todo (O):放大資料表
                //todo (X):新增一上欄，顯示獲利%數
                //todo (X):正規化
                //todo (X):資料庫新增 %數/總成本 欄位

                String mode = String.valueOf(_JSortRule.getItemAt(_JSortRule.getSelectedIndex()));
                if (_JSortRule.getSelectedIndex() % 2 != 0)
                    UP2DOWN = true;
                else
                    UP2DOWN = false;
                SORTRULE = mode;

                UpdateTopData();
            }
        });

        _JTableSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int mode = _JTableSelect.getSelectedIndex();
                switch (mode) {
                    case 0:
                        TABLENAME = "stock_db";
                        break;
                    case 1:
                        TABLENAME = "hold_db";
                        break;
                    case 2:
                        TABLENAME = "realized_db";
                        break;
                }
                UpdateTopData();
            }
        });

        _table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                TableButtonClick(e);
            }

            private void TableButtonClick(MouseEvent event) {
                //右鍵動作
                if (event.getButton() == MouseEvent.BUTTON3) {
                    int focusedRowIndex = _table.rowAtPoint((event.getPoint()));
                    if (focusedRowIndex == -1)
                        return;
                    _table.setRowSelectionInterval(focusedRowIndex, focusedRowIndex);
                    createPopupMenu();
                    table_popmenu.show(_table, event.getX(), event.getY());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    void createPopupMenu() {
        table_popmenu = new JPopupMenu();

//        JMenuItem delete_tableData = new JMenuItem();
//        delete_tableData.setText("刪除資料");
//        delete_tableData.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                System.out.println("Delete Data.");
//            }
//        });
//        table_popmenu.add(delete_tableData);

        JMenuItem update_tableData = new JMenuItem();
        update_tableData.setText("修改資料");
        update_tableData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Update Data.");
                CanEditedRow = _table.getSelectedRow();
                _table.isCellEditable(CanEditedRow, _table.getSelectedColumn());
            }
        });
        table_popmenu.add(update_tableData);
    }

    void EditTable(DefaultTableModel dtm) {
        _dtm = dtm;
        _dtm.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int type = e.getType();
                int row = e.getFirstRow();
                int column = e.getColumn();

                DataBase_Work dataBase_work = new DataBase_Work();
                String ColumnName = "";
                //get key by value
                for (Map.Entry<String, String> entry : MapData.map_tableTitle.entrySet()) {
                    if (Objects.equals(_dtm.getColumnName(column), entry.getValue())) {
                        ColumnName = entry.getKey();
                    }
                }
                //只能對交易紀錄做更動
                if (TABLENAME != "stock_db")
                    return;
                if (ColumnName == "BUY" || ColumnName == "SELL" || ColumnName == "NUMBER_OF_SHARES") {
                    boolean result = dataBase_work.editData(TABLENAME, _dtm.getValueAt(row, 0).toString(),
                            ColumnName, _dtm.getValueAt(row, column).toString());
                    UpdateTopData();
                }
            }
        });
        _table.setModel(_dtm);
        UpdateTableRowColor();
    }

    void UpdateTopData() {
        String stock_id = _search_text.getText();
        for (int i = 0; i < stock_id.length(); ++i)
            if (!Character.isDigit(stock_id.charAt(i)))
                return;

        DataBase_Work db_work = new DataBase_Work();
        db_work.Search(TABLENAME, stock_id, SORTRULE, UP2DOWN);
        EditTable(db_work.GetTableModel());

        Calculate cal = new Calculate(db_work.GetTableModel());
        String price = "$ " + cal.AddComma(cal.SumOfStock(stock_id));
        first.text.setText(price);
        second.text.setText(cal.AddComma(cal.averageOfBuy(stock_id)));
        third.text.setText("$ " + cal.AddComma(cal.RealizeProfitLoss_FULL(stock_id)) + " / " + cal.GetPercent());
    }

    void UpdateTopData(String stock_ID) {
        DataBase_Work db_work = new DataBase_Work();
        Calculate cal = new Calculate(db_work.GetTableModel());
        String price = "$ " + cal.AddComma(cal.SumOfStock(stock_ID));
        first.text.setText(price);
        second.text.setText(cal.AddComma(cal.averageOfBuy(stock_ID)));
        third.text.setText("$ " + cal.AddComma(cal.RealizeProfitLoss_FULL(stock_ID)) + " / " + cal.GetPercent());
    }

    void ChangeColor(boolean success) {
        Color c = success ? new Color(0, 215, 0) : new Color(215, 0, 0);
        _top_panel.setBackground(c);
    }
}

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
        text.setPreferredSize(new Dimension(300, 40));
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