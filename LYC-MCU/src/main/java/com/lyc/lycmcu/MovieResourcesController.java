package com.lyc.lycmcu;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

public class MovieResourcesController {

    @FXML
    private AnchorPane mainresourcespane;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> yearComboBox;

    @FXML
    private ComboBox<String> phaseComboBox;

    @FXML
    private FlowPane movieContainer;

    @FXML
    private Button closebtn, minimisebtn;

    private List<Movie> allMovies;

    @FXML
    public void initialize() {
        // 初始化电影数据
        initializeMovieData();

        // 初始化年份和阶段下拉框
        initializeComboBoxes();

        // 加载所有电影
        loadMovies(allMovies);


        Platform.runLater(() -> {
            ScrollStyleManager.applyStyle(mainresourcespane.getScene());
        });

        customiseWindowButtons();
        moveWindow();
    }

    private void initializeMovieData() {
        allMovies = List.of(
                //第一阶段
                new Movie("钢铁侠", "2008", "第1阶段", "images/IronMan1.png"),
                new Movie("无敌浩克", "2008", "第1阶段", "images/Hulk.jpeg"),
                new Movie("钢铁侠2", "2010", "第1阶段", "images/IronMan2.jpeg"),
                new Movie("雷神", "2011", "第1阶段", "images/Thor1.png"),
                new Movie("美国队长", "2011", "第1阶段", "images/CaptainAmerica1.jpeg"),
                new Movie("复仇者联盟", "2012", "第1阶段", "images/Avengers1.jpg"),

                //第二阶段
                new Movie("钢铁侠3", "2013", "第2阶段", "images/IronMan3.jpg"),
                new Movie("雷神2：黑暗世界", "2013", "第2阶段", "images/Thor2.jpeg"),
                new Movie("美国队长2：冬日战士", "2014", "第2阶段", "images/CaptainAmerica2.jpeg"),
                new Movie("银河护卫队", "2014", "第2阶段", "images/GuardiansoftheGalaxy1.jpg"),
                new Movie("复仇者联盟2：奥创纪元", "2015", "第2阶段", "images/Avengers2.jpg"),
                new Movie("蚁人", "2015", "第2阶段", "images/AntMan1.jpg"),

                //第三阶段
                new Movie("美国队长3：内战", "2016", "第3阶段", "images/CaptainAmerica2.jpeg"),
                new Movie("奇异博士", "2016", "第3阶段", "images/DoctorStrange1.jpg"),
                new Movie("银河护卫队2", "2017", "第3阶段", "images/GuardiansoftheGalaxy2.jpg"),
                new Movie("蜘蛛侠：英雄归来", "2017", "第3阶段", "images/SpiderMan3.1.jpeg"),
                new Movie("雷神3：诸神黄昏", "2017", "第3阶段", "images/Thor3.jpg"),
                new Movie("黑豹", "2018", "第3阶段", "images/Blackpanther1.jpg"),
                new Movie("复仇者联盟3：无限战争", "2018", "第3阶段", "images/Avengers3.jpg"),
                new Movie("蚁人2：黄蜂女现身", "2018", "第3阶段", "images/AntMan2.jpg"),
                new Movie("惊奇队长", "2019", "第3阶段", "images/CaptainMarvel.jpg"),
                new Movie("复仇者联盟4：终局之战", "2019", "第3阶段", "images/Avengers4.jpg"),
                new Movie("蜘蛛侠：英雄远征", "2019", "第3阶段", "images/SpiderMan3.2.jpg"),

                //第四阶段
                new Movie("黑寡妇", "2021", "第4阶段", "images/BlackWidow.jpeg"),
                new Movie("尚气与十环传奇", "2021", "第4阶段", "images/Shang-Chi.jpg"),
                new Movie("永恒族", "2021", "第4阶段", "images/Eternals.jpeg"),
                new Movie("蜘蛛侠：英雄无归", "2021", "第4阶段", "images/SpiderMan3.3.jpg"),
                new Movie("奇异博士2：疯狂多元宇宙", "2022", "第4阶段", "images/DoctorStrange2.jpg"),
                new Movie("雷神4：爱与雷霆", "2022", "第4阶段", "images/Thor4.jpeg"),
                new Movie("黑豹2：瓦坎达万岁", "2022", "第4阶段", "images/Blackpanther2.jpeg"),

                //第五阶段
                new Movie("蚁人与黄蜂女：量子狂潮", "2023", "第5阶段", "images/AntMan3.jpg"),
                new Movie("银河护卫队3", "2023", "第5阶段", "images/GuardiansoftheGalaxy3.jpeg"),
                new Movie("惊奇队长2", "2023", "第5阶段", "images/TheMarvels.jpg"),
                new Movie("死侍与金刚狼", "2024", "第5阶段", "images/Deadpool&Wolverine.jpg"),
                new Movie("美国队长4：新世界秩序", "2025", "第5阶段", "images/CaptainAmerica4.jpeg"),
                new Movie("雷霆特工队", "2025", "第5阶段", "images/Thunderbolts.jpeg"),

                //第六阶段
                new Movie("复仇者联盟5：毁灭之日", "2026", "第6阶段", "images/Avengers5.jpg"),
                new Movie("复仇者联盟6：秘密战争", "2027", "第6阶段", "images/Avengers6.jpeg")
        );
    }

