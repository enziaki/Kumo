package org.example.kumo.DataBase;

import org.example.kumo.model.UrlNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DBInterfaceTest {

    @Test
    void checkConnection() {
        DBInterface.connectToDataBase();
    }

    @Test
    void testInsertAndDelete() {

        DBInterface.connectToDataBase();

        String url = "https://google.com";
        var urlNode = new UrlNode(url);

        DBInterface.insertNode(urlNode);

        assertTrue(DBInterface.contains(urlNode));

        DBInterface.removeNode(urlNode);

        assertFalse(DBInterface.contains(urlNode));

    }
}