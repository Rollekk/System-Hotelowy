package System.Client.SystemHotelowy;

import System.Client.PokojeWolne.PokojeWController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class SystemHotelowy {

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

    public void powrotClicked(ActionEvent e) throws IOException {

            Parent parentLogowanie = FXMLLoader.load(getClass().getResource("/System/Client/Logowanie/Logowanie.fxml"));
            Scene scenaLogowania = new Scene(parentLogowanie);

            Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
            window.setScene(scenaLogowania);
            window.show();

    }

    public void nowaClicked(ActionEvent e)throws IOException{

        Parent parentRez = FXMLLoader.load(getClass().getResource("/System/Client/Rezerwacja/Rezerwacja.fxml"));
        Scene scenaRez = new Scene(parentRez);

        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();

        window.setScene(scenaRez);
        window.show();
    }

    public void wolneClicked(ActionEvent e) throws IOException, ClassNotFoundException {

        Parent parentPok = FXMLLoader.load(getClass().getResource("/System/Client/PokojeWolne/Pokoje.fxml"));
        Scene scenaPok = new Scene(parentPok);

        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();

        window.setScene(scenaPok);
        window.show();

    }

    public void zarezerwowaneClicked(ActionEvent e) throws  IOException{

        Parent parentPok = FXMLLoader.load(getClass().getResource("/System/Client/PokojeZajete/Pokoje2.fxml"));
        Scene scenaPok = new Scene(parentPok);

        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();

        window.setScene(scenaPok);
        window.show();

    }



}
