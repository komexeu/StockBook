package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

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
        System.out.println("Search.");
        //Stage stage = (Stage) SearchButton.getScene().getWindow();
        //stage.close();
    }
}
