package com.example.assh;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.net.URL;
import java.sql.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;


public class LoginController implements Initializable {

    public TextField user;
    public TextField pass;
    public Label superlabel;
    public Label label;
    dbconnection con=new dbconnection();
    Connection connect=con.getConnection();
    @FXML
    private TextField admin_id;
    @FXML
    private TextField admin_role;
    @FXML
    private TextField admin_name;
    @FXML
    private TextField admin_user_name;
    @FXML
    private TextField admin_password;
    @FXML
    private Label iderror;

    @FXML
    private Label nameerror;

    @FXML
    private Label passError;

    @FXML
    private Label roleerror;

    @FXML
    private Label userNameerr;

    @FXML
    private TableView<Account> admin_table=new TableView<>();



    @FXML
    private TableColumn<Account, String> id_col=new TableColumn<>();

    @FXML
    private TableColumn<Account, String> password_col=new TableColumn<>();

    @FXML
    private TableColumn<Account, String> user_name_col=new TableColumn<>();
    boolean t=false;
    ObservableList<Account> data=FXCollections.observableArrayList();
    @FXML
    private TextField search;
    private String userId;
    public LoginController() throws SQLException, ClassNotFoundException {
    }
    @FXML
    void Admin_scene(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("s_admin.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        HelloApplication.stage.setTitle("ADMIN PAGE");
        HelloApplication.stage.setScene(scene);
        HelloApplication.stage.show();
    }

    @FXML
    void Back(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        HelloApplication.stage.setTitle("LOGIN PAGE ");
        HelloApplication.stage.setScene(scene);
        HelloApplication.stage.show();
    }

    @FXML
    void Employee_scene(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("s_employee.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        HelloApplication.stage.setTitle("EMPLOYEE PAGE");
        HelloApplication.stage.setScene(scene);
        HelloApplication.stage.show();
    }






    public void login(ActionEvent event) throws IOException, SQLException {
        if(pass.getText().isBlank()){
            userNameerr.setText("userName is required");

        }


        if (pass.getText().isBlank()){
            passError.setText("Password is Required");

        }
        if (!pass.getText().isBlank() && pass.getText().length()<4){
            passError.setText("Password should contain at least 4 words");

        }


        if(!user.getText().isBlank()){
            userNameerr.setText("");

        }



        if (!pass.getText().isBlank() && pass.getText().length()>4){
            passError.setText("");

        }
        String sql= "SELECT COUNT(1) FROM account where password=? and user_name=?";//check whether there is user based on the name and password
        PreparedStatement pst= connect.prepareStatement(sql);
        pst.setString(1,pass.getText());
        pst.setString(2,user.getText());
        ResultSet rs=pst.executeQuery();
        String sql2="select *from account where role=?"; //to check whether superadmin or not
        PreparedStatement pst4=connect.prepareStatement(sql2);
        pst4.setString(1,"superadmin");
        ResultSet re1=pst4.executeQuery();
        boolean superadmin=false;
        while (re1.next()){
            if (re1.getString("role").equals("superadmin")){
                superadmin=true;
                System.out.println(re1.getString("role").equals("superadmin"));
            }
        }
        if (superadmin) {
            while (rs.next()) {
                if (rs.getInt(1) == 1) {
                    String sql1 = "SELECT * FROM account where password=? and user_name=?";
                    PreparedStatement pst1 = connect.prepareStatement(sql1);
                    pst1.setString(1, pass.getText());
                    pst1.setString(2, user.getText());
                    ResultSet rs1 = pst1.executeQuery();
                    while (rs1.next()) {
                        userId=rs1.getString("id");
                        if (rs1.getString("role").equals("superadmin")) {
                            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("superadmin.fxml"));
                            Scene scene = new Scene(fxmlLoader.load());
                            HelloApplication.stage.setTitle("SUPER ADMIN MANAGEMENT PAGE");
                            HelloApplication.stage.setScene(scene);
                            HelloApplication.stage.show();
                        } else if (rs1.getString("role").equals("admin")) {
                            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("admin.fxml"));
                            Scene scene = new Scene(fxmlLoader.load());
                            HelloApplication.stage.setTitle("admin page");
                            HelloApplication.stage.setScene(scene);
                            HelloApplication.stage.show();
                        } else if (rs1.getString("role").equals("employee")) {
                            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("employee.fxml"));
                            Scene scene = new Scene(fxmlLoader.load());
                            HelloApplication.stage.setTitle("employee page");
                            HelloApplication.stage.setScene(scene);
                            HelloApplication.stage.show();
                        } else if (rs1.getString("role").equals("customer")) {
                            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("customer.fxml"));
                            Scene scene = new Scene(fxmlLoader.load());
                            HelloApplication.stage.setTitle("Hello!");
                            HelloApplication.stage.setScene(scene);
                            HelloApplication.stage.show();
                        }
                        BufferedWriter writer=new BufferedWriter(new FileWriter("account.txt"));
                        writer.write(rs1.getString("id"));
                        writer.close();
                    }
                    Date now = new Date(System.currentTimeMillis());
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                    String formattedDate = formatter.format(now);
                    System.out.println("logout "+readLogedAccount());
                    String idd=readLogedAccount();//write id of logged in
                    String id= idd.trim();// to remove space
                    System.out.println("iddd  "+id);
                    System.out.println("e=id  "+id.equals("e"));
                    String sqltr= "INSERT INTO information (info_ID,Event,Time) VALUES(?,?,?)";
                    PreparedStatement preptr = connect.prepareStatement(sqltr);
                    preptr.setString(1, userId);
                    preptr.setString(2, formattedDate);
                    preptr.setString(3, "login");
                    preptr.executeUpdate();

                }

            }
        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("You don't have a super Admin ");
            alert.setContentText("please create super admin first !!!");
            alert.showAndWait();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("superregister.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            HelloApplication.stage.setTitle("Create Account!");
            HelloApplication.stage.setScene(scene);

        }



write();// write information and information text of transaction


    }


    public void create(ActionEvent event) throws SQLException, IOException {
        boolean n,d,p,e,i,r,na;
        e=n=p=d=i=r=na=true;
        if(admin_user_name.getText().isBlank()){
            e=false;
        }


        if (admin_password.getText().isBlank()){
            p=false;

        }
        if(admin_id.getText().isBlank()){
            i=false;
        }



        if (admin_name.getText().isBlank()){
            na = false;

        }
        if (!admin_password.getText().isBlank() && admin_password.getText().length()<4){
            p=false;

        }
        if(!admin_user_name.getText().isBlank()){
            e=true;

        }


        if(!admin_user_name.getText().isBlank()){
            e=true;
        }



        if (!admin_password.getText().isBlank() && admin_password.getText().length()>4){
            p=true;

        }
        if (p && e && na && i ) {
            String sql = "insert into account (id,user_name,password,role) values(?,?,?,?)";
            PreparedStatement pt = connect.prepareStatement(sql);
            pt.setString(1, admin_id.getText());
            pt.setString(2, admin_user_name.getText());
            pt.setString(3, admin_password.getText());
            pt.setString(4, admin_role.getText());
            pt.executeUpdate();
            String sql11 = "INSERT INTO user (Name,id) VALUES(?,?)";
            PreparedStatement prep11 = connect.prepareStatement(sql11);
            prep11.setString(1, admin_name.getText());
            prep11.setString(2, admin_id.getText());
            prep11.executeUpdate();
            Date now = new Date(System.currentTimeMillis());

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            String formattedDate = formatter.format(now);
            System.out.println("logout "+readLogedAccount());
            String idd=readLogedAccount();
            String id= idd.trim();
            System.out.println("iddd  "+id);
            System.out.println("e=id  "+id.equals("e"));
            String sqltr= "INSERT INTO information (info_ID,Event,Time) VALUES(?,?,?)";
            PreparedStatement preptr = connect.prepareStatement(sqltr);
            preptr.setString(1, admin_id.getText());
            preptr.setString(2, formattedDate);
            preptr.setString(3, "Create account");
            preptr.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("create account page");
            alert.setContentText("you have created successfully ");
            alert.showAndWait();
            admin_name.clear();
            admin_id.clear();
            admin_password.clear();
            admin_user_name.clear();
            admin_role.clear();
        }
        write();
    }

    public void createSuper(ActionEvent event) throws SQLException, IOException {
        boolean n,d,p,e,i,r,na;
        e=n=p=d=i=r=na=true;

        if(admin_user_name.getText().isBlank()){
            userNameerr.setText("userName is required");
            e=false;
        }
        if(admin_role.getText().isBlank()){
            roleerror.setText("Role is required");
            r=false;
        }



        if (admin_password.getText().isBlank()){
            p=false;
            passError.setText("Password is Required");

        }
        if(admin_id.getText().isBlank()){
            iderror.setText("Id is required");
            i=false;
        }



        if (admin_name.getText().isBlank()){
             na = false;
            nameerror.setText("Name is Required");

        }
        if (!admin_password.getText().isBlank() && admin_password.getText().length()<4){
            p=false;
            passError.setText("Password should contain at least 8 words");

        }
        if(!admin_user_name.getText().isBlank()){
            userNameerr.setText("");
            e=true;

        }
        if(!admin_role.getText().isBlank()){
            roleerror.setText("");
            r=true;

        }
        if(!admin_id.getText().isBlank()){
            iderror.setText("");
            i=true;

        }


        if(!admin_name.getText().isBlank()){
            nameerror.setText("");
            na=true;
        }



        if (!admin_password.getText().isBlank() && admin_password.getText().length()>4){
            p=true;
            passError.setText("");

        }
        if (n && p && e  && d && na && i && r ) {
            String sql = "insert into account (id,user_name,password,role) values(?,?,?,?)";
            PreparedStatement pt = connect.prepareStatement(sql);
            pt.setString(1, admin_id.getText());
            pt.setString(2, admin_user_name.getText());
            pt.setString(3, admin_password.getText());
            pt.setString(4, admin_role.getText());
            pt.executeUpdate();
            String sql11 = "INSERT INTO user (Name,id) VALUES(?,?)";
            PreparedStatement prep11 = connect.prepareStatement(sql11);
            prep11.setString(1, admin_name.getText());
            prep11.setString(2, admin_id.getText());
            prep11.executeUpdate();
            Date now = new Date(System.currentTimeMillis());

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            String formattedDate = formatter.format(now);
            System.out.println("logout "+readLogedAccount());
            String idd=readLogedAccount();
            String id= idd.trim();
            System.out.println("iddd  "+id);
            System.out.println("e=id  "+id.equals("e"));
            String sqltr= "INSERT INTO information (info_ID,Event,Time) VALUES(?,?,?)";
            PreparedStatement preptr = connect.prepareStatement(sqltr);
            preptr.setString(1, admin_id.getText());
            preptr.setString(2, formattedDate);
            preptr.setString(3, "Create account");
            preptr.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Login page");
            alert.setContentText("you successfully login");
            alert.showAndWait();
            admin_name.clear();
            admin_id.clear();
            admin_password.clear();
            admin_user_name.clear();
            admin_role.clear();
        }
        write();
    }

    public  void update(ActionEvent event) throws SQLException {
        String sql="UPDATE account SET password=?,user_name=?,id=? where user_name='"+ admin_user_name.getText()+"'";

        PreparedStatement prep = connect.prepareStatement(sql);
        prep.setString(1, admin_password.getText());
        prep.setString(2, admin_user_name.getText());
        prep.setString(3, admin_id.getText());
        prep.executeUpdate();
        admin_id.setText("");
        admin_password.setText("");
        admin_user_name.setText("");
        admin_name.setText("");
        admin_role.setText("");
    }

    public  void delete(ActionEvent event) throws SQLException {
        String sql="delete from account where user_name=?";

        PreparedStatement prep = connect.prepareStatement(sql);
        prep.setString(1, admin_user_name.getText());
        prep.executeUpdate();
        admin_id.setText("");
        admin_password.setText("");
        admin_user_name.setText("");
        admin_name.setText("");
        admin_role.clear();
    }

    public  void search(ActionEvent event) throws SQLException {
        String sql="select *from account where user_name=?";
        PreparedStatement stmt=connect.prepareStatement(sql);
        stmt.setString(1,search.getText());
        ResultSet re=stmt.executeQuery();
        while (re.next()){
            admin_user_name.setText(re.getString("user_name"));
            admin_password.setText(re.getString("password"));
            admin_id.setText(re.getString("id"));
            admin_name.setText(re.getString("role"));
            admin_role.setText(re.getString("role"));
        }
    }

    public void Back_super(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("superadmin.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        HelloApplication.stage.setTitle("SUPER ADMIN MANAGEMENT PAGE");
        HelloApplication.stage.setScene(scene);
        HelloApplication.stage.show();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        id_col.setCellValueFactory(new PropertyValueFactory<>("id"));
        password_col.setCellValueFactory(new PropertyValueFactory<>("password"));
        user_name_col.setCellValueFactory(new PropertyValueFactory<>("username"));


    }
    @FXML
    void refresh(ActionEvent event) throws SQLException, IOException {
        String sql="select *from account";
        PreparedStatement pt=connect.prepareStatement(sql);
        ResultSet res=pt.executeQuery();
        data.clear();
        while (res.next()){

                data.add(new Account(res.getString("user_name"), res.getString("password"), res.getString("id")));
                admin_table.setItems(data);

            admin_table.setItems(data);
            FileWriter file=new FileWriter("C:\\Users\\Beki\\Downloads\\Compressed\\StudentApp-master\\ASSh\\src\\main\\java\\com\\example\\assh\\write.txt");
            file.write(res.getString("id"));
            file.write(res.getString("user_name"));

//
        }
        t=true;

    }


    public void userRegister(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("userregister.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        HelloApplication.stage.setTitle("create account");
        HelloApplication.stage.setScene(scene);
        HelloApplication.stage.show();
    }


    public void Back_super_B(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        HelloApplication.stage.setTitle("LOGIN PAGE");
        HelloApplication.stage.setScene(scene);
        HelloApplication.stage.show();
    }
    public  void readfile(ActionEvent event) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Beki\\Downloads\\Compressed\\StudentApp-master\\ASSh\\information.txt"));
            String line;
            System.out.println("read");
            StringBuilder content = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
                System.out.println("re2");
            }

            System.out.println(content);
            reader.close();
            label.setText(content.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public  void readfileSuper(ActionEvent event) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Beki\\Downloads\\Compressed\\StudentApp-master\\ASSh\\informationALL.txt"));
            String line;
            StringBuilder content = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();
            superlabel.setText(content.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String readLogedAccount() {
        String line = "logout";
        StringBuilder content = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Beki\\Downloads\\Compressed\\StudentApp-master\\ASSh\\account.txt"));
            System.out.println("read");
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
                System.out.println("re2");
            }
            line= String.valueOf(content);
            line=line.trim();
            reader.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }

    public  void write() throws SQLException, IOException {
        String sqlw = "SELECT user.Name, user.id, account.user_name, account.password, account.role, information.Event, information.Time \n" +
                "FROM user \n" +
                "JOIN account ON user.id = account.id \n" +
                "JOIN information ON information.info_ID = account.id ";
        PreparedStatement pt = connect.prepareStatement(sqlw);
        ResultSet res0 = pt.executeQuery();
        String sqlwo = "SELECT *FROM user JOIN account ON user.id  = account.id;";
        PreparedStatement pto = connect.prepareStatement(sqlwo);
        ResultSet res1 = pto.executeQuery();
        BufferedWriter writer=new BufferedWriter(new FileWriter("output.txt"));
        BufferedWriter writetran=new BufferedWriter(new FileWriter("information.txt"));
        BufferedWriter writetranAll=new BufferedWriter(new FileWriter("informationALL.txt"));

        writer.write(String.format("%-10s %-20s  %-20s %-20s %-10s\n", "Name","ID", "Username", "Password", "Role"));
        writetran.write(String.format("%-10s %-20s %-20s %-10s\n", "Name","date","type transaction", "Role"));
        writetranAll.write(String.format("%-10s %-20s %-20s %-10s\n", "Name","date","type transaction", "Role"));

        while (res1.next()) {
            StringBuilder tableData = new StringBuilder();
            tableData.append(String.format("%-10s %-20s %-20s %-20s %-10s\n",res1.getString("Name"), res1.getString("id"), res1.getString("user_name"), res1.getString("password"), res1.getString("role")));
            writer.write(tableData.toString());
            System.out.println(tableData);
            System.out.println(res1.getString("Name"));
            System.out.println("write");

        }
        while (res0.next()) {

            StringBuilder tableData1 = new StringBuilder();
            if (readLogedAccount().equals(res0.getString("id"))) {
                tableData1.append(String.format("%-10s %-20s %-20s %-10s\n", res0.getString("Name"), res0.getString("Event"), res0.getString("Time"), res0.getString("role")));
                writetran.write(tableData1.toString());
            }
        }
        String sqlwos ="SELECT user.Name, user.id, account.user_name, account.password, account.role, information.Event, information.Time \n" +
                "FROM user \n" +
                "JOIN account ON user.id = account.id \n" +
                "JOIN information ON information.info_ID = account.id ";
        PreparedStatement ptos = connect.prepareStatement(sqlwos);
        ResultSet r = ptos.executeQuery();
        while (r.next()) {

            StringBuilder tableData2 = new StringBuilder();
            tableData2.append(String.format("%-10s %-20s %-20s %-10s\n", r.getString("Name"), r.getString("Event"), r.getString("Time"), r.getString("role")));
            writetranAll.write(tableData2.toString());
            System.out.println(tableData2 + "output");

        }

        writer.close();
        writetran.close();
        writetranAll.close();
    }

    public void Logout(ActionEvent event) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        HelloApplication.stage.setTitle("Login Page");
        HelloApplication.stage.setScene(scene);
        Date now = new Date(System.currentTimeMillis());

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String formattedDate = formatter.format(now);
        System.out.println("logout "+readLogedAccount());
        String idd=readLogedAccount();
        String id= idd.trim();
        System.out.println("iddd  "+id);
        System.out.println("e=id  "+id.equals("e"));
        String sqltr= "INSERT INTO information (info_id,Event,Time) VALUES(?,?,?)";
        PreparedStatement preptr = connect.prepareStatement(sqltr);
        preptr.setString(1, id);
        preptr.setString(2, formattedDate);
        preptr.setString(3, "log out");
        preptr.executeUpdate();


        write();
    }


}
