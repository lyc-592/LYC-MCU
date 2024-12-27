package com.lyc.lycmcu;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:AccountInformation.db";

    public static void initializeDatabase() {
        // 检查数据库文件是否存在
        File dbFile = new File("AccountInformation.db");
        if (!dbFile.exists()) {
            createNewDatabase();
        }
    }

    private static void createNewDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                        + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                        + " username TEXT NOT NULL UNIQUE,\n"
                        + " password TEXT NOT NULL\n"
                        + ");";

                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(sql);
                    System.out.println("数据库和用户表已创建。");
                }
            }
        } catch (Exception e) {
            System.out.println("创建数据库时发生错误：" + e.getMessage());
        }
    }

    public static void deleteDatabase() {
        File dbFile = new File("AccountInformation.db");
        if (dbFile.exists()) {
            if (dbFile.delete()) {
                System.out.println("数据库已删除。");
            } else {
                System.out.println("无法删除数据库。");
            }
        } else {
            System.out.println("数据库文件不存在。");
        }
    }
}
