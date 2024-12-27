package com.lyc.lycmcu;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.IOException;

public class HomeController {

    @FXML
    private Button resourcesbtn;

    @FXML
    private Button musicbtn;

    @FXML
    private Button mebtn;

    @FXML
    private Button rewardbtn;

    @FXML
    private Button logoutbtn;

    @FXML
    private Button closebtn, minimisebtn;

    @FXML
    private TabPane tabpane;

    @FXML
    private AnchorPane mainhomepane;

    @FXML
    private ScrollPane stage1ScrollPane;

    @FXML
    private ScrollPane stage2ScrollPane;

    @FXML
    private ScrollPane stage3ScrollPane;

    @FXML
    private ScrollPane stage4ScrollPane;

    @FXML
    private ScrollPane stage5ScrollPane;

    @FXML
    private ScrollPane stage6ScrollPane;

    @FXML
    private VBox stage1Content, stage2Content, stage3Content, stage4Content, stage5Content, stage6Content;

    @FXML
    private ImageView Ironman1Poster, HulkPoster, Ironman2Poster, Thor1Poster, CaptainAmerica1Poster, Avengers1Poster;

    @FXML
    private ImageView Ironman3Poster, Thor2Poster, CaptainAmerica2Poster, GuardiansoftheGalaxy1Poster, Avengers2Poster, Antman1Poster;

    @FXML
    private ImageView CaptainAmerica3Poster, DoctorStrange1Poster, GuardiansoftheGalaxy2Poster, Spiderman1Poster, Thor3Poster, BlackPanther1Poster;

    @FXML
    private ImageView Avengers3Poster, Antman2Poster, CaptainMarvel1Poster, Avengers4Poster, Spiderman2Poster;

    @FXML
    private ImageView BlackwidowPoster, ShangChiPoster, EternalsPoster, Spiderman3Poster, DoctorStrange2Poster, Thor4Poster, BlackPanther2Poster;

    @FXML
    private ImageView Antman3Poster, GuardiansoftheGalaxy3Poster, CaptainMarvel2Poster, Deadpool3Poster, CaptainAmerica4Poster, ThunderboltsPoster;

    @FXML
    private ImageView Avengers5Poster, Spiderman4Poster, Avengers6Poster;


    @FXML
    public void initialize() {

        customiseWindowButtons();
        moveWindow();
        setupStage1Scroll();
        setupStage2Scroll();
        setupStage3Scroll();
        setupStage4Scroll();
        setupStage5Scroll();
        setupStage6Scroll();
        loadImages();

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
            case "musicbtn": return "music.fxml";
            case "rewardbtn": return null; // 不需要返回路径
            default: return null;
        }
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

    @FXML
    private void logOut() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("logout.fxml")); // 确保路径正确
            AnchorPane logoutPane = loader.load();

            // 获取 LogoutController 的实例
            LogoutController logoutController = loader.getController();

            // 获取当前的 Home 窗口的 Stage
            Stage homeStage = (Stage) mainhomepane.getScene().getWindow();

            // 将 Home 的 Stage 传递给 LogoutController
            logoutController.setHomeStage(homeStage);

            // 创建一个新的 Stage 来显示 logout 窗口
            Stage logoutStage = new Stage();
            logoutStage.setTitle("Log Out");
