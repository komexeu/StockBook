package sample;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import javafx.scene.control.TableColumn.CellDataFeatures;

import java.sql.*;

public class DataBase_Controller {
    ObservableList dbData = FXCollections.observableArrayList();
    TableView _table;

    DataBase_Controller(TableView table) {
        _table = table;
    }

    public void init() throws Exception {
        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/stockbook_db?serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8"
                , "root", "password");
        try {
            String query = "SELECT * FROM stock_db";
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();

            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                final int j = i;
                TableColumn col = new TableColumn(rsmd.getColumnName(i + 1));
                if (rsmd.getColumnName(i + 1).equals("ID") || rsmd.getColumnName(i + 1).equals("NumberOfShares")) {
                    col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, Integer>, ObservableValue<Integer>>() {
                        public ObservableValue<Integer> call(CellDataFeatures<ObservableList, Integer> param) {
                            return new SimpleIntegerProperty(Integer.valueOf(param.getValue().get(j).toString())).asObject();
                        }
                    });
                } else if (rsmd.getColumnName(i + 1).equals("BUY") ||
                        rsmd.getColumnName(i + 1).equals("SELL")) {
                    col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, Float>, ObservableValue<Float>>() {
                        public ObservableValue<Float> call(CellDataFeatures<ObservableList, Float> param) {
                            return new SimpleFloatProperty(Float.valueOf(param.getValue().get(j).toString())).asObject();
                        }
                    });
                } else {
                    col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                        public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                            return new SimpleStringProperty(param.getValue().get(j).toString());
                        }
                    });
                }

                _table.getColumns().addAll(col);
                System.out.println("Column [" + i + "] ");
            }

            System.out.println("rsmd size-> " + rsmd.getColumnCount());
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    row.add(rs.getString(i + 1));
                }
                dbData.add(row);
            }

            _table.setItems(dbData);
            System.out.println("Data size-> " + dbData.size());
            System.out.println("FISISH");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void SearchName() throws Exception{
        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/stockbook_db?serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8"
                , "root", "password");
        try {
            String query = "SELECT * FROM stock_db";
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();

            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                final int j = i;
                TableColumn col = new TableColumn(rsmd.getColumnName(i + 1));
                if (rsmd.getColumnName(i + 1).equals("ID") || rsmd.getColumnName(i + 1).equals("NumberOfShares")) {
                    col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, Integer>, ObservableValue<Integer>>() {
                        public ObservableValue<Integer> call(CellDataFeatures<ObservableList, Integer> param) {
                            return new SimpleIntegerProperty(Integer.valueOf(param.getValue().get(j).toString())).asObject();
                        }
                    });
                } else if (rsmd.getColumnName(i + 1).equals("BUY") ||
                        rsmd.getColumnName(i + 1).equals("SELL")) {
                    col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, Float>, ObservableValue<Float>>() {
                        public ObservableValue<Float> call(CellDataFeatures<ObservableList, Float> param) {
                            return new SimpleFloatProperty(Float.valueOf(param.getValue().get(j).toString())).asObject();
                        }
                    });
                } else {
                    col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                        public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                            return new SimpleStringProperty(param.getValue().get(j).toString());
                        }
                    });
                }

                _table.getColumns().addAll(col);
                System.out.println("Column [" + i + "] ");
            }

            System.out.println("rsmd size-> " + rsmd.getColumnCount());
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    row.add(rs.getString(i + 1));
                }
                dbData.add(row);
            }

            _table.setItems(dbData);
            System.out.println("Data size-> " + dbData.size());
            System.out.println("FISISH");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
