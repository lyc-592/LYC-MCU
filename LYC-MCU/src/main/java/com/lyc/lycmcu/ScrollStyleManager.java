package com.lyc.lycmcu;

import javafx.scene.Scene;

public class ScrollStyleManager {
    private static final String STYLE_PATH = "styles/scroll.css";

    public static void applyStyle(Scene scene) {
        if (scene != null && !scene.getStylesheets().contains(STYLE_PATH)) {
            String cssPath = ScrollStyleManager.class.getResource(STYLE_PATH).toExternalForm();
            scene.getStylesheets().add(cssPath);
        }
    }
}
