package System.Client.Logowanie;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.Socket;

public class LogowanieController {

    @FXML
    private TextField id;

    @FXML
    private PasswordField haslo;


    private int Window = 1;
    private double x = 0;
    private double y = 0;

    public void logowanieClicked(ActionEvent e) throws IOException, ClassNotFoundException {

        String userPassword;
        String userLogin;


        boolean canLogin;

            Socket onlySocket = new Socket("127.0.0.1", 1342);

            ObjectOutputStream wyslij = new ObjectOutputStream(onlySocket.getOutputStream());
            wyslij.flush();
            ObjectInput odbierz = new ObjectInputStream(onlySocket.getInputStream());

            wyslij.writeObject(Window);
            wyslij.flush();
            userLogin = id.getText();
            wyslij.writeObject(userLogin);
            wyslij.flush();
            userPassword = haslo.getText();
            wyslij.writeObject(userPassword);
            wyslij.flush();

             canLogin = (boolean) odbierz.readObject();

            if(canLogin){
                Parent loadthis = FXMLLoader.load(getClass().getResource("/System/Client/SystemHotelowy/SystemHotelowy.fxml"));
                Scene scenaRezerwacji = new Scene(loadthis);

                Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();

                window.setScene(scenaRezerwacji);
                window.show();

            }else{
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/System/Client/Alerty/Login/Login.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.setScene(new Scene(root));
                stage.showAndWait();
                id.setText("");
                haslo.setText("");
            }
    }
    public void xClicked(javafx.event.ActionEvent actionEvent) {
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.close();
    }
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

}



