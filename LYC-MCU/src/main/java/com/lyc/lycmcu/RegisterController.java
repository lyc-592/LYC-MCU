package com.lyc.lycmcu;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import java.security.MessageDigest;

public class RegisterController implements Initializable {

    @FXML
    private TextField userfield;
    @FXML
    private PasswordField passfield, confirmpassfield;
    @FXML
    private Button registerbtn, closebtn, minimisebtn;
    @FXML
    private Label errorlabel, back;
    @FXML
    private ProgressBar progressbar;
    @FXML
    private AnchorPane mainregisterpane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorlabel.setVisible(false);
        back.setOnMouseClicked(event -> loadLogin());

        customiseWindowButtons();
    }

    @FXML
    public void registerUser(ActionEvent event) {
        String username = userfield.getText();
        String password = passfield.getText();
        String confirmPassword = confirmpassfield.getText();

        // 检查用户名是否为空
        if (username.isEmpty()) {
            showError("                      请输入用户名");
            return;
        }

        // 检查密码是否为空
        if (password.isEmpty()) {
            showError("                      请输入密码");
            return;
        }

        if (usernameExists(username)) {
            showError("                      用户名已存在");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("                     两次密码不一致");
            return;
        }

        saveUser(username, password);
        loadLogin();
    }

    private boolean usernameExists(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:AccountInformation.db");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // 如果存在，返回 true
        } catch (SQLException e) {
            e.printStackTrace();
            showError("数据库连接失败");
        }
        return false;
    }

    private void saveUser(String username, String password) {
        String hashedPassword = hashPassword(password);
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:AccountInformation.db");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            pstmt.executeUpdate();
            System.out.println("用户注册成功。");
        } catch (SQLException e) {
            e.printStackTrace();
            showError("注册失败：" + e.getMessage());
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(password.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showError(String message) {
        errorlabel.setText(message);
        errorlabel.setVisible(true);
    }

    private void loadLogin() {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage stage = (Stage) userfield.getScene().getWindow();
            stage.setScene(new Scene(parent));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 最小化窗口的方法
     */
    @FXML
    public void minimiseWindow(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setIconified(true);
    }

    /**
     * 关闭窗口的方法
     */
    @FXML
    public void handleClose(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    /**
     * 自定义窗口按钮的行为
     */
    private void customiseWindowButtons() {
        closebtn.setOnAction(this::handleClose);
        minimisebtn.setOnAction(this::minimiseWindow);
    }

    /**
     * 使窗口可拖动。
     */
    private void moveWindow() {
        // 使窗口可拖动
        mainregisterpane.setOnMousePressed(event -> {
            // 记录鼠标按下时的坐标
            mainregisterpane.setUserData(new double[]{event.getSceneX(), event.getSceneY()});
        });

        mainregisterpane.setOnMouseDragged(event -> {
            // 获取鼠标按下时的坐标
            double[] coords = (double[]) mainregisterpane.getUserData();
            double offsetX = event.getScreenX() - coords[0];
            double offsetY = event.getScreenY() - coords[1];

            // 移动窗口
            Stage stage = (Stage) mainregisterpane.getScene().getWindow();
            stage.setX(offsetX);
            stage.setY(offsetY);
        });
    }

}