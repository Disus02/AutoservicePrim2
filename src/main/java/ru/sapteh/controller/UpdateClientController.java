package ru.sapteh.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.sapteh.model.Client;
import ru.sapteh.model.Gender;
import ru.sapteh.service.ClientDaoImpl;
import ru.sapteh.service.GenderService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class UpdateClientController {

    ObservableList<Gender> genders= FXCollections.observableArrayList();
    @FXML
    private TextField txtId;

    @FXML
    private TextField txtLastName;

    @FXML
    private TextField txtFirstName;

    @FXML
    private TextField txtPatronymic;

    @FXML
    private TextField txtPhone;

    @FXML
    private DatePicker pickerBirthday;

    @FXML
    private TextField txtEmail;

    @FXML
    private Button buttonToClose;
    @FXML
    private ComboBox<Gender> comboGender;

    public static Client client=new Client();
    @FXML
    public void initialize(){
        txtFirstName.setText(client.getFirstName());
        txtEmail.setText(client.getEmail());
        txtLastName.setText(client.getLastName());
        txtPhone.setText(client.getPhone());
        txtPatronymic.setText(client.getPatronymic());
        pickerBirthday.getEditor().setText(client.getBirthday().toString());
        getGender();
        comboGender.setItems(genders);
        comboGender.setValue(genders.get(0));

    }

    @FXML
    void pressClose(ActionEvent event) {

    }

    @FXML
    void updateClient(ActionEvent event) throws ParseException {
        SessionFactory factory=new Configuration().configure().buildSessionFactory();
        ClientDaoImpl clientDao=new ClientDaoImpl(factory);
        client.setFirstName(txtFirstName.getText());
        client.setLastName(txtLastName.getText());
        client.setPhone(txtPhone.getText());
        client.setEmail(txtEmail.getText());
        client.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(pickerBirthday.getValue())));
        client.setGender(comboGender.getValue());
        client.setPatronymic(txtPatronymic.getText());
        clientDao.update(client);
        factory.close();

    }

    private void getGender(){
        SessionFactory factory=new Configuration().configure().buildSessionFactory();
        GenderService genderService=new GenderService(factory);
        genders.addAll(genderService.readByAll());
    }
}
