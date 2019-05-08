package System.Client.nRezerwacja;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.format.DateTimeFormatter;

public class nRezerwacjaController {

    @FXML
    private TextField nazwisko;

    @FXML
    private TextField adres;

    @FXML
    private TextField Cena;

    @FXML
    private TextField liczba_miejsc;

    @FXML
    private TextField nrPokoju;

    @FXML
    private DatePicker dataOd;

    @FXML
    private DatePicker dataDo;

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

    int Window = 5;
    private int Loading = 0;

    public void anulujClicked(ActionEvent e) throws IOException {

            Parent parentSystem = FXMLLoader.load(getClass().getResource("/System/Client/SystemHotelowy/SystemHotelowy.fxml"));
            Scene scenaSystem = new Scene(parentSystem);

            Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();

            window.setScene(scenaSystem);
            window.show();
            }

    public void zatwierdzClicked(ActionEvent e) throws IOException, ClassNotFoundException {

        boolean goNext;
        String Nazwisko, Adres, od, dod, lm, nrp,cena;

        if(dataOd.getValue() == null || dataDo.getValue() == null){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/System/Client/Alerty/notAll/notAll.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        }else {

            Socket onlySocket = new Socket("127.0.0.1", 1342);

            ObjectOutputStream wyslij = new ObjectOutputStream(onlySocket.getOutputStream());
            wyslij.flush();
            ObjectInput odbierz = new ObjectInputStream(onlySocket.getInputStream());

            int Function = 2;

            wyslij.writeObject(Window);
            wyslij.flush();

            wyslij.writeObject(Function);
            wyslij.flush();

            Nazwisko = nazwisko.getText();
            wyslij.writeObject(Nazwisko);
            wyslij.flush();

            Adres = adres.getText();
            wyslij.writeObject(Adres);
            wyslij.flush();

            od = dataOd.getValue().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
            wyslij.writeObject(od);
            wyslij.flush();

            dod = dataDo.getValue().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
            wyslij.writeObject(dod);
            wyslij.flush();

            lm = liczba_miejsc.getText();
            wyslij.writeObject(lm);
            wyslij.flush();

            nrp = nrPokoju.getText();
            wyslij.writeObject(nrp);
            wyslij.flush();

            cena = Cena.getText();
            wyslij.writeObject(cena);
            wyslij.flush();

            goNext = (boolean) odbierz.readObject();

            if (goNext) {
                Parent parentSystem = FXMLLoader.load(getClass().getResource("/System/Client/SystemHotelowy/SystemHotelowy.fxml"));
                Scene scenaSystem = new Scene(parentSystem);

                Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();

                window.setScene(scenaSystem);
                window.show();
            } else {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/System/Client/Alerty/notAll/notAll.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.setScene(new Scene(root));
                stage.showAndWait();
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

            Socket onlySocket = new Socket("127.0.0.1", 1342);

            ObjectOutputStream wyslij = new ObjectOutputStream(onlySocket.getOutputStream());
            wyslij.flush();
            ObjectInput odbierz = new ObjectInputStream(onlySocket.getInputStream());

            int Function = 1;

            wyslij.writeObject(Window);
            wyslij.flush();
            wyslij.writeObject(Function);
            wyslij.flush();

            nrPokoju.setText((String) odbierz.readObject());
            liczba_miejsc.setText((String) odbierz.readObject());
            Loading++;
        }
    }
}
