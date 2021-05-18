package ru.sapteh.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.apache.poi.ss.usermodel.Cell;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookType;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.sapteh.dao.Dao;
import ru.sapteh.model.Client;
import ru.sapteh.model.ClientService;
import ru.sapteh.model.Gender;
import ru.sapteh.service.ClientDaoImpl;
import ru.sapteh.service.GenderService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class MainController {

    private final ObservableList<Client> clients= FXCollections.observableArrayList();
    ObservableList<Character> genders=FXCollections.observableArrayList('м', 'ж');
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
    private TextField txtSearch;
    @FXML
    private ComboBox<Integer> comboPaged;
    @FXML
    private Pagination pagination;
    @FXML
    private Button buttonOpenAdd;
    @FXML
    private Button openRegUser;
    @FXML
    private ComboBox<String> comboFilter;
    @FXML
    private ComboBox<Character> comboGender;
    @FXML
    private Button openUpdateClient;
    @FXML
    private Button buttonDelete;

    private int sizeClients;
    private int comboBoxValue;

    public static int idClient;
    public static String firstName;
    public static String lastName;
    public static String patronymic;
    public static String phone;
    public static String email;
    public static Date birthday;
    public static char gender;
    public static Client client;

    @FXML
    public void initialize(){
        if (LoginWindowController.role.equals("user")){
            buttonOpenAdd.setVisible(false);
            openRegUser.setVisible(false);
        }
        getList(clients);
        initializeTableView();
        sortGender();
        searchClient(clients);
        pageTableView();
        sortGender();
        clientShowDetails(null);
        tableClient.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                        clientShowDetails(newValue);

        });
        deleteClient();
        buttonDelete.setOnAction(event -> {
            try {
                Parent root=FXMLLoader.load(getClass().getResource("/view/warrior.fxml"));
                Stage stage=new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("WARRIOR");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }
    @FXML
    public void pressUpdateClient() throws IOException {
        openUpdateClient.getScene().getWindow().hide();
        Parent parent=FXMLLoader.load(getClass().getResource("/view/updateClient.fxml"));
        Stage stage=new Stage();
        stage.setTitle("Изменение клиета");
        stage.setScene(new Scene(parent));
        stage.show();
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
    @FXML
    public void pressRegUsers(ActionEvent event) throws IOException {
        openRegUser.getScene().getWindow().hide();
        Parent root=FXMLLoader.load(getClass().getResource("/view/addUsers.fxml"));
        Stage stage=new Stage();
        stage.setTitle("Регистрация пользователя");
        stage.setScene(new Scene(root));
        stage.show();
    }
    @FXML
    public void pressSaveExcel(ActionEvent event) throws IOException {
        FileChooser fileChooser=new FileChooser();
        fileChooser.setTitle("Выберите путь");
        File file=fileChooser.showSaveDialog(new Stage());
        String fileName= file.getAbsolutePath();
        XSSFWorkbook workbook = new XSSFWorkbook(XSSFWorkbookType.XLSX);

        Sheet sheet = workbook.createSheet("Client");
        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short)14);
        headerStyle.setFont(font);

        //Header cell
        ObservableList<TableColumn<Client, ?>> columns = tableClient.getColumns();
        int count = 0;
        for(TableColumn<Client, ?> column : columns){
            Cell headerCell = header.createCell(count++);
            headerCell.setCellValue(column.getText());
            headerCell.setCellStyle(headerStyle);
        }

        //Next cell (tableViewClient.getItems())
        for (int i = 0; i < tableClient.getItems().size(); i++) {
            Row row=sheet.createRow(i+1);
            Cell id = row.createCell(0);
            Cell gender = row.createCell(1);
            Cell lastName = row.createCell(2);
            Cell firstName = row.createCell(3);
            Cell patronymic = row.createCell(4);
            Cell birthday = row.createCell(5);
            Cell phone = row.createCell(6);
            Cell email = row.createCell(7);
            Cell dateRegistration = row.createCell(8);
            Cell lastDateVisit = row.createCell(9);
            Cell quantityVisit = row.createCell(10);
            id.setCellValue(tableClient.getItems().get(i).getId());
            gender.setCellValue(String.valueOf(tableClient.getItems().get(i).getGender().getCode()));
            firstName.setCellValue(tableClient.getItems().get(i).getFirstName());
            lastName.setCellValue(tableClient.getItems().get(i).getLastName());
            patronymic.setCellValue(tableClient.getItems().get(i).getPatronymic());
            birthday.setCellValue(tableClient.getItems().get(i).getBirthday().toString());
            dateRegistration.setCellValue(tableClient.getItems().get(i).getRegistrationDate().toString());
            email.setCellValue(tableClient.getItems().get(i).getEmail());
            phone.setCellValue(tableClient.getItems().get(i).getPhone());
            quantityVisit.setCellValue(tableClient.getItems().get(i).getServices().size());
            Set<ClientService> services = tableClient.getItems().get(i).getServices();
            if (services.size() != 0) {
                lastDateVisit.setCellValue(services.stream().max(Comparator.comparing(ClientService::getStartTime)).get().getStartTime().toString());
            }
        }

        FileOutputStream outputStream = new FileOutputStream(fileName);
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    @FXML
    public void pressSavePDF(ActionEvent event) throws IOException, DocumentException {
        FileChooser fileChooser=new FileChooser();
        fileChooser.setTitle("Выберите путь");
        File file=fileChooser.showSaveDialog(new Stage());
        String fileName= file.getAbsolutePath();
        Document document=new Document();
        PdfWriter.getInstance(document,new FileOutputStream(fileName));
        document.open();

        Image image=Image.getInstance("C:\\JavaProgect\\AutoservicePrim2\\src\\main\\resources\\image\\logo.png");
        image.scaleAbsoluteHeight(20);
        image.scaleAbsoluteWidth(130);
        image.setAlignment(Element.ALIGN_RIGHT);
        document.add(image);
        String font = "./src/main/resources/font/ArialRegular.ttf";

        BaseFont bf = BaseFont.createFont(font, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font fontParagraph = new Font(bf,30,Font.NORMAL);
        Font fontTable=new Font(bf,12,Font.NORMAL);

        Paragraph paragraph=new Paragraph("Clients Автосервиса: ",fontParagraph);
        paragraph.setSpacingAfter(20);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);

        int numColumns=tableClient.getColumns().size();

        PdfPTable tablePdf=new PdfPTable(numColumns);
        ObservableList<TableColumn<Client, ?>> columns = tableClient.getColumns();
        columns.forEach(c->tablePdf.addCell(new PdfPCell(new Phrase(c.getText(),fontTable))));
        tablePdf.setHeaderRows(1);
        for (int i = 0; i < tableClient.getItems().size(); i++) {
            tablePdf.addCell(new PdfPCell(new Phrase(String.valueOf(tableClient.getItems().get(i).getId()),fontTable)));
            tablePdf.addCell(new PdfPCell(new Phrase(String.valueOf(tableClient.getItems().get(i).getGender().getCode()),fontTable)));
            tablePdf.addCell(new PdfPCell(new Phrase(tableClient.getItems().get(i).getLastName(),fontTable)));
            tablePdf.addCell(new PdfPCell(new Phrase(tableClient.getItems().get(i).getFirstName(),fontTable)));
            tablePdf.addCell(new PdfPCell(new Phrase(tableClient.getItems().get(i).getPatronymic(),fontTable)));
            tablePdf.addCell(new PdfPCell(new Phrase(tableClient.getItems().get(i).getBirthday().toString(),fontTable)));
            tablePdf.addCell(new PdfPCell(new Phrase(tableClient.getItems().get(i).getPhone(),fontTable)));
            tablePdf.addCell(new PdfPCell(new Phrase(tableClient.getItems().get(i).getEmail(),fontTable)));
            tablePdf.addCell(new PdfPCell(new Phrase(tableClient.getItems().get(i).getRegistrationDate().toString(),fontTable)));
            Set<ClientService> services = tableClient.getItems().get(i).getServices();
            String lastDate="";
            if (services.size()!=0){
                Date date=services.stream().max(Comparator.comparing(ClientService::getStartTime)).get().getStartTime();
               lastDate=new SimpleDateFormat("dd.MM.yyyy").format(date);
            }
            tablePdf.addCell(new PdfPCell(new Phrase(lastDate,fontTable)));
            //tablePdf.addCell(new PdfPCell(new Phrase(tableClient.getItems().get(i).getServices().stream().max(Comparator.comparing(ClientService::getStartTime)).get().getStartTime().toString(),fontTable)));
            tablePdf.addCell(new PdfPCell(new Phrase(String.valueOf(tableClient.getItems().get(i).getServices().size()),fontTable)));

        }
        document.add(tablePdf);
        document.close();




    }
    private void pageTableView(){
        sizeClients=clients.size();
        ObservableList<Integer> options=FXCollections.observableArrayList(10,20,50,200);
        comboPaged.setItems(options);
        comboPaged.setValue(options.get(0));
        comboPaged.valueProperty().addListener((obj,oldValue,newValue)-> {
            comboBoxValue = comboPaged.getValue();
            if (comboBoxValue>sizeClients){
                comboBoxValue=sizeClients;
                newValue=sizeClients;
            }
            int page=(int) Math.ceil(sizeClients * 1.0/comboBoxValue);
            pagination.setPageCount(page);
            pagination.setCurrentPageIndex(0);
            int clientPage=FXCollections.observableArrayList(clients.subList(pagination.getCurrentPageIndex(),newValue)).size();
            tableClient.setItems(FXCollections.observableArrayList(clients.subList(pagination.getCurrentPageIndex(),newValue)));
            status.setText(String.format("кол-во записей %d из %d",clientPage,sizeClients));
            pagination.currentPageIndexProperty().addListener((obj1,oldValue1,newValue1)->{
                try {
                    tableClient.setItems(FXCollections.observableArrayList(clients.subList(comboBoxValue * (newValue1.intValue() + 1) - comboBoxValue,
                            comboBoxValue * (newValue1.intValue() + 1))));
                }catch (IndexOutOfBoundsException exception){
                    tableClient.setItems(FXCollections.observableArrayList(clients.subList(comboBoxValue * (newValue1.intValue() + 1) - comboBoxValue,
                            sizeClients)));
                }
            });
        });
    }
    private static void getList(ObservableList<Client> clients){
        SessionFactory factory=new Configuration().configure().buildSessionFactory();
        Dao<Client, Integer> clientDao=new ClientDaoImpl(factory);
        clients.addAll(clientDao.readByAll());
        factory.close();
    }
    private void initializeTableView(){
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
    public void searchClient(ObservableList<Client> clients){
        FilteredList<Client> filteredList=new FilteredList<>(clients,p->true);
        comboFilter.setItems(FXCollections.observableArrayList("FIO","phone","email"));
        txtSearch.textProperty().addListener((obj,oldValue,newValue)->{
            filteredList.setPredicate(client -> {
                if (newValue==null||newValue.isEmpty()){
                    return true;
                }
                String lowerCase=newValue.toLowerCase();
                if (comboFilter.getValue().equals("FIO")){
                    if (client.getFirstName().toLowerCase().contains(lowerCase)){
                        return true;
                    }
                    if (client.getLastName().toLowerCase().contains(lowerCase)){
                        return true;
                    }
                    if (client.getPatronymic().toLowerCase().contains(lowerCase)){
                        return true;
                    }
                }
                if (comboFilter.getValue().equals("phone")){
                    if (client.getPhone().toLowerCase().contains(lowerCase)){
                        return true;
                    }
                }
                if (comboFilter.getValue().equals("email")){
                    if (client.getEmail().toLowerCase().contains(lowerCase)){
                        return true;
                    }
                }
                return false;
            });
            tableClient.setItems(filteredList);
        });
    }

    private void sortGender(){
        comboGender.setItems(genders);
        comboGender.valueProperty().addListener(
                (obj, oldValue, newValue) -> {
                    FilteredList<Client> filteredList = new FilteredList<>(
                            clients, s -> newValue.equals(s.getGender().getCode()));
                    tableClient.setItems(filteredList);
                }
        );
    }
    private void clientShowDetails(Client client){
        if (client!=null){
            idClient=client.getId();
            firstName=client.getFirstName();
            UpdateClientController.client.setFirstName(firstName);
            lastName=client.getLastName();
            UpdateClientController.client.setLastName(lastName);
            patronymic=client.getPatronymic();
            UpdateClientController.client.setPatronymic(patronymic);
            phone=client.getPhone();
            UpdateClientController.client.setPhone(phone);
            email=client.getEmail();
            UpdateClientController.client.setEmail(email);
            UpdateClientController.client.setGender(client.getGender());
            UpdateClientController.client.setBirthday(client.getBirthday());
            UpdateClientController.client.setRegistrationDate(client.getRegistrationDate());
            UpdateClientController.client.setPhotoPath(client.getPhotoPath());
        }else
            idClient=0;
         firstName="";
         lastName="";
         patronymic="";
         phone="";
         email="";
         birthday=null;
        gender=0;

    }
    private void deleteClient(){
        tableClient.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldValue, newValue) -> {
            client=newValue;
        }));
    }


}
