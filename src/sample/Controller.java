package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pkg.SQL_Connect;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.ResultSet;

public class Controller {
    DataBase_Controller db;

    @FXML
    private Button button_search;

    @FXML
    private ComboBox<String> comboBox_INOUT;
    @FXML
    private TextField text_stockID;
    @FXML
    private TextField text_stockName;
    @FXML
    private TextField text_price;
    @FXML
    private TextField text_num;
    @FXML
    private Button button_intputData;

    @FXML
    private TextField SearchText;
    @FXML
    private TableView _table;
    @FXML
    private ComboBox<String> comboBox_Table;


    public void initialize() throws Exception {
        action();

        db = new DataBase_Controller(_table);
        db.init();

        comboBox_INOUT.getItems().setAll("買入", "賣出");
        comboBox_INOUT.setValue("買入");

        comboBox_Table.getItems().setAll("交易紀錄", "庫存股", "獲利結算");
        comboBox_Table.setValue("交易紀錄");
    }

    public void SearchButtonOnAction(javafx.event.ActionEvent actionEvent) {
        System.out.println("TEXT -> " + SearchText.getText());
        SearchText.setText(comboBox_INOUT.getValue());
        //Stage stage = (Stage) SearchButton.getScene().getWindow();
        //stage.close();
    }

    public void SearchStockName() throws Exception {
        SQL_Connect sql_conn = new SQL_Connect();
        ResultSet rs = sql_conn.GET_StockName(text_stockID.getText());

        if (rs.getRow() != 1) {
            text_stockName.setText("");
        }
        while (rs.next()) {
            System.out.println("SEARCH =>" + rs.getString(1));
            text_stockName.setText(rs.getString(1));
        }
    }

    public void AddData() throws Exception {
        SQL_Connect sql_conn = new SQL_Connect();
        sql_conn.Add_OneData(text_stockID.getText(), text_stockName.getText(), text_price.getText(), text_num.getText()
                , comboBox_INOUT.getSelectionModel().getSelectedIndex());
    }

    void action() throws Exception {
        text_stockID.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    SearchStockName();
                } catch (Exception e) {

                }
            }
        });

        comboBox_Table.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                System.out.println("CHANGE");
                if (comboBox_Table.getValue().equals("交易紀錄")) {
                    try {
                        ObservableList data = FXCollections.observableArrayList();
                        //db.GetTable("stock_db");
                        db.GetTable("stockid_information");
                    } catch (Exception e) {

                    }
                } else if (comboBox_Table.getValue().equals("庫存股")) {
                    try {
                        //db.GetTable("hold_db");
                    } catch (Exception e) {

                    }
                } else if (comboBox_Table.getValue().equals("獲利結算")) {
                    try {
                        //db.GetTable("realized_db");
                    } catch (Exception e) {

                    }
                }
            }
        });
    }
}
