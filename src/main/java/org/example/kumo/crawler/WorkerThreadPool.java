package org.example.kumo.crawler;

import org.example.kumo.data.SyncSet;
import org.example.kumo.model.UrlNode;
import org.example.kumo.utils.Log;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * The thread pool which maintains the workers
 */
public class WorkerThreadPool {

    private int MAX_THREADS = 10;
    private int MIN_THREADS = 3;
    private int KEEP_ALIVE = 0;
    private ThreadPoolExecutor workerPool;

    private int MAX_PAGE_FETCH = 1000;
    private int CRAWL_DEPTH = 6;
    private boolean SPAWN_WORKERS = true;

    WorkerThreadPool() {
        workerPool = new ThreadPoolExecutor(
                MIN_THREADS, MAX_THREADS, KEEP_ALIVE, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>()
        );
    }

    public void crawlUrl(String url) {
        UrlNode targetUrl = new UrlNode(url);

        if(SyncSet.contains(targetUrl)) {
            Log.info("Already crawled: %s", url);
            return;
        }
        if(canSpawnWorkers())
            addWorker(targetUrl);
    }

    private synchronized void addWorker(UrlNode targetUrl) {
        // Adds worker to the exec queue
        if(SyncSet.contains(targetUrl)) {
            Log.info("Already crawled: %s", targetUrl.getUrl());
            return;
        }
        workerPool.execute(new Worker(targetUrl));
    }

    public synchronized void shutdown() {
        // Shutdowns the worker pool
        // NOTE: Can cause RejectionExceptions
        workerPool.shutdown();
    }

    public synchronized boolean isTerminated() {
        return workerPool.isTerminated();
    }

    public synchronized boolean canSpawnWorkers() {
        if(!SPAWN_WORKERS)  // No need to check if already false
            return SPAWN_WORKERS;

        if(SyncSet.size() > MAX_PAGE_FETCH) {
            SPAWN_WORKERS = false;
            Log.debug("Max crawl depth reached, Stopping spawning workers");
            shutdown(); // No more spiders!!
        }

        return SPAWN_WORKERS;
    }

}
