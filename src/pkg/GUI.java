package pkg;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.BoxLayout;
import java.awt.*;

public class GUI {
    JFrame frame = new JFrame("First GUI");
    JTextField text = new JTextField("",15);
    JButton button = new JButton("BUTTON");
    JTable tb = new JTable();

    JPanel text_button_panel = new JPanel();
    JPanel panel = new JPanel();
    JScrollPane scrollPane;

    GUI(DefaultTableModel dtm) {
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tb.setModel(dtm);
        scrollPane = new JScrollPane(tb);

        text_button_panel.add(text);
        text_button_panel.add(button);
        text_button_panel.add(scrollPane);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(text_button_panel);
        panel.add(scrollPane);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }
}
