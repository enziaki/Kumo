package org.example.kumo.crawler;

public interface WorkerInterface extends Runnable {

    @Override
    public void run();

    public void crawl();

}
