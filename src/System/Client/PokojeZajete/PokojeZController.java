package System.Client.PokojeZajete;

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

public class PokojeZController {

    @FXML
    private TableColumn<PokojZ, String> Numerpokoju;

    @FXML
    private TableColumn<PokojZ, String> Nazwisko;

    @FXML
    private TableColumn<PokojZ, String> Od;

    @FXML
    private TableColumn<PokojZ, String> Do;

    @FXML
    private TableColumn<PokojZ, String> Liczbamiejsc;

    @FXML
    private TableColumn<PokojZ, String> Cena;

    @FXML
    private TableView<PokojZ> tabela = new TableView<>();

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

    private int Window = 4;
    private int Function = 0;
    private int Loading = 0;
    private boolean isLoaded = false;
    private ArrayList<PokojZ> room2;
    private ObservableList<PokojZ> room;

    public void powrotClicked(ActionEvent e) throws IOException {

        Parent parentSystem = FXMLLoader.load(getClass().getResource("/System/Client/SystemHotelowy/SystemHotelowy.fxml"));
        Scene scenaSystem = new Scene(parentSystem);

        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();

        window.setScene(scenaSystem);
        window.show();
    }

    public void usunClicked(ActionEvent e) throws IOException{

        if(isLoaded == false){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/System/Client/Alerty/Select/Selected.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        }else {


            PokojZ pokojee2 = tabela.getSelectionModel().getSelectedItem();
            if(pokojee2 == null){
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/System/Client/Alerty/Select/Selected.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.setScene(new Scene(root));
                stage.showAndWait();
            }else {

                Function = 3;
                String Numer_pokoju, Od, Do, Liczba_Miejsc, Cena;
                Socket onlySocket = new Socket("127.0.0.1", 1342);
                ObjectOutputStream wyslij = new ObjectOutputStream(onlySocket.getOutputStream());

                wyslij.writeObject(Window);
                wyslij.flush();

                wyslij.writeObject(Function);
                wyslij.flush();

                Numer_pokoju = pokojee2.getNumerpokoju();
                wyslij.writeObject(Numer_pokoju);
                wyslij.flush();

                Od = pokojee2.getOd();
                wyslij.writeObject(Od);
                wyslij.flush();

                Do = pokojee2.getDod();
                wyslij.writeObject(Do);
                wyslij.flush();

                Liczba_Miejsc = pokojee2.getLiczbamiejsc();
                wyslij.writeObject(Liczba_Miejsc);
                wyslij.flush();

                Cena = pokojee2.getCena();
                wyslij.writeObject(Cena);
                wyslij.flush();

                room.removeAll(room);
                tabela.setItems(room);
            }
        }
    }

    public void zaladujClicked(ActionEvent e) throws IOException, ClassNotFoundException {


        if(Loading > 0){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/System/Client/Alerty/Loading/Loading.fxml"));
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
            ObjectInput odbierz = new ObjectInputStream(onlySocket.getInputStream());


            wyslij.writeObject(Window);
            wyslij.flush();

            wyslij.writeObject(Function);
            wyslij.flush();

            room2 = (ArrayList<PokojZ>) odbierz.readObject();
            odbierz.close();

            room = FXCollections.observableArrayList(room2);


            Numerpokoju.setCellValueFactory(new PropertyValueFactory<>("numerpokoju"));

            Nazwisko.setCellValueFactory(new PropertyValueFactory<>("nazwisko"));

            Od.setCellValueFactory(new PropertyValueFactory<>("od"));

            Do.setCellValueFactory(new PropertyValueFactory<>("dod"));

            Liczbamiejsc.setCellValueFactory(new PropertyValueFactory<>("liczbamiejsc"));

            Cena.setCellValueFactory(new PropertyValueFactory<>("cena"));

            tabela.getColumns().clear();
            tabela.getColumns().addAll(Numerpokoju, Nazwisko, Od, Do, Liczbamiejsc, Cena);
            tabela.setItems(room);
            Loading++;
            isLoaded = true;
        }

    }

    public void wyszukanie(javafx.scene.input.KeyEvent keyEvent) throws IOException {

        if(isLoaded == true) {
            FilteredList<PokojZ> szRoom = new FilteredList<>(room, ek -> true);
            szukanie.textProperty().addListener((ObservableValue, oldValue, newValue) -> {
                szRoom.setPredicate(pers -> {

                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String napisane = newValue.toLowerCase();
                    if (pers.getNazwisko().toLowerCase().indexOf(napisane) != -1) {

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
                SortedList<PokojZ> sortedList = new SortedList<>(szRoom);
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
