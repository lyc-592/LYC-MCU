package com.lyc.lycmcu;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.StageStyle;

import java.io.IOException;

public class LogoutController {

    @FXML
    private Button yesbtn;

    @FXML
    private Button nobtn;

    @FXML
    private Label messageLabel;

    private Stage homeStage;    // 用于保存 Home 窗口的 Stage
    private Stage logoutStage;  // 用于保存 Logout 窗口的 Stage

    // 设置 Home 的 Stage
    public void setHomeStage(Stage stage) {
        this.homeStage = stage;
    }

    // 设置 Logout 的 Stage
    public void setLogoutStage(Stage stage) {
        this.logoutStage = stage;
    }

    @FXML
    public void handleYesButton() {
        // 如果用户点击"是"，关闭 home.fxml 和 logout.fxml 窗口，然后返回登录页面
        if (homeStage != null) {
            homeStage.close();
        }

        if (logoutStage != null) {
            logoutStage.close();
        }

        // 加载登录页面
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage loginStage = new Stage();
            loginStage.setScene(scene);
            loginStage.setTitle("Login");
            loginStage.initStyle(StageStyle.UNDECORATED);
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleNoButton() {
        // 用户点击"否"时，只关闭 logout.fxml 窗口
        if (logoutStage != null) {
            logoutStage.close();
        }
    }

    @FXML
    public void initialize() {
        // 初始化代码（如果需要）
    }
}
