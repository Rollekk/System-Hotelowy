package System.Client.Rezerwacja;

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

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class RezerwacjaController {

    @FXML
    private TextField szukanie;

    @FXML
    private TableColumn<Rezerwacje, String> Nazwisko;

    @FXML
    private TableColumn<Rezerwacje, String> Adres;

    @FXML
    private TableColumn<Rezerwacje, String> Od;

    @FXML
    private TableColumn<Rezerwacje, String> Do;

    @FXML
    private TableColumn<Rezerwacje, String> Pokoj;

    @FXML
    private TableColumn<Rezerwacje, String> Numer;

    @FXML
    private TableColumn<Rezerwacje, String> Cena;

    @FXML
    private TableView<Rezerwacje> tabela2 = new TableView<>();

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


    private int Window = 3;
    private int Loading = 0;
    private boolean isLoaded = false;

    private ArrayList<Rezerwacje> reservation2;
    private ObservableList<Rezerwacje> reservation;


    public void zameldujClicked(ActionEvent e) throws IOException{

        if(isLoaded == false){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/System/Client/Alerty/Select/Selected.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        }else {



            Rezerwacje rezerwacje = tabela2.getSelectionModel().getSelectedItem();
            if(rezerwacje == null){
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/System/Client/Alerty/Select/Selected.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.setScene(new Scene(root));
                stage.showAndWait();
            }else {

                String Numer_pokoju, Nazwisko, Od, Do, Cena, Liczba_Miejsc;

                int Function = 4;

                Socket onlySocket = new Socket("127.0.0.1", 1342);

                ObjectOutputStream wyslij = new ObjectOutputStream(onlySocket.getOutputStream());
                wyslij.flush();

                wyslij.writeObject(Window);
                wyslij.flush();

                wyslij.writeObject(Function);
                wyslij.flush();

                Numer_pokoju = rezerwacje.getNumer();
                wyslij.writeObject(Numer_pokoju);
                wyslij.flush();

                Nazwisko = rezerwacje.getNazwisko();
                wyslij.writeObject(Nazwisko);
                wyslij.flush();

                Od = rezerwacje.getOd();
                wyslij.writeObject(Od);
                wyslij.flush();

                Do = rezerwacje.getDod();
                wyslij.writeObject(Do);
                wyslij.flush();

                Liczba_Miejsc = rezerwacje.getPokoj();
                wyslij.writeObject(Liczba_Miejsc);
                wyslij.flush();

                Cena = rezerwacje.getCena();
                wyslij.writeObject(Cena);
                wyslij.flush();

                reservation.removeAll(reservation);
                tabela2.setItems(reservation);
            }
        }
    }

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
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        }else {
            int Function = 2;

            Socket onlySocket = new Socket("127.0.0.1", 1342);

            ObjectOutputStream wyslij = new ObjectOutputStream(onlySocket.getOutputStream());
            wyslij.flush();
            ObjectInput odbierz = new ObjectInputStream(onlySocket.getInputStream());


            wyslij.writeObject(Window);
            wyslij.flush();

            wyslij.writeObject(Function);
            wyslij.flush();


            reservation2 = (ArrayList<Rezerwacje>) odbierz.readObject();


            Nazwisko.setCellValueFactory(new PropertyValueFactory<>("nazwisko"));

            Adres.setCellValueFactory(new PropertyValueFactory<>("adres"));

            Od.setCellValueFactory(new PropertyValueFactory<>("od"));

            Do.setCellValueFactory(new PropertyValueFactory<>("dod"));

            Pokoj.setCellValueFactory(new PropertyValueFactory<>("pokoj"));

            Numer.setCellValueFactory(new PropertyValueFactory<>("numer"));

            Cena.setCellValueFactory(new PropertyValueFactory<>("cena"));

            tabela2.getColumns().clear();
            tabela2.getColumns().addAll(Nazwisko, Adres, Od, Do, Pokoj, Numer, Cena);

            reservation = FXCollections.observableArrayList(reservation2);
            tabela2.setItems(reservation);
            Loading++;
            isLoaded = true;
        }
    }


    public void wyszukanie(javafx.scene.input.KeyEvent keyEvent) throws IOException {

        if(isLoaded == true) {
            FilteredList<Rezerwacje> szRoom2 = new FilteredList<>(reservation, ek -> true);
            szukanie.textProperty().addListener((ObservableValue, oldValue, newValue) -> {
                szRoom2.setPredicate(pers -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String napisane = newValue.toLowerCase();
                if (pers.getNazwisko().toLowerCase().indexOf(napisane) != -1) {
                    return true;
                }
                if (pers.getAdres().toLowerCase().indexOf(napisane) != -1) {
                    return true;
                }
                if (pers.getOd().toLowerCase().indexOf(napisane) != -1) {
                    return true;
                }
                if (pers.getDod().toLowerCase().indexOf(napisane) != -1) {
                    return true;
                }
                if (pers.getPokoj().toLowerCase().indexOf(napisane) != -1) {
                    return true;
                }
                if (pers.getNumer().toLowerCase().indexOf(napisane) != -1) {
                    return true;
                }
                if (pers.getCena().toLowerCase().indexOf(napisane) != -1) {
                    return true;
                }

                return false;
            });
            SortedList<Rezerwacje> sortedList = new SortedList<>(szRoom2);
            sortedList.comparatorProperty().bind(tabela2.comparatorProperty());
            tabela2.setItems(sortedList);
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


    public void usunClicked(ActionEvent actionEvent) throws IOException {

        if(isLoaded == false){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/System/Client/Alerty/Select/Selected.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        }else {

            Rezerwacje rezerwacje = tabela2.getSelectionModel().getSelectedItem();
            if(rezerwacje == null){
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/System/Client/Alerty/Select/Selected.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.setScene(new Scene(root));
                stage.showAndWait();
            }else {

                int Function = 3;

                Socket onlySocket = new Socket("127.0.0.1", 1342);

                ObjectOutputStream wyslij = new ObjectOutputStream(onlySocket.getOutputStream());

                wyslij.writeObject(Window);
                wyslij.flush();

                wyslij.writeObject(Function);
                wyslij.flush();

                String Numer_pokoju, Od, Do, Cena, lM;

                Numer_pokoju = rezerwacje.getNumer();
                wyslij.writeObject(Numer_pokoju);
                wyslij.flush();

                lM = rezerwacje.getPokoj();
                wyslij.writeObject(lM);
                wyslij.flush();

                Od = rezerwacje.getOd();
                wyslij.writeObject(Od);
                wyslij.flush();

                Do = rezerwacje.getDod();
                wyslij.writeObject(Do);
                wyslij.flush();

                Cena = rezerwacje.getCena();
                wyslij.writeObject(Cena);
                wyslij.flush();


                reservation.removeAll(rezerwacje);
                tabela2.setItems(reservation);
            }
        }
    }

}
