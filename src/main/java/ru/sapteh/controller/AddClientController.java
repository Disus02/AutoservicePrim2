package ru.sapteh.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.sapteh.model.Client;
import ru.sapteh.model.Gender;
import ru.sapteh.service.ClientDaoImpl;
import ru.sapteh.service.GenderService;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddClientController {
    private ObservableList<Gender> genderObservableList= FXCollections.observableArrayList();
    @FXML
    private TextField txtLastName;
    @FXML
    private Button buttonToClose;
    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtPatronymic;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextField txtEmail;
    @FXML
    private DatePicker pickerBirthday;
    @FXML
    private ComboBox<Gender> comboGender;
    @FXML
    private TextField txtPath;
    @FXML
    private Label status;

    @FXML
    public void initialize(){
        getGenderList(genderObservableList);
        comboGender.setItems(genderObservableList);
    }

    @FXML
    void pressAddClient(ActionEvent event) throws ParseException {
       getClient();
       status.setText("Регистрация прошла успешно");

    }

    @FXML
    void pressClose(ActionEvent event) throws IOException {
        buttonToClose.getScene().getWindow().hide();
        Parent root= FXMLLoader.load(getClass().getResource("/view/main.fxml"));
        Stage stage=new Stage();
        stage.setTitle("Автосервис");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void pressPathImage(ActionEvent event) {
        FileChooser fileChooser=new FileChooser();
        fileChooser.setTitle("Выберите путь до фотографии");
        File file=fileChooser.showOpenDialog(new Stage());
        txtPath.setText(file.getAbsolutePath());
    }

    private void getGenderList(ObservableList<Gender> genders){
        SessionFactory factory=new Configuration().configure().buildSessionFactory();
        GenderService genderService=new GenderService(factory);
        genders.addAll(genderService.readByAll());
        factory.close();
    }
    private void getClient() throws ParseException {
        SessionFactory factory=new Configuration().configure().buildSessionFactory();
        ClientDaoImpl clientDao=new ClientDaoImpl(factory);
        Client client=new Client();
        client.setFirstName(txtFirstName.getText());
        client.setLastName(txtLastName.getText());
        client.setPatronymic(txtPatronymic.getText());
        client.setPhone(txtPhone.getText());
        client.setEmail(txtEmail.getText());
        client.setPhotoPath(txtPath.getText());
        client.setGender(comboGender.getValue());
        Date birthday=new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(pickerBirthday.getValue()));
        client.setBirthday(birthday);
        client.setRegistrationDate(new Date());
        clientDao.create(client);
        factory.close();

    }
}