    private void initializeComboBoxes() {
        // 年份下拉框
        yearComboBox.getItems().add("请选择时间");  // 添加"请选择"选项
        yearComboBox.getItems().addAll(
                allMovies.stream()
                        .map(Movie::getYear)
                        .distinct()
                        .sorted()
                        .collect(Collectors.toList())
        );
        yearComboBox.setValue("请选择时间");  // 设置默认值

        // 阶段下拉框
        phaseComboBox.getItems().add("请选择阶段");  // 添加"请选择"选项
        phaseComboBox.getItems().addAll(
                allMovies.stream()
                        .map(Movie::getPhase)
                        .distinct()
                        .sorted()
                        .collect(Collectors.toList())
        );
        phaseComboBox.setValue("请选择阶段");  // 设置默认值
    }

    // 修改搜索方法，支持模糊搜索
    @FXML
    public void searchMovies() {
        String searchText = searchField.getText().toLowerCase();
        String selectedYear = yearComboBox.getValue();
        String selectedPhase = phaseComboBox.getValue();

        List<Movie> filteredMovies = allMovies.stream()
                .filter(movie ->
                        // 修改搜索逻辑，支持模糊匹配
                        (searchText.isEmpty() || movie.getName().toLowerCase().contains(searchText)) &&
                                // 如果选择了"请选择时间"，则不进行年份过滤
                                (selectedYear.equals("请选择时间") || movie.getYear().equals(selectedYear)) &&
                                // 如果选择了"请选择阶段"，则不进行阶段过滤
                                (selectedPhase.equals("请选择阶段") || movie.getPhase().equals(selectedPhase))
                )
                .collect(Collectors.toList());

        loadMovies(filteredMovies);
    }

    private void loadMovies(List<Movie> movies) {
        movieContainer.getChildren().clear();

        for (Movie movie : movies) {
            HBox movieBox = createMovieBox(movie);
            movieContainer.getChildren().add(movieBox);
        }
    }

