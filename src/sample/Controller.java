package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Controller {
    @FXML
    private Button SearchButton = new Button();
    @FXML
    private TextField SearchText = new TextField();
    @FXML
    private TableView _table = new TableView();

    public void initialize() throws Exception {
        DataBase_Controller db = new DataBase_Controller(_table);
        db.init();

    }

    public void SearchButtonOnAction(javafx.event.ActionEvent actionEvent) {
        System.out.println("TEXT -> " + SearchText.getText());
        Stage stage = (Stage) SearchButton.getScene().getWindow();
        //stage.close();
    }
}
