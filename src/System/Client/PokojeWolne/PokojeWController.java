package System.Client.PokojeWolne;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class PokojeWController {

    @FXML
    private TableColumn<PokojW, String> Numerpokoju;

    @FXML
    private TableColumn<PokojW, String> Od;

    @FXML
    private TableColumn<PokojW, String> Do;

    @FXML
    private TableColumn<PokojW, String> Liczbamiejsc;

    @FXML
    private TableColumn<PokojW, String> Cena;

    @FXML
    private TableColumn<PokojW, String> Opis;

    @FXML
    private TableView<PokojW> tabela = new TableView<>();

    @FXML
    private TextField szukanie;

    private double x = 0;
    private double y = 0;

    @FXML
    public void dragged(MouseEvent event){
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }
    @FXML
    public void pressed(MouseEvent event){
        x = event.getSceneX();
        y = event.getSceneY();
    }

    int Window = 2;
    int Loading = 0;
    int Function = 0;
    boolean isLoaded = false;
    public ArrayList<PokojW> room2;
    public ObservableList<PokojW> room;

    public void powrotClicked(ActionEvent e) throws IOException {

        Parent parentSystem = FXMLLoader.load(getClass().getResource("/System/Client/SystemHotelowy/SystemHotelowy.fxml"));
        Scene scenaSystem = new Scene(parentSystem);

        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();

        window.setScene(scenaSystem);
        window.show();
    }

    public void zaladujClicked(ActionEvent e) throws IOException, ClassNotFoundException {


        if(Loading > 0){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/System/Client/Alerty/Loading/Loading.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.showAndWait();
        }else {

            Function = 1;

            Socket onlySocket = new Socket("127.0.0.1", 1342);

            ObjectOutputStream wyslij = new ObjectOutputStream(onlySocket.getOutputStream());
            wyslij.flush();
            ObjectInput odbierz = new ObjectInputStream(onlySocket.getInputStream());

            wyslij.writeObject(Window);
            wyslij.flush();

            wyslij.writeObject(Function);
            wyslij.flush();

            room2 = (ArrayList<PokojW>) odbierz.readObject();

            room = FXCollections.observableArrayList(room2);

            Numerpokoju.setCellValueFactory(new PropertyValueFactory<>("numerpokoju"));

            Liczbamiejsc.setCellValueFactory(new PropertyValueFactory<>("liczbamiejsc"));

            Cena.setCellValueFactory(new PropertyValueFactory<>("cena"));

            Od.setCellValueFactory(new PropertyValueFactory<>("od"));

            Do.setCellValueFactory(new PropertyValueFactory<>("dod"));

            Opis.setCellValueFactory(new PropertyValueFactory<>("opis"));

            tabela.getColumns().clear();
            tabela.getColumns().addAll(Numerpokoju, Liczbamiejsc, Cena, Od, Do, Opis);
            tabela.setItems(room);

            Loading++;
            isLoaded = true;

        }

    }

    public void zatwierdzClicked(ActionEvent e) throws IOException{

        if(isLoaded == false){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/System/Client/Alerty/Select/Selected.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        }else{

            PokojW pokojee = tabela.getSelectionModel().getSelectedItem();

            if(pokojee == null){
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/System/Client/Alerty/Select/Selected.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.setScene(new Scene(root));
                stage.showAndWait();
            }else {
                Function = 2;

                Socket onlySocket = new Socket("127.0.0.1", 1342);

                ObjectOutputStream wyslij = new ObjectOutputStream(onlySocket.getOutputStream());
                wyslij.flush();

                wyslij.writeObject(Window);
                wyslij.flush();

                wyslij.writeObject(Function);
                wyslij.flush();
                String numerpokoju = pokojee.getNumerpokoju();
                String liczbamiejsc = pokojee.getLiczbamiejsc();

                wyslij.writeObject(numerpokoju);
                wyslij.flush();
                wyslij.writeObject(liczbamiejsc);
                wyslij.flush();

                Parent parentNowa = FXMLLoader.load(getClass().getResource("/System/Client/nRezerwacja/nowaRezerwacja.fxml"));
                Scene scenaNowa = new Scene(parentNowa);

                Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();

                window.setScene(scenaNowa);

                window.show();
            }
        }
    }

    public void wyszukanie(javafx.scene.input.KeyEvent keyEvent) throws IOException, ClassNotFoundException {

        if(isLoaded == true) {

            FilteredList<PokojW> szRoom = new FilteredList<>(room, ek -> true);
            szukanie.textProperty().addListener((ObservableValue, oldValue, newValue) -> {
                szRoom.setPredicate(pers -> {

                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String napisane = newValue.toLowerCase();
                    if (pers.getOpis().toLowerCase().indexOf(napisane) != -1) {

                        return true;
                    }
                    if (pers.getNumerpokoju().toLowerCase().indexOf(napisane) != -1) {

                        return true;
                    }
                    if (pers.getOd().toLowerCase().indexOf(napisane) != -1) {

                        return true;
                    }
                    if (pers.getDod().toLowerCase().indexOf(napisane) != -1) {

                        return true;
                    }
                    if (pers.getLiczbamiejsc().toLowerCase().indexOf(napisane) != -1) {

                        return true;
                    }
                    if (pers.getCena().toLowerCase().indexOf(napisane) != -1) {

                        return true;
                    }

                    return false;
                });
                SortedList<PokojW> sortedList = new SortedList<>(szRoom);
                sortedList.comparatorProperty().bind(tabela.comparatorProperty());
                tabela.setItems(sortedList);
            });
        }else{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/System/Client/Alerty/notLoaded/notLoaded.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            szukanie.clear();
        }
    }

}
