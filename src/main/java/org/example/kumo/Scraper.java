package org.example.kumo;

import org.example.kumo.DataBase.DBInterface;
import org.example.kumo.crawler.WorkerThreadPool;
import org.example.kumo.utils.Log;

public class Scraper {
    String url;

    Scraper(String url) {
        this.url = url;
    }

    public void crawl() {
        DBInterface.connectToDataBase();

        WorkerThreadPool.getInstance().crawlUrl(url);

    }
}
