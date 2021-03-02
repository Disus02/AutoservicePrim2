package ru.sapteh.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import ru.sapteh.model.Client;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UpdateClientController {
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
    private TextField txtGender;
    public static Client client=new Client();
    @FXML
    public void initialize(){
        txtId.setText(String.valueOf(MainController.idClient));
        txtFirstName.setText(client.getFirstName());
        txtEmail.setText(client.getEmail());
        txtGender.setText(String.valueOf(client.getGender().getCode()));
        txtLastName.setText(client.getLastName());
        txtPhone.setText(client.getPhone());
        txtPatronymic.setText(client.getPatronymic());
        pickerBirthday.getEditor().setText(String.valueOf(client.getBirthday()));
    }

    @FXML
    void pressClose(ActionEvent event) {

    }

    @FXML
    void updateClient(ActionEvent event) {

    }
}
