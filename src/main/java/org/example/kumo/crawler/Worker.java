package org.example.kumo.crawler;

import org.example.kumo.model.UrlNode;

public class Worker implements Runnable {

    private UrlNode targetNode;

    Worker(UrlNode targetNode) {
        this.targetNode = targetNode;
    }

    @Override
    public void run() {

    }
}