//          logoutStage.initStyle(StageStyle.UNDECORATED);
            logoutStage.setScene(new Scene(logoutPane));
            logoutStage.initModality(Modality.APPLICATION_MODAL); // 设置为模态窗口

            // 将 logout 窗口的 Stage 也传递给 LogoutController，以便关闭
            logoutController.setLogoutStage(logoutStage);

            logoutStage.show();
        } catch (IOException e) {
            e.printStackTrace();
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

    @FXML
    public void btnHover() {
        // 鼠标进入按钮时的效果
        Button sourceButton = (Button) resourcesbtn.getScene().getFocusOwner();
        if (sourceButton != null) {
            sourceButton.setCursor(Cursor.HAND);
            sourceButton.setOpacity(1.0); // 可以调整透明度
        }
    }

    @FXML
    public void btnExit() {
        // 鼠标退出按钮时的效果
        Button sourceButton = (Button) resourcesbtn.getScene().getFocusOwner();
        if (sourceButton != null) {
            sourceButton.setCursor(Cursor.DEFAULT);
            sourceButton.setOpacity(0.8); // 恢复透明度
        }
    }

    /**
     * 自定义窗口按钮的行为。
     */
    private void customiseWindowButtons() {
        closebtn.setOnAction(this::handleClose);
        minimisebtn.setOnAction(this::minimiseWindow);
    }

    private void setupStage1Scroll() {
        if (stage1ScrollPane != null) {
            // 设置滚动条的样式
            stage1ScrollPane.getStyleClass().add("edge-to-edge");

            // 设置滚动条始终显示在右侧
            stage1ScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            stage1ScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            // 设置滚动速度
            stage1ScrollPane.setVvalue(0);

            // 可以添加滚动事件监听器
            stage1ScrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> {
                // 这里可以处理滚动事件
            });
        }
    }

    private void setupStage2Scroll() {
        if (stage2ScrollPane != null) {
            // 设置滚动条的样式
            stage2ScrollPane.getStyleClass().add("edge-to-edge");

            // 设置滚动条始终显示在右侧
            stage2ScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            stage2ScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            // 设置滚动速度
            stage2ScrollPane.setVvalue(0);

            // 可以添加滚动事件监听器
            stage2ScrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> {
                // 这里可以处理滚动事件
            });
        }
    }

    private void setupStage3Scroll() {
        if (stage3ScrollPane != null) {
            // 设置滚动条的样式
            stage3ScrollPane.getStyleClass().add("edge-to-edge");

            // 设置滚动条始终显示在右侧
            stage3ScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            stage3ScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            // 设置滚动速度
            stage3ScrollPane.setVvalue(0);

            // 可以添加滚动事件监听器
            stage3ScrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> {
                // 这里可以处理滚动事件
            });
        }
    }

    private void setupStage4Scroll() {
        if (stage4ScrollPane != null) {
            // 设置滚动条的样式
            stage4ScrollPane.getStyleClass().add("edge-to-edge");

            // 设置滚动条始终显示在右侧
            stage4ScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            stage4ScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            // 设置滚动速度
            stage4ScrollPane.setVvalue(0);

            // 可以添加滚动事件监听器
            stage4ScrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> {
                // 这里可以处理滚动事件
            });
        }
    }

    private void setupStage5Scroll() {
        if (stage5ScrollPane != null) {
            // 设置滚动条的样式
            stage5ScrollPane.getStyleClass().add("edge-to-edge");

            // 设置滚动条始终显示在右侧
            stage5ScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            stage5ScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            // 设置滚动速度
            stage5ScrollPane.setVvalue(0);

            // 可以添加滚动事件监听器
            stage3ScrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> {
                // 这里可以处理滚动事件
            });
        }
    }

    private void setupStage6Scroll() {
        if (stage6ScrollPane != null) {
            // 设置滚动条的样式
            stage6ScrollPane.getStyleClass().add("edge-to-edge");

            // 设置滚动条始终显示在右侧
            stage6ScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            stage6ScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            // 设置滚动速度
            stage6ScrollPane.setVvalue(0);

            // 可以添加滚动事件监听器
            stage3ScrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> {
                // 这里可以处理滚动事件
            });
        }
    }


    private void loadImage(ImageView imageView, String imageName) {
        String imagePath = "/com/lyc/lycmcu/images/" + imageName;
        Task<Image> task = new Task<Image>() {
            @Override
            protected Image call() throws Exception {
                var imageUrl = getClass().getResource(imagePath);
                if (imageUrl != null) {
                    return new Image(imageUrl.toExternalForm());
                } else {
                    System.err.println("无法找到图片: " + imagePath);
                    return null;
                }
            }
        };
        task.setOnSucceeded(e -> {
            Image image = task.getValue();
            if (image != null) {
                imageView.setImage(image);
            }
        });
        task.setOnFailed(e -> {
            System.err.println("加载图片失败: " + imageName);
            task.getException().printStackTrace();
        });
        new Thread(task).start();
    }

    private void loadImages() {
        //第一阶段
        loadImage(Ironman1Poster, "IronMan1.png");
        loadImage(HulkPoster, "Hulk.jpeg");
        loadImage(Ironman2Poster, "IronMan2.jpeg");
        loadImage(Thor1Poster, "Thor1.png");
        loadImage(CaptainAmerica1Poster, "CaptainAmerica1.jpeg");
        loadImage(Avengers1Poster, "Avengers1.jpg");

        //第二阶段
        loadImage(Ironman3Poster, "IronMan3.jpg");
        loadImage(Thor2Poster, "Thor2.jpeg");
        loadImage(CaptainAmerica2Poster, "CaptainAmerica1.jpeg");
        loadImage(GuardiansoftheGalaxy1Poster, "GuardiansoftheGalaxy1.jpg");
        loadImage(Avengers2Poster, "Avengers2.jpg");
        loadImage(Antman1Poster, "AntMan1.jpg");

        //第三阶段
        loadImage(CaptainAmerica3Poster, "CaptainAmerica3.jpg");
        loadImage(DoctorStrange1Poster, "DoctorStrange1.jpg");
        loadImage(GuardiansoftheGalaxy2Poster, "GuardiansoftheGalaxy2.jpg");
        loadImage(Spiderman1Poster, "SpiderMan3.1.jpeg");
        loadImage(Thor3Poster, "Thor3.jpg");
        loadImage(BlackPanther1Poster, "Blackpanther2.jpeg");
        loadImage(Avengers3Poster, "Avengers3.jpg");
        loadImage(Antman2Poster, "AntMan2.jpg");
        loadImage(CaptainMarvel1Poster, "CaptainMarvel.jpg");
        loadImage(Avengers4Poster, "Avengers4.jpg");
        loadImage(Spiderman2Poster, "SpiderMan3.2.jpg");

        //第四阶段
        loadImage(BlackwidowPoster, "BlackWidow.jpeg");
        loadImage(ShangChiPoster, "Shang-Chi.jpg");
        loadImage(EternalsPoster, "Eternals.jpeg");
        loadImage(Spiderman3Poster, "Spiderman3.3.jpg");
        loadImage(DoctorStrange2Poster, "DoctorStrange2.jpg");
        loadImage(Thor4Poster, "Thor4.jpeg");
        loadImage(BlackPanther2Poster, "Blackpanther2.jpeg");

        //第五阶段
        loadImage(Antman3Poster, "AntMan3.jpg");
        loadImage(GuardiansoftheGalaxy3Poster, "GuardiansoftheGalaxy3.jpeg");
        loadImage(CaptainMarvel2Poster, "TheMarvels.jpg");
        loadImage(Deadpool3Poster, "Deadpool&Wolverine.jpg");
        loadImage(CaptainAmerica4Poster, "CaptainAmerica4.jpeg");
        loadImage(ThunderboltsPoster, "Thunderbolts.jpeg");

        //第六阶段
        loadImage(Avengers5Poster, "Avengers5.jpg");
        loadImage(Avengers6Poster, "Avengers6.jpeg");
    }

    /**
     * 使窗口可拖动。
     */
    private void moveWindow() {
        // 使窗口可拖动
        mainhomepane.setOnMousePressed(event -> {
            // 记录鼠标按下时的坐标
            mainhomepane.setUserData(new double[]{event.getSceneX(), event.getSceneY()});
        });

        mainhomepane.setOnMouseDragged(event -> {
            // 获取鼠标按下时的坐标
            double[] coords = (double[]) mainhomepane.getUserData();
            double offsetX = event.getScreenX() - coords[0];
            double offsetY = event.getScreenY() - coords[1];

            // 移动窗口
            Stage stage = (Stage) mainhomepane.getScene().getWindow();
            stage.setX(offsetX);
            stage.setY(offsetY);
        });
    }
}
