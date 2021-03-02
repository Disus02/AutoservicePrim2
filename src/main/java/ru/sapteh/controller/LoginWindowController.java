package ru.sapteh.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.sapteh.model.Users;
import ru.sapteh.service.UsersDaoImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class LoginWindowController {
List<Users> usersList=new ArrayList<>();
    @FXML
    private TextField txtLogin;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Button openMain;
    @FXML
    private Label status;
    public static  String role;

    @FXML
    void pressExit(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void pressOpen(ActionEvent event) throws IOException {
        getUsers();
        if (txtLogin.getText().isEmpty()&&txtPassword.getText().isEmpty()) {
            status.setText("Логин и пароль не заполнены");
        }else if (txtLogin.getText().isEmpty()){
            status.setText("Логин пустой");
        }else if (txtPassword.getText().isEmpty()){
            status.setText("Пароль пустой");
        } else
        for (Users users:usersList) {
            if (users.getLogin().equals(txtLogin.getText())&&users.getPassword().equals(txtPassword.getText())){
                   role=users.getRole();
                   openMain.getScene().getWindow().hide();
                   Parent parent= FXMLLoader.load(getClass().getResource("/view/main.fxml"));
                   Stage stage=new Stage();
                   stage.setTitle("Информация о клиенте");
                   Scene scene=new Scene(parent);
                   stage.setScene(scene);
                   stage.show();
            }else status.setText("Пароля и логина не существует");
        }
    }
    private void getUsers(){
        SessionFactory factory=new Configuration().configure().buildSessionFactory();
        UsersDaoImpl usersDao=new UsersDaoImpl(factory);
        usersList=usersDao.readByAll();
    }
}
