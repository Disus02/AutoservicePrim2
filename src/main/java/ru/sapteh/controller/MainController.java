package ru.sapteh.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.sapteh.dao.Dao;
import ru.sapteh.model.Client;
import ru.sapteh.model.ClientService;
import ru.sapteh.model.Gender;
import ru.sapteh.service.ClientDaoImpl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Set;

public class MainController {

    ObservableList<Client> clients= FXCollections.observableArrayList();
    @FXML
    private TableView<Client> tableClient;
    @FXML
    private TableColumn<Client,Integer> columnID;
    @FXML
    private TableColumn<Client,Character> columnGender;
    @FXML
    private TableColumn<Client,String> columnLastName;
    @FXML
    private TableColumn<Client,String> columnFirstName;
    @FXML
    private TableColumn<Client,String> columnPatronymic;
    @FXML
    private TableColumn<Client, Date> columnBirthday;
    @FXML
    private TableColumn<Client,String> columnPhone;
    @FXML
    private TableColumn<Client,String> columnEmail;
    @FXML
    private TableColumn<Client,Date> columnDataReg;
    @FXML
    private TableColumn<Client,String> columnDataLastVisit;
    @FXML
    private TableColumn<Client,Integer> columnSizeVisit;
    @FXML
    private Label status;
    @FXML
    private Button buttonOpenAdd;
    private int sizeClients;
    private int sizeList;
    @FXML
    public void initialize(){
        getList(clients);
    columnID.setCellValueFactory(c->new SimpleObjectProperty<>(c.getValue().getId()));
    columnGender.setCellValueFactory(c->new SimpleObjectProperty<>(c.getValue().getGender().getCode()));
    columnLastName.setCellValueFactory(c->new SimpleObjectProperty<>(c.getValue().getLastName()));
    columnFirstName.setCellValueFactory(c->new SimpleObjectProperty<>(c.getValue().getFirstName()));
    columnPatronymic.setCellValueFactory(c->new SimpleObjectProperty<>(c.getValue().getPatronymic()));
    columnBirthday.setCellValueFactory(c->new SimpleObjectProperty<>(c.getValue().getBirthday()));
    columnPhone.setCellValueFactory(c->new SimpleObjectProperty<>(c.getValue().getPhone()));
    columnEmail.setCellValueFactory(c->new SimpleObjectProperty<>(c.getValue().getEmail()));
    columnDataReg.setCellValueFactory(c->new SimpleObjectProperty<>(c.getValue().getRegistrationDate()));
    columnDataLastVisit.setCellValueFactory(c->{
        Set<ClientService> clientServicesSet=c.getValue().getServices();
        String str="";
        if (clientServicesSet.size() !=0) {
            Date date=clientServicesSet.stream().max(Comparator.comparing(ClientService::getStartTime)).get().getStartTime();
            str=new SimpleDateFormat("dd.MM.yyyy").format(date);
        }
        return new SimpleObjectProperty<>(str);
    });
    columnSizeVisit.setCellValueFactory(c->new SimpleObjectProperty<>(c.getValue().getServices().size()));
    tableClient.setItems(clients);

    }
    @FXML
    public void pressTen(ActionEvent event){
        cutRows();
        getList(clients);
        sizeClients=clients.size();
        String str=((RadioButton)event.getSource()).getText();
        for (int i = Integer.parseInt(str); i < clients.size(); i++) {
            clients.remove(i--);
        }
        sizeList=clients.size();
        status.setText(String.format("%d из %d",sizeList,sizeClients));
    }
    @FXML
    public void pressFifty(ActionEvent event){
        cutRows();
        getList(clients);
        sizeClients=clients.size();
        String str=((RadioButton)event.getSource()).getText();
        for (int i = Integer.parseInt(str); i < clients.size(); i++) {
            clients.remove(i--);
        }
        sizeList=clients.size();
        status.setText(String.format("%d из %d",sizeList,sizeClients));

    }
    @FXML
    public void pressAll(ActionEvent event){
        cutRows();
        getList(clients);
        status.setText(String.format("%d из %d",clients.size(),clients.size()));

    }
    @FXML
    public void openAddClient(ActionEvent event) throws IOException {
        buttonOpenAdd.getScene().getWindow().hide();
        Parent root= FXMLLoader.load(getClass().getResource("/view/addClient.fxml"));
        Stage stage=new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Добавление клиента");
        stage.show();
    }

    private static void getList(ObservableList<Client> clients){
        SessionFactory factory=new Configuration().configure().buildSessionFactory();
        Dao<Client, Integer> clientDao=new ClientDaoImpl(factory);
        clients.addAll(clientDao.readByAll());
        factory.close();
    }
    private void cutRows() {
        for (int i = 0; i < clients.size(); i++) {
            clients.remove(i--);
        }
    }

}
