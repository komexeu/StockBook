package pkg;

import javax.swing.*;

public class StockBookUI {
    public JTable table1;
    private JButton button1;
    private JPanel BasePanel;
    private JTextField textField1;
    private JTextField textField2;
    public JScrollPane scrollPane;
    public JFrame frame;
    StockBookUI(){
        frame = new JFrame("StockBook");
        scrollPane=new JScrollPane();
        frame.setContentPane(this.BasePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    void SetScrollPane(){

    }
}
