package com.lyc.lycmcu;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.Bloom;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import java.security.MessageDigest;

public class LoginController implements Initializable {

    @FXML
    private TextField userfield;

    @FXML
    private ProgressBar progressbar;

    @FXML
    private PasswordField passfield;

    @FXML
    private Button loginbtn, closebtn, minimisebtn;

    @FXML
    private AnchorPane mainloginpane;

    @FXML
    private StackPane stackpane;

    @FXML
    private Label errorlabel, forgotpassword, register;


    private boolean loginsuccess, vanished;
    private boolean disablelogin = true;
    private boolean loggedout = false;
    private String username;

    /**
     * 初始化方法，在加载 login.fxml 时运行。
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorlabel.setVisible(false);

        if (loggedout) {
            errorlabel.setText("           Logged out successfully");
            errorlabel.setVisible(true);
            FadeTransition ft = new FadeTransition(Duration.millis(1200), errorlabel);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
            ft.setOnFinished(event -> {
                FadeTransition f = new FadeTransition(Duration.millis(1200), errorlabel);
                f.setFromValue(1);
                f.setToValue(0);
                f.play();
                f.setOnFinished(event1 -> {
                    errorlabel.setText("Error! Incorrect Password Or Username");
                    errorlabel.setVisible(false);
                });
            });
            loggedout = false;
        }

        customiseWindowButtons();
        loginbtn.setOnMouseEntered(e -> {
            loginbtn.setStyle("-fx-background-color: #FF9A00;");
            loginbtn.setEffect(new Bloom(0.85));
        });
        loginbtn.setOnMouseExited(e -> {
            loginbtn.setStyle("-fx-background-color:  #FF5722;");
            loginbtn.setEffect(new Bloom(1));
        });

        forgotpassword.setOnMouseClicked(event -> showDialog());
        register.setOnMouseClicked(event -> goToRegister());
        moveWindow();

        userfield.setOnKeyPressed(this::fieldListeners);
        passfield.setOnKeyPressed(this::fieldListeners);
    }

    /**
     * 显示错误信息的方法。
     */
    private void showError(String message) {
        Platform.runLater(() -> {
            errorlabel.setText(message);
            errorlabel.setVisible(true);
            FadeTransition ft = new FadeTransition(Duration.millis(500), errorlabel);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
        });
    }

    /**
     * 显示“忘记密码”对话框。
     */
    public void showDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Forgot Password");
        alert.setHeaderText(null);
        alert.setContentText("It looks like you forgot your username or password,\n" +
                "if so then no need to worry. Simply please contact \n" +
                "me directly or send me an email and I'll be \n" +
                "happy to help.\n\nThanks\nLYC\nMy Email:1036133125@qq.com");

        alert.initOwner(stackpane.getScene().getWindow());
        alert.showAndWait();
    }

    /**
     * 处理登录按钮点击事件。
     */
    @FXML
    public void staffLogin(ActionEvent event) {
        if (disablelogin) {
            Timeline timeline = animateLogin();
            timeline.play();
            timeline.setOnFinished(e -> {
                if (loginsuccess) {
                    Node source = (Node) event.getSource();
                    Stage stage = (Stage) source.getScene().getWindow();
                    stage.close();
                    loginsuccess = false;
                    username = userfield.getText();
                    loadHome();
                } else {
                    rotateButton(loginbtn);
                    Timeline timeline2 = new Timeline();
                    timeline2.setCycleCount(1);
                    timeline2.getKeyFrames().add(new KeyFrame(Duration.millis(300),
                            new KeyValue(progressbar.translateYProperty(), 0)));
                    timeline2.getKeyFrames().add(new KeyFrame(Duration.millis(200),
                            new KeyValue(loginbtn.translateYProperty(), 0)));
                    timeline2.play();

                    timeline2.setOnFinished(event1 -> {
                        errorlabel.setVisible(true);
                        vanished = false;
                        errorlabel.setOpacity(0);
                        FadeTransition ft = new FadeTransition(Duration.millis(500), errorlabel);
                        ft.setFromValue(0);
                        ft.setToValue(1);
                        ft.play();
                        progressbar.setVisible(false);
                        disablelogin = true;
                    });
                }
            });
        }
    }

    /**
     * 最小化窗口的方法。
     */
    @FXML
    public void minimiseWindow(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setIconified(true);
    }

    /**
     * 关闭窗口的方法。
     */
    @FXML
    public void handleClose(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    /**
     * 登录动画效果。
     */
    private Timeline animateLogin() {
        disablelogin = false;
        progressbar.setVisible(true);
        errorlabel.setVisible(false);
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);

        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(300),
                new KeyValue(progressbar.translateYProperty(), -70)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(200),
                new KeyValue(loginbtn.translateYProperty(), -30)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2400)));

        Thread t = new Thread(() -> {
            String username = userfield.getText();
            String password = passfield.getText();
            loginsuccess = checkUser(username, password);
        });
        t.start();
        return timeline;
    }

    /**
     * 加载主页。
     */
    private void loadHome() {
        disablelogin = true;
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("home.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(parent, 1200, 700);
            ScrollStyleManager.applyStyle(scene);
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (IOException ex) {
            System.out.println("Error in switching stages");
            ex.printStackTrace();
        }
    }

    /**
     * 检查用户凭据。
     */
    private boolean checkUser(String username, String password) {
        String hashedPassword = hashPassword(password);
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:AccountInformation.db");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // 如果存在，返回 true
        } catch (SQLException e) {
            showError("数据库连接失败：" + e.getMessage());
            e.printStackTrace();
        }
        return false;
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

    /**
     * 处理文本字段的键盘事件。
     */
    private void fieldListeners(KeyEvent event) {
        FadeTransition ft = new FadeTransition(Duration.millis(500), errorlabel);
        if (event.getCode() == KeyCode.ENTER) {
            loginbtn.fire();
        } else if (errorlabel.isVisible() && !vanished) {
            vanished = true;
            ft.setFromValue(1);
            ft.setToValue(0);
            ft.play();
            ft.setCycleCount(1);
            ft.setOnFinished(event1 -> errorlabel.setVisible(false));
        }
    }

    /**
     * 自定义窗口按钮的行为。
     */
    private void customiseWindowButtons() {
        closebtn.setOnAction(this::handleClose);
        minimisebtn.setOnAction(this::minimiseWindow);
    }

    /**
     * 跳转到注册页面。
     */
    public void goToRegister() {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("register.fxml"));
            Stage stage = (Stage) stackpane.getScene().getWindow();
            stage.setScene(new Scene(parent));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 旋转按钮的动画效果。
     */
    private void rotateButton(Button button) {
        RotateTransition rt = new RotateTransition(Duration.millis(100), button);
        rt.setByAngle(10);
        rt.setCycleCount(6);
        rt.setAutoReverse(true);
        rt.play();
    }

    /**
     * 使窗口可拖动。
     */
    private void moveWindow() {
        // 使窗口可拖动
        mainloginpane.setOnMousePressed(event -> {
            // 记录鼠标按下时的坐标
            mainloginpane.setUserData(new double[]{event.getSceneX(), event.getSceneY()});
        });

        mainloginpane.setOnMouseDragged(event -> {
            // 获取鼠标按下时的坐标
            double[] coords = (double[]) mainloginpane.getUserData();
            double offsetX = event.getScreenX() - coords[0];
            double offsetY = event.getScreenY() - coords[1];

            // 移动窗口
            Stage stage = (Stage) mainloginpane.getScene().getWindow();
            stage.setX(offsetX);
            stage.setY(offsetY);
        });
    }
}