    private HBox createMovieBox(Movie movie) {
        HBox movieBox = new HBox(10);
        movieBox.setStyle("-fx-border-color: lightgray; -fx-border-width: 1px; -fx-padding: 10px;");

        // 海报
        ImageView poster = new ImageView(new Image(getClass().getResourceAsStream(movie.getPosterPath())));
        poster.setFitWidth(150);
        poster.setFitHeight(225);
        poster.setPreserveRatio(true);

        // 电影信息
        VBox movieInfo = new VBox(10);
        Label nameLabel = new Label(movie.getName());
        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // 年份和阶段信息
        Label detailLabel = new Label(movie.getYear() + " | " + movie.getPhase());
        detailLabel.setStyle("-fx-font-size: 14px;");

        // 资源链接
        HBox resourceLinks = new HBox(10);
        Hyperlink baiduLink, doubanLink, movieResourceLink;

        switch (movie.getName()) {
            //第一阶段
            case "钢铁侠":
                baiduLink = createResourceLink("百度", "https://baike.baidu.com/item/%E9%92%A2%E9%93%81%E4%BE%A0/303?fromModule=lemma_inlink");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/1432146/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/v_19rrk3rbgo.html?vfrmrst=0&vfrm=3&vfrmblk=pca_115_IP_card");
                break;
            case "无敌浩克":
                baiduLink = createResourceLink("百度", "https://baike.baidu.com/item/%E6%97%A0%E6%95%8C%E6%B5%A9%E5%85%8B/65940?fromModule=lemma_inlink");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/1866475/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/?vfm=f_764_xcw&fv=9b181f14a1d2a834");
                break;
            case "钢铁侠2":
                baiduLink = createResourceLink("百度", "https://baike.baidu.com/item/%E9%92%A2%E9%93%81%E4%BE%A02/62749?fromModule=lemma_inlink");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/3066739/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/v_19rrg7egrs.html?s3=pca_115_episode_new&s4=0&vfrmblk=pca_115_episode_new&vfrm=3&s2=3&vfrmrst=0");
                break;
            case "雷神":
                baiduLink = createResourceLink("百度", "https://www.baidu.com/s?wd=雷神1电影");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/4164024/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/v_19rrk0cc4s.html?vfrmrst=0&vfrm=3&vfrmblk=pca_115_IP_card");
                break;
            case "美国队长":
                baiduLink = createResourceLink("百度", "https://www.baidu.com/s?wd=美国队长1电影");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/4690218/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/v_19rrk0cajg.html?vfrmrst=0&vfrm=3&vfrmblk=pca_115_IP_card");
                break;
            case "复仇者联盟":
                baiduLink = createResourceLink("百度", "https://www.baidu.com/s?wd=复仇者联盟1电影");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/6051002/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/v_19rrk4egc4.html?vfrmrst=0&vfrm=3&vfrmblk=pca_115_IP_card");
                break;
                //第二阶段
            case "钢铁侠3":
                baiduLink = createResourceLink("百度", "https://baike.baidu.com/item/%E9%92%A2%E9%93%81%E4%BE%A03/8468661?fromModule=lemma_inlink");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/3231742/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/");
                break;
            case "雷神2：黑暗世界":
                baiduLink = createResourceLink("百度", "https://baike.baidu.com/item/%E9%9B%B7%E7%A5%9E2%EF%BC%9A%E9%BB%91%E6%9A%97%E4%B8%96%E7%95%8C/8937952?fromModule=lemma_inlink");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/6560058/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/v_19rrn2b5nw.html?vfrmrst=0&vfrm=3&vfrmblk=pca_115_IP_card");
                break;
            case "美国队长2：冬日战士":
                baiduLink = createResourceLink("百度", "https://baike.baidu.com/item/%E7%BE%8E%E5%9B%BD%E9%98%9F%E9%95%BF2%EF%BC%9A%E5%86%AC%E6%97%A5%E6%88%98%E5%A3%AB/8938680?fromModule=lemma_inlink");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/6390823/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/v_19rrg0tpa4.html?vfrmrst=0&vfrm=3&vfrmblk=pca_115_IP_card");
                break;
            case "银河护卫队":
                baiduLink = createResourceLink("百度", "https://baike.baidu.com/item/%E9%93%B6%E6%B2%B3%E6%8A%A4%E5%8D%AB%E9%98%9F/221813?fromModule=lemma_inlink");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/7065154/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/v_19rrn8h3xg.html?vfrmrst=0&vfrm=3&vfrmblk=pca_115_IP_card");
                break;
            case "复仇者联盟2：奥创纪元":
                baiduLink = createResourceLink("百度", "https://baike.baidu.com/item/%E5%A4%8D%E4%BB%87%E8%80%85%E8%81%94%E7%9B%9F2%EF%BC%9A%E5%A5%A5%E5%88%9B%E7%BA%AA%E5%85%83/15102053?fromModule=lemma_inlink");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/10741834/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/v_19rrodq1gc.html?vfrmrst=0&vfrm=3&vfrmblk=pca_115_IP_card");
                break;
            case "蚁人":
                baiduLink = createResourceLink("百度", "https://baike.baidu.com/item/%E8%9A%81%E4%BA%BA/16272?fromModule=lemma_inlink");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/1866473/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/v_19rrklq2bs.html?vfrmrst=0&vfrm=3&vfrmblk=pca_115_IP_card");
                break;

                //第三阶段
            case "美国队长3：内战":
                baiduLink = createResourceLink("百度", "https://baike.baidu.com/item/%E7%BE%8E%E5%9B%BD%E9%98%9F%E9%95%BF3%EF%BC%9A%E5%86%85%E6%88%98/15970854?fromModule=lemma_inlink");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/25820460/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/v_19rrmfx1z8.html?vfrmrst=0&vfrm=3&vfrmblk=pca_115_IP_card");
                break;
            case "奇异博士":
                baiduLink = createResourceLink("百度", "https://baike.baidu.com/item/%E5%A5%87%E5%BC%82%E5%8D%9A%E5%A3%AB/2492942?fromModule=lemma_inlink");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/3025375/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/v_19rrajwnxk.html?vfrmrst=0&vfrm=3&vfrmblk=pca_115_IP_card");
                break;
            case "银河护卫队2":
                baiduLink = createResourceLink("百度", "https://baike.baidu.com/item/%E9%93%B6%E6%B2%B3%E6%8A%A4%E5%8D%AB%E9%98%9F2/15079836?fromModule=lemma_inlink");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/25937854/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/v_19rr7qiwak.html?vfrmrst=0&vfrm=3&vfrmblk=pca_115_IP_card");
                break;
            case "蜘蛛侠：英雄归来":
                baiduLink = createResourceLink("百度", "https://baike.baidu.com/item/%E8%9C%98%E8%9B%9B%E4%BE%A0%EF%BC%9A%E8%8B%B1%E9%9B%84%E5%BD%92%E6%9D%A5/20270901?fromModule=lemma_inlink");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/24753477/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/v_19rrdw5cs4.html?vfrmrst=0&vfrm=3&vfrmblk=pca_115_IP_card");
                break;
            case "雷神3：诸神黄昏":
                baiduLink = createResourceLink("百度", "https://baike.baidu.com/item/%E9%9B%B7%E7%A5%9E3%EF%BC%9A%E8%AF%B8%E7%A5%9E%E9%BB%84%E6%98%8F/15970851?fromModule=lemma_inlink");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/25821634/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/v_19rr7qiwaw.html?vfrmrst=0&vfrm=3&vfrmblk=pca_115_IP_card");
                break;
            case "黑豹":
                baiduLink = createResourceLink("百度", "https://baike.baidu.com/item/%E9%BB%91%E8%B1%B9/15466851?fromModule=lemma_inlink");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/6390825/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/v_19rr7p16x0.html?vfrmrst=0&vfrm=3&vfrmblk=pca_115_IP_card");
                break;
            case "复仇者联盟3：无限战争":
                baiduLink = createResourceLink("百度", "https://baike.baidu.com/item/%E5%A4%8D%E4%BB%87%E8%80%85%E8%81%94%E7%9B%9F3%EF%BC%9A%E6%97%A0%E9%99%90%E6%88%98%E4%BA%89/15971907?fromModule=lemma_inlink");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/24773958/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/v_19rr7q1fyc.html?vfrmrst=0&vfrm=3&vfrmblk=pca_115_IP_card");
                break;
            case "蚁人2：黄蜂女现身":
                baiduLink = createResourceLink("百度", "https://baike.baidu.com/item/%E8%9A%81%E4%BA%BA2%EF%BC%9A%E9%BB%84%E8%9C%82%E5%A5%B3%E7%8E%B0%E8%BA%AB/22360628?fromModule=lemma_inlink");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/26636712/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/v_19rr7peols.html?vfrmrst=0&vfrm=3&vfrmblk=pca_115_IP_card");
                break;
            case "惊奇队长":
                baiduLink = createResourceLink("百度", "https://baike.baidu.com/item/%E6%83%8A%E5%A5%87%E9%98%9F%E9%95%BF/15970597?fromModule=lemma_inlink");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/26213252/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/v_19rr7pgg08.html?vfrmrst=0&vfrm=3&vfrmblk=pca_115_IP_card");
                break;
            case "复仇者联盟4：终局之战":
                baiduLink = createResourceLink("百度", "https://baike.baidu.com/item/%E5%A4%8D%E4%BB%87%E8%80%85%E8%81%94%E7%9B%9F4%EF%BC%9A%E7%BB%88%E5%B1%80%E4%B9%8B%E6%88%98/23196017?fromModule=lemma_inlink");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/26100958/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/v_19rr7q1fy0.html?vfrmrst=0&vfrm=3&vfrmblk=pca_115_IP_card");
                break;
            case "蜘蛛侠：英雄远征":
                baiduLink = createResourceLink("百度", "https://baike.baidu.com/item/%E8%9C%98%E8%9B%9B%E4%BE%A0%EF%BC%9A%E8%8B%B1%E9%9B%84%E8%BF%9C%E5%BE%81/22944950?fromModule=lemma_inlink");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/26931786/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/v_19rrf6v2c8.html?vfrmrst=0&vfrm=3&vfrmblk=pca_115_IP_card");
                break;

            //第四阶段
            case "黑寡妇":
                baiduLink = createResourceLink("百度", "https://baike.baidu.com/item/%E9%BB%91%E5%AF%A1%E5%A6%87/19751743?fromModule=lemma_inlink");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/25828589/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/");
                break;
            case "尚气与十环传奇":
                baiduLink = createResourceLink("百度", "https://baike.baidu.com/item/%E5%B0%9A%E6%B0%94%E4%B8%8E%E5%8D%81%E7%8E%AF%E4%BC%A0%E5%A5%87/57543601?fromModule=lemma_inlink");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/30394797/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/");
                break;
            case "永恒族":
                baiduLink = createResourceLink("百度", "https://baike.baidu.com/item/%E6%B0%B8%E6%81%92%E6%97%8F/22588559?fromModule=lemma_inlink");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/30223888/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/");
                break;
            case "蜘蛛侠：英雄无归":
                baiduLink = createResourceLink("百度", "https://baike.baidu.com/item/%E8%9C%98%E8%9B%9B%E4%BE%A0%EF%BC%9A%E8%8B%B1%E9%9B%84%E6%97%A0%E5%BD%92/56733738?fromModule=lemma_inlink");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/26933210/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/");
                break;
            case "奇异博士2：疯狂多元宇宙":
                baiduLink = createResourceLink("百度", "https://baike.baidu.com/item/%E5%A5%87%E5%BC%82%E5%8D%9A%E5%A3%AB2%EF%BC%9A%E7%96%AF%E7%8B%82%E5%A4%9A%E5%85%83%E5%AE%87%E5%AE%99/59680424?fromModule=lemma_inlink");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/30304994/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/");
                break;
            case "雷神4：爱与雷霆":
                baiduLink = createResourceLink("百度", "https://baike.baidu.com/item/%E9%9B%B7%E7%A5%9E4%EF%BC%9A%E7%88%B1%E4%B8%8E%E9%9B%B7%E9%9C%86/23630337?fromModule=lemma_inlink");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/34477861/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/");
                break;
            case "黑豹2：瓦坎达万岁":
                baiduLink = createResourceLink("百度", "https://baike.baidu.com/item/%E9%BB%91%E8%B1%B92%EF%BC%9A%E7%93%A6%E5%9D%8E%E8%BE%BE%E4%B8%87%E5%B2%81/62069660?fromModule=lemma_inlink");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/30167997/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/");
                break;

                //第五阶段
            case "蚁人与黄蜂女：量子狂潮":
                baiduLink = createResourceLink("百度", "https://baike.baidu.com/item/%E8%9A%81%E4%BA%BA%E4%B8%8E%E9%BB%84%E8%9C%82%E5%A5%B3%EF%BC%9A%E9%87%8F%E5%AD%90%E7%8B%82%E6%BD%AE/62098652?fromModule=lemma_inlink");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/34610636/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/v_12xx2svq71k.html?s3=pca_115_episode_new&s4=0&vfrmblk=pca_115_episode_new&vfrm=3&s2=3&vfrmrst=0");
                break;
            case "银河护卫队3":
                baiduLink = createResourceLink("百度", "https://baike.baidu.com/item/%E9%93%B6%E6%B2%B3%E6%8A%A4%E5%8D%AB%E9%98%9F3/15281215?fromModule=lemma_inlink");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/26258779/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/v_19rs2i06s0.html?s3=pca_115_episode_new&s4=0&vfrmblk=pca_115_episode_new&vfrm=3&s2=3&vfrmrst=0");
                break;
            case "惊奇队长2":
                baiduLink = createResourceLink("百度", "https://baike.baidu.com/item/%E6%83%8A%E5%A5%87%E9%98%9F%E9%95%BF2/49921127?fromModule=lemma_inlink");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/34678231/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/v_uvhs30ayzw.html?s3=pca_115_episode_new&s4=0&vfrmblk=pca_115_episode_new&vfrm=3&s2=3&vfrmrst=0");
                break;
            case "死侍与金刚狼":
                baiduLink = createResourceLink("百度", "https://baike.baidu.com/item/%E6%AD%BB%E4%BE%8D%E4%B8%8E%E9%87%91%E5%88%9A%E7%8B%BC/64446435?fromModule=lemma_inlink");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/26957900/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/v_vxpptjxjxc.html?vid=0fdfb524b85dc80a2ed04729ddde5ae2&ischarge=false&vtype=0&ht=0&s2=cloud_cinema_ply&s3=lianbo_card_0&cloud_cinema=true&bitid=800&ac=_1");
                break;
            case "美国队长4：新世界秩序":
                baiduLink = createResourceLink("百度", "https://baike.baidu.com/item/%E7%BE%8E%E5%9B%BD%E9%98%9F%E9%95%BF4%EF%BC%9A%E6%96%B0%E4%B8%96%E7%95%8C%E7%A7%A9%E5%BA%8F/61764545?fromModule=lemma_inlink");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/35443158/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/");
                break;
            case "雷霆特工队":
                baiduLink = createResourceLink("百度", "https://baike.baidu.com/item/%E9%9B%B7%E9%9C%86%E7%89%B9%E5%B7%A5%E9%98%9F/61379835?fromtitle=%E9%9B%B7%E9%9C%86%E7%89%B9%E6%94%BB%E9%98%9F*&fromid=64941873");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/35927475/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/");
                break;

                //第六阶段
            case "复仇者联盟5：毁灭之日":
                baiduLink = createResourceLink("百度", "https://baike.baidu.com/item/%E5%A4%8D%E4%BB%87%E8%80%85%E8%81%94%E7%9B%9F5%EF%BC%9A%E6%AF%81%E7%81%AD%E4%B9%8B%E6%97%A5/64717737?fromModule=lemma_inlink");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/36011202/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/");
                break;
            case "复仇者联盟6：秘密战争":
                baiduLink = createResourceLink("百度", "https://baike.baidu.com/item/%E5%A4%8D%E4%BB%87%E8%80%85%E8%81%94%E7%9B%9F6%EF%BC%9A%E7%A7%98%E5%AF%86%E6%88%98%E4%BA%89/61764406?fromModule=lemma_inlink");
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/subject/36011203/");
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/");
                break;
            default:
                baiduLink = createResourceLink("百度", "https://www.baidu.com/index.php?tn=15007414_11_dg" + movie.getName());
                doubanLink = createResourceLink("豆瓣", "https://movie.douban.com/" + movie.getName());
                movieResourceLink = createResourceLink("影视资源", "https://www.iqiyi.com/" + movie.getName() + "+在线观看");
        }

        resourceLinks.getChildren().addAll(baiduLink, doubanLink, movieResourceLink);

        movieInfo.getChildren().addAll(nameLabel, detailLabel, resourceLinks);

        movieBox.getChildren().addAll(poster, movieInfo);
        return movieBox;
    }

