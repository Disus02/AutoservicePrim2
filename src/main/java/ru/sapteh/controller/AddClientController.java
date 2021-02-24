package ru.sapteh.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import ru.sapteh.model.Gender;

public class AddClientController {
    @FXML
    private TextField txtLastName;

    @FXML
    private TextField txtFirstName;

    @FXML
    private TextField txtPatronymic;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtBirthday;

    @FXML
    private ComboBox<Gender> comboGender;

    @FXML
    private TextField txtPath;

    @FXML
    void pressAddClient(ActionEvent event) {

    }

    @FXML
    void pressClose(ActionEvent event) {

    }

    @FXML
    void pressPathImage(ActionEvent event) {

    }
}
