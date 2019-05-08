package System.Main;

import System.Client.PokojeWolne.PokojW;
import System.Client.PokojeZajete.PokojZ;
import System.Client.Rezerwacja.Rezerwacje;

import java.sql.Statement;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


class Serwer {

    private ObjectOutputStream wyslij ;
    private ObjectInputStream odbierz;

    private Socket connection;
    private ServerSocket socket;

    private boolean canLogin = false;

    private int Window = 0;
    private int Function = 0;

    private String userLogin = null;
    private String userPassword = null;

    private List login = new ArrayList();
    private List password = new ArrayList();

    private ArrayList<PokojW> room = new ArrayList();
    private ArrayList<PokojZ> room2 = new ArrayList();
    private ArrayList<Rezerwacje> reservation = new ArrayList();

    private String numerpokoju;
    private String liczbamiejsc;



    private void Logowanie() throws IOException, ClassNotFoundException {
        userLogin = (String) odbierz.readObject();
        userPassword = (String) odbierz.readObject();

        for (int i = 0; i < login.size(); i++) {
            if (userLogin.equals(login.get(i)) && userPassword.equals(password.get(i))) {
                canLogin = true;
            }

        }
        wyslij.writeObject(canLogin);
        wyslij.flush();
        canLogin = false;
    }
    private void bazaPokojeZ(ArrayList<PokojZ> room2, Connection conn) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select * from pokoje2");
        while (rs.next()){
            room2.add(new PokojZ(
                    rs.getString("NrPokoju"),
                    rs.getString("Nazwisko"),
                    rs.getString("Od"),
                    rs.getString("Do"),
                    rs.getString("LbMiejsc"),
                    rs.getString("Cena")
            ));

        }
    }
    private void bazaPokojeW(ArrayList<PokojW> room, Connection conn) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select * from pokoje");
        while (rs.next()){
            room.add(new PokojW(
                    rs.getString("NrPokoju"),
                    rs.getString("LbMiejsc"),
                    rs.getString("Cena"),
                    rs.getString("Od"),
                    rs.getString("Do"),
                    rs.getString("Opis")
            ));
        }
    }
    private void bazaRezerwacje(ArrayList<Rezerwacje> reservation, Connection conn) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select * from rezerwacje");
        while(rs.next()) {
            reservation.add(new Rezerwacje(
                    rs.getString("Nazwisko"),
                    rs.getString("Adres"),
                    rs.getString("Od"),
                    rs.getString("Do"),
                    rs.getString("LbMiejsc"),
                    rs.getString("NrPokoju"),
                    rs.getString("Cena")
            ));

        }
    }

    private void Pokoje() throws IOException{
        wyslij.writeObject(room);
        wyslij.flush();

    }
    private void Rezerwacje() throws IOException{
        wyslij.writeObject(reservation);
        wyslij.flush();
    }

    private void deleteThis(Connection conn) throws IOException, ClassNotFoundException, SQLException {

        String Numer_pokoju, Od, Do, Liczba_miejsc, Cena;
        Random r = new Random();
        Numer_pokoju = (String) odbierz.readObject();
        Liczba_miejsc = (String) odbierz.readObject();
        Od = (String) odbierz.readObject();
        Do = (String) odbierz.readObject();
        Cena = (String) odbierz.readObject();
        String[] lottery = {"Apartament", "Standard", "Premium", "Lux"};
        int i = r.nextInt(lottery.length);
        String Opis = (lottery[i]);
        Statement st = conn.createStatement();
        st.executeUpdate("delete from rezerwacje where nrPokoju ='" + Numer_pokoju + "'");
        st.executeUpdate("INSERT INTO pokoje(NrPokoju, LbMiejsc, Cena, Od, Do, Opis) VALUES(" + Numer_pokoju + "," + Liczba_miejsc + "," + Cena + ",'" + Od  + "','" +  Do  + "','" + Opis + "')");

        reservation.removeAll(reservation);
        bazaRezerwacje(reservation, conn);

        room.removeAll(room);
        bazaPokojeW(room, conn);
    }


    private void Pokoje2() throws IOException{

        wyslij.writeObject(room2);
        wyslij.flush();

    }

    private void PokojeNext() throws IOException, ClassNotFoundException {

        numerpokoju = (String) odbierz.readObject();
        liczbamiejsc = (String) odbierz.readObject();

    }

    private void Odbierz() throws IOException {
        wyslij.writeObject(numerpokoju);
        wyslij.flush();
        wyslij.writeObject(liczbamiejsc);
        wyslij.flush();

    }


    private void nowaRezerwacja(Connection conn) throws IOException, ClassNotFoundException, SQLException {

        String Nazwisko, Adres, Od, Dod, lMiejsc, nrPokoju,Cena;
        boolean goNext;

        Nazwisko = (String) odbierz.readObject();
        Adres = (String) odbierz.readObject();
        Od = (String) odbierz.readObject();
        Dod = (String) odbierz.readObject();
        lMiejsc = (String) odbierz.readObject();
        nrPokoju = (String) odbierz.readObject();
        Cena = (String) odbierz.readObject();

        if(Nazwisko.isEmpty() || Adres.isEmpty() || Od.isEmpty() || Dod.isEmpty() || lMiejsc.isEmpty() || nrPokoju.isEmpty() || Cena.isEmpty()){
            goNext = false;
        }else {
            goNext = true;
        }
        wyslij.writeObject(goNext);
        if(goNext) {
            Statement st = conn.createStatement();
            st.executeUpdate("INSERT INTO rezerwacje(Nazwisko,Adres, Od, Do, LbMiejsc, NrPokoju, Cena) VALUES(" + "'" + Nazwisko + "','" + Adres + "','" + Od + "','" + Dod + "'," + lMiejsc + "," + nrPokoju + "," + Cena + ")");
            st.executeUpdate("DELETE FROM pokoje WHERE NrPokoju =" + nrPokoju);

            room.removeAll(room);
            bazaPokojeW(room, conn);
            wyslij.writeObject(room);
            wyslij.flush();

            reservation.removeAll(reservation);
            bazaRezerwacje(reservation, conn);
            wyslij.writeObject(reservation);
            wyslij.flush();
        }

    }

    private void zameldujRezerwacja(Connection conn) throws IOException, ClassNotFoundException, SQLException {

        String Numer_pokoju, Nazwisko, Od, Dod, Liczba_miejsc, Cena;

        Numer_pokoju = (String) odbierz.readObject();
        Nazwisko = (String) odbierz.readObject();
        Od = (String) odbierz.readObject();
        Dod = (String) odbierz.readObject();
        Liczba_miejsc = (String) odbierz.readObject();
        Cena = (String) odbierz.readObject();

        room2.removeAll(room2);

        Statement st = conn.createStatement();
        st.executeUpdate("DELETE FROM rezerwacje WHERE NrPokoju =" + Numer_pokoju);
        reservation.removeAll(reservation);
        bazaRezerwacje(reservation, conn);

        st.executeUpdate("INSERT INTO pokoje2(NrPokoju ,Nazwisko, Od, Do, LbMiejsc, Cena) VALUES(" + Numer_pokoju + ",'" + Nazwisko + "','" + Od + "','" + Dod + "'," + Liczba_miejsc + "," + Cena + ")");

        bazaPokojeZ(room2, conn);
        wyslij.writeObject(room2);
        wyslij.flush();
    }

    private void usunPokoje2(Connection conn) throws IOException, ClassNotFoundException, SQLException {

        String Numer_pokoju, Od, Do, Liczba_miejsc,Cena;

        Numer_pokoju = (String) odbierz.readObject();
        Od = (String) odbierz.readObject();
        Do = (String) odbierz.readObject();
        Liczba_miejsc = (String) odbierz.readObject();
        Cena = (String) odbierz.readObject();
        Random r = new Random();
        String[] lottery = {"Apartament", "Standard", "Premium", "Lux"};
        int i = r.nextInt(lottery.length);
        String Opis = (lottery[i]);

        Statement st = conn.createStatement();
        st.executeUpdate("delete from pokoje2 where NrPokoju ='" + Numer_pokoju + "'");
        st.executeUpdate("INSERT INTO pokoje(NrPokoju, LbMiejsc, Cena, Od, Do, Opis) VALUES("+ Numer_pokoju + "," + Liczba_miejsc + "," + Cena + ",'" + Od + "','" + Do + "','" + Opis + "')");

        room.removeAll(room);
        bazaPokojeW(room, conn);

        room2.removeAll(room2);
        bazaPokojeZ(room2, conn);
    }
    public void Server() throws SQLException, IOException, ClassNotFoundException {

            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Projekt?autoReconnect=true&useSSL=false", "root", "Zaq12wsx");
            if (conn != null) {
                System.out.println("Pomyslnie polaczono z baza danych");
            }else{
                System.out.println("Nie mozna polaczyc sie z baza");
            }


        Statement st = conn.createStatement();
        ResultSet resultSet = st.executeQuery("select * from user");
        while (resultSet.next()) {

            login.add(resultSet.getString("login"));
            password.add(resultSet.getString("password"));

        }

        bazaPokojeW(room, conn);
        bazaRezerwacje(reservation, conn);
        bazaPokojeZ(room2, conn);

        socket = new ServerSocket(1342);

        while (true) {

            connection = socket.accept();
            wyslij = new ObjectOutputStream(connection.getOutputStream());
            odbierz = new ObjectInputStream(connection.getInputStream());

            Window = (int) odbierz.readObject();

            if (Window == 1) { //Logowanie
                Logowanie();
            } else if (Window == 2) { //Pokoje Wolne

                Function = (int) odbierz.readObject();

                if (Function == 1) {
                    Pokoje();
                } else if (Function == 2) {
                    PokojeNext();
                }
            } else if (Window == 3) { // Rezerwacje

                Function = (int) odbierz.readObject();

                if (Function == 2) {
                    Rezerwacje();
                } else if (Function == 3) {
                    deleteThis(conn);
                } else if (Function == 4) {
                    zameldujRezerwacja(conn);
                }

            } else if (Window == 4) { // Pokoje Zajete
                Function = (int) odbierz.readObject();

                if (Function == 2) {
                    Pokoje2();
                }else if (Function == 3) {
                    usunPokoje2(conn);
                }
            } else if (Window == 5) {  // Nowa Rezerwacja
                Function = (int) odbierz.readObject();

                if (Function == 1) {
                    Odbierz();
                } else if (Function == 2) {
                    nowaRezerwacja(conn);
                }
            }
        }


    }




}
