package sample;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller {
    @FXML
    private Button SearchButton;
    @FXML
    private TableView _table = new TableView();

    public void initialize() throws Exception {
        DataBase_Controller db = new DataBase_Controller(_table);
        db.init();
    }

    public void SearchButtonOnAction(javafx.event.ActionEvent actionEvent) {
        Stage stage = (Stage) SearchButton.getScene().getWindow();
        stage.close();
    }
}
