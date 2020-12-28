package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import pkg.SQL_Connect;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.awt.event.ActionEvent;
import java.sql.ResultSet;

public class Controller {
    @FXML
    private Button SearchButton;

    @FXML
    private TextField stock_id_text;
    @FXML
    private TextField stock_name_text;

    @FXML
    private TextField SearchText;
    @FXML
    private TableView _table;
    @FXML
    private ComboBox<String> comboBox_INOUT;

    public void initialize() throws Exception {
        action();

        DataBase_Controller db = new DataBase_Controller(_table);
        db.init();

        comboBox_INOUT.getItems().setAll("輸入", "輸出");
        comboBox_INOUT.setValue("輸入");
    }

    public void SearchButtonOnAction(javafx.event.ActionEvent actionEvent) {
        System.out.println("TEXT -> " + SearchText.getText());
        SearchText.setText(comboBox_INOUT.getValue());
        //Stage stage = (Stage) SearchButton.getScene().getWindow();
        //stage.close();
    }

    public void SearchStockName() throws Exception {
        SQL_Connect sql_conn = new SQL_Connect();
        ResultSet rs = sql_conn.GET_StockName(stock_id_text.getText());

        if (rs.getRow() != 1) {
            stock_name_text.setText("");
        }
        while (rs.next()) {
            if (rs.getString(1).equals("NAME"))
                stock_name_text.setText("");
            else
                stock_name_text.setText(rs.getString(1));
        }
    }
//    public void SearchStockID() throws Exception {
//        SQL_Connect sql_conn = new SQL_Connect();
//        ResultSet rs = sql_conn.GET_StockID(stock_name_text.getText());
//
//        if (rs.getRow() != 1) {
//            stock_id_text.setText("");
//        }
//        while (rs.next()) {
//            stock_id_text.setText(rs.getString(1));
//        }
//    }

    void action() throws Exception {
        stock_id_text.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    SearchStockName();
                } catch (Exception e) {

                }
            }
        });

//        stock_name_text.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                try {
//                    SearchStockID();
//                } catch (Exception e) {
//
//                }
//            }
//        });
    }
}
