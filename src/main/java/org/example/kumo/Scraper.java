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

    public void waitComplete() {

        while(!WorkerThreadPool.getInstance().isTerminated()) {
            Log.info("Waiting for termination");
            try {
                Thread.sleep(10000);  // TODO: This ia bad way of synchronization
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //.getInstance().waitComplete();
        }
    }
}