    private Hyperlink createResourceLink(String text, String url) {
        Hyperlink link = new Hyperlink(text);
        link.setOnAction(e -> openWebpage(url));
        return link;
    }

    private void openWebpage(String url) {
        try {
            // 检查是否支持桌面操作
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();

                // 检查是否支持浏览
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(new URI(url));
                } else {
                    showErrorAlert("不支持打开浏览器");
                }
            } else {
                showErrorAlert("不支持桌面操作");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("无法打开网页：" + e.getMessage());
        }
    }

    private void showErrorAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    @FXML
    public void btnHover(MouseEvent event) {
        // 鼠标进入按钮时的效果
        if (event.getSource() instanceof Button sourceButton) {
            sourceButton.setCursor(Cursor.HAND);
            sourceButton.setOpacity(1.0); // 可以调整透明度
        }
    }

    @FXML
    public void btnExit(MouseEvent event) {
        // 鼠标退出按钮时的效果
        if (event.getSource() instanceof Button sourceButton) {
            sourceButton.setCursor(Cursor.DEFAULT);
            sourceButton.setOpacity(0.8); // 恢复透明度
        }
    }

    @FXML
    private void logOut() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("logout.fxml"));
            AnchorPane logoutPane = loader.load();

            // 获取 LogoutController 的实例
            LogoutController logoutController = loader.getController();

            // 获取当前的 Home 窗口的 Stage
            Stage homeStage = (Stage) mainresourcespane.getScene().getWindow();

            // 将 Home 的 Stage 传递给 LogoutController
            logoutController.setHomeStage(homeStage);

            // 创建一个新的 Stage 来显示 logout 窗口
            Stage logoutStage = new Stage();
            logoutStage.setTitle("Log Out");
            logoutStage.setScene(new Scene(logoutPane));
            logoutStage.initModality(Modality.APPLICATION_MODAL);

            // 将 logout 窗口的 Stage 也传递给 LogoutController，以便关闭
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
            case "musicbtn": return "music.fxml";
            case "mebtn": return "me.fxml";
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

    /**
     * 使窗口可拖动。
     */
    private void moveWindow() {
        // 使窗口可拖动
        mainresourcespane.setOnMousePressed(event -> {
            // 记录鼠标按下时的坐标
            mainresourcespane.setUserData(new double[]{event.getSceneX(), event.getSceneY()});
        });

        mainresourcespane.setOnMouseDragged(event -> {
            // 获取鼠标按下时的坐标
            double[] coords = (double[]) mainresourcespane.getUserData();
            double offsetX = event.getScreenX() - coords[0];
            double offsetY = event.getScreenY() - coords[1];

            // 移动窗口
            Stage stage = (Stage) mainresourcespane.getScene().getWindow();
            stage.setX(offsetX);
            stage.setY(offsetY);
        });
    }

}