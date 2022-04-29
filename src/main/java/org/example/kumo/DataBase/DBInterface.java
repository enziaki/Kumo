package org.example.kumo.DataBase;

import org.example.kumo.model.FileData;
import org.example.kumo.model.UrlNode;
import org.example.kumo.utils.Config;
import org.example.kumo.utils.Log;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;


/**
 * The Database interface for the entire project
 */
public class DBInterface {



    private static Connection db;
    private static final String url = Config.databaseUrl();
    private static final String userName = "postgres";
    private static final String password = "postgres";

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("y-M-d H:m:s");

    public static void connectToDataBase() {
        Properties properties = new Properties();

        properties.setProperty("user", userName);
        properties.setProperty("password", password);

        try {
            db = DriverManager.getConnection(url, properties);
        } catch (SQLException sqe) {
            Log.error("Failed to connect to database: " + sqe);
            System.exit(-1);
        }
        Log.info("Connection established to: %s", url);
        createTable();
    }

    public static boolean createTable() {
        Statement statement;
        try {
            statement = db.createStatement();
            statement.execute(String.format("CREATE TABLE IF NOT EXISTS urls(id SERIAL  PRIMARY KEY, url VARCHAR NOT NULL, last_visited TIMESTAMP, mime_type VARCHAR, metadata VARCHAR)"));
        } catch (SQLException e) {
            Log.error("Failed to create table: %s", e.toString());
            return false;
        }
        return true;
    }

    public static boolean contains(UrlNode node) {
        try {
            Statement statement = db.createStatement();
            statement.execute(String.format("SELECT * FROM urls WHERE url='%s'", node.getUrl()));
            ResultSet res = statement.getResultSet();
            if (res.next())
                return true;
        } catch (SQLException sqe) {
            Log.error("Failed to check if(%s) exists: %s", node.getUrl(), sqe.toString());
            return false;
        }
        return false;
    }

    public static boolean insertNode(UrlNode node) {

        if (contains(node))
            return true;

        String url = node.getUrl();
        String type = node.getMimeType();
        Date date = new Date();

        try {
            Statement statement = db.createStatement();
            statement.execute(String.format("INSERT INTO urls (url, last_visited, mime_type, metadata) VALUES ('%s', '%s', '%s', '%s');", url, dateFormat.format(date), node.getMimeType(), node.getMetaData()));
        } catch (SQLException sqe) {
            Log.error("Failed to insert values: %s", sqe.toString());
            return false;
        }
        Log.info("Successfully inserted: %s", node.toString());
        return true;
    }

    public static void nukeTheTable() {
        try {
            db.createStatement().execute("DROP TABLE IF EXISTS urls;");
        } catch (SQLException ignored) {
        }
        createTable(); // Rebuild the Tables
    }

    public static boolean removeNode(UrlNode urlNode) {
        try {
            String query = String.format("DELETE FROM urls WHERE url='%s'", urlNode.getUrl());
            db.createStatement().execute(query);
        } catch (SQLException sqe) {
            Log.error("Failed to insert remove(%s): %s", urlNode.getUrl(), sqe.toString());
            return false;
        }
        return true;
    }

    public static boolean updateNode(UrlNode node) {
        if (!contains(node)) {
            return insertNode(node);
        }
        try {
            String query = String.format("UPDATE urls SET last_visited='%s', mime_type='%s', metadata='%s' WHERE url='%s';", dateFormat.format(new Date()), node.getMimeType(), node.getMetaData(), node.getUrl());
            db.createStatement().execute(query);
        } catch (SQLException sqe) {
            Log.error("Failed to update database: %s", sqe.toString());
            return false;
        }
        return true;
    }

    public static boolean saveTo(String fileName) {
        String query = String.format("COPY urls TO '%s' DELIMITER ',' CSV HEADER;", fileName);  // TODO: escape chars

        try {
            db.createStatement().execute(query);
        } catch (SQLException e) {
            Log.error("Failed to save to file: " + e);
            return false;
        }
        return true;
    }
}
