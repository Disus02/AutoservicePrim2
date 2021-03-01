package ru.sapteh.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.sapteh.model.Users;
import ru.sapteh.service.UsersDaoImpl;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AddUsersController {
    ObservableList<String> roles= FXCollections.observableArrayList();
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtLogin;
    @FXML
    private TextField txtPassword;
    @FXML
    private ComboBox<String> comboRole;
    @FXML
    private Button buttonToClose;
    @FXML
    private Label status;


    @FXML
    public void initialize(){
        getRole();
        comboRole.setItems(roles);

    }

    @FXML
    public void pressClose(ActionEvent event) throws IOException {
        buttonToClose.getScene().getWindow().hide();
        Parent root= FXMLLoader.load(getClass().getResource("/view/main.fxml"));
        Stage stage=new Stage();
        stage.setTitle("Информация о клиенте");
        Scene scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void addUsers(ActionEvent event){
        getUser();
    }
    private void getRole(){
        SessionFactory factory=new Configuration().configure().buildSessionFactory();
        UsersDaoImpl usersDao=new UsersDaoImpl(factory);
        List<Users> users=usersDao.readByAll();
        Set<String> rolesSet=new HashSet<>();
        for (Users users1:users) {
            rolesSet.add(users1.getRole());
        }
        roles.addAll(rolesSet);
    }
    private void getUser(){
        SessionFactory factory=new Configuration().configure().buildSessionFactory();
        UsersDaoImpl usersDao=new UsersDaoImpl(factory);
        Users users=new Users();
        users.setName(txtName.getText());
        for (Users users1: usersDao.readByAll()) {
            if (!users1.getLogin().equals(txtLogin.getText())){
                users.setLogin(txtLogin.getText());
            }else status.setText("Логин занят");
        }
        users.setPassword(txtPassword.getText());
        users.setRole(comboRole.getValue());
        usersDao.create(users);
        factory.close();
    }
}
