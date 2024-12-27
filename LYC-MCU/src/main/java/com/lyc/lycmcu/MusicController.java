package com.lyc.lycmcu;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MusicController {

    @FXML
    private AnchorPane mainmusicpane;

    @FXML
    private Button closebtn, minimisebtn, uploadBtn;

    @FXML
    private TextArea instructionsTextArea;

    @FXML
    public void initialize() {
        customiseWindowButtons();
        moveWindow();
    }

    @FXML
    public void btnHover(MouseEvent event) {
        if (event.getSource() instanceof Button sourceButton) {
            sourceButton.setCursor(Cursor.HAND);
            sourceButton.setOpacity(1.0);
        }
    }

    @FXML
    public void btnExit(MouseEvent event) {
        if (event.getSource() instanceof Button sourceButton) {
            sourceButton.setCursor(Cursor.DEFAULT);
            sourceButton.setOpacity(0.8);
        }
    }

    @FXML
    private void logOut() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("logout.fxml"));
            AnchorPane logoutPane = loader.load();
            LogoutController logoutController = loader.getController();
            Stage homeStage = (Stage) mainmusicpane.getScene().getWindow();
            logoutController.setHomeStage(homeStage);
            Stage logoutStage = new Stage();
            logoutStage.setTitle("Log Out");
            logoutStage.setScene(new Scene(logoutPane));
            logoutStage.initModality(Modality.APPLICATION_MODAL);
            logoutController.setLogoutStage(logoutStage);
            logoutStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loadScene(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        String buttonId = sourceButton.getId();
        String fxmlPath = getFxmlPath(buttonId);

        if (fxmlPath != null) {
            Task<AnchorPane> task = new Task<AnchorPane>() {
                @Override
                protected AnchorPane call() throws Exception {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
                    return loader.load();
                }
            };

            task.setOnSucceeded(e -> {
                AnchorPane newPane = task.getValue();
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(newPane);
                currentStage.setScene(scene);
                ScrollStyleManager.applyStyle(scene);
            });

            task.setOnFailed(e -> {
                Throwable exception = task.getException();
                exception.printStackTrace();
            });

            new Thread(task).start();
        } else if ("rewardbtn".equals(buttonId)) {
            showRewardWindow(event); // 直接在这里调用
        }
    }

    private String getFxmlPath(String buttonId) {
        switch (buttonId) {
            case "homebtn": return "home.fxml";
            case "resourcesbtn": return "movieResources.fxml";
            case "rewardbtn": return null; // 不需要返回路径
            default: return null;
        }
    }

    @FXML
    public void minimiseWindow(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    public void handleClose(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private void customiseWindowButtons() {
        closebtn.setOnAction(this::handleClose);
        minimisebtn.setOnAction(this::minimiseWindow);
    }

    private void moveWindow() {
        mainmusicpane.setOnMousePressed(event -> {
            mainmusicpane.setUserData(new double[]{event.getSceneX(), event.getSceneY()});
        });

        mainmusicpane.setOnMouseDragged(event -> {
            double[] coords = (double[]) mainmusicpane.getUserData();
            double offsetX = event.getScreenX() - coords[0];
            double offsetY = event.getScreenY() - coords[1];
            Stage stage = (Stage) mainmusicpane.getScene().getWindow();
            stage.setX(offsetX);
            stage.setY(offsetY);
        });
    }

    @FXML
    public void uploadFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("UC Files", "*.uc"));
        File selectedFile = fileChooser.showOpenDialog(mainmusicpane.getScene().getWindow());

        if (selectedFile != null) {
            if (selectedFile.getName().endsWith(".uc")) {
                convertUcToMp3(selectedFile); // 直接调用转换方法
            } else {
                showAlert("请上传.uc文件！");
            }
        }
    }

    private void convertUcToMp3(File ucFile) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"));
        File mp3File = fileChooser.showSaveDialog(mainmusicpane.getScene().getWindow());

        if (mp3File != null) {
            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    try (FileInputStream fis = new FileInputStream(ucFile);
                         FileOutputStream fos = new FileOutputStream(mp3File)) {
                        int ch;
                        while ((ch = fis.read()) != -1) {
                            fos.write(ch ^ 0xa3); // XOR operation
                        }
                        System.out.println("转换成功: " + mp3File.getAbsolutePath());
                    } catch (IOException e) {
                        e.printStackTrace();
                        showAlert("转换失败: " + e.getMessage());
                    }
                    return null;
                }

                @Override
                protected void succeeded() {
                    super.succeeded();
                    showAlert("转换成功: " + mp3File.getAbsolutePath());
                }

                @Override
                protected void failed() {
                    super.failed();
                    showAlert("转换失败，请重试。");
                }
            };

            // 启动后台线程
            new Thread(task).start();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("信息");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void showRewardWindow(ActionEvent event) {
        // 创建一个新的窗口
        Stage rewardStage = new Stage();
        rewardStage.setTitle("Reward");

        // 创建一个ImageView来显示图片
        ImageView imageView = new ImageView();
        Image image = new Image(getClass().getResourceAsStream("/com/lyc/lycmcu/images/WeChatPay.jpg")); // 确保图片路径正确
        imageView.setImage(image);
        imageView.setFitWidth(400); // 设置宽度
        imageView.setFitHeight(300); // 设置高度
        imageView.setPreserveRatio(true); // 保持比例

        // 创建一个Pane来放置ImageView
        StackPane root = new StackPane();
        root.getChildren().add(imageView);

        // 创建一个Scene并设置到新窗口
        Scene scene = new Scene(root, 400, 300);
        rewardStage.setScene(scene);

        // 设置窗口可最小化和关闭
        rewardStage.initModality(Modality.APPLICATION_MODAL);
        rewardStage.show();
    }
}