package ru.sapteh.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.sapteh.dao.Dao;
import ru.sapteh.model.Client;
import ru.sapteh.service.ClientDaoImpl;

public class WarriorController {
    @FXML
    private Button buttonOK;

    @FXML
    void initialize(){
        buttonOK.setOnAction(event -> {
            deleteClient();
        });
    }
    private void deleteClient(){
        SessionFactory factory=new Configuration().configure().buildSessionFactory();
        Dao<Client,Integer> clientIntegerDao=new ClientDaoImpl(factory);
        clientIntegerDao.delete(MainController.client);
    }
}
