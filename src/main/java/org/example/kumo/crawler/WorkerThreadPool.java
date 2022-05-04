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
    private int KEEP_ALIVE = 10;
    private ThreadPoolExecutor workerPool;

    private final int  MAX_PAGE_FETCH = 1000;
    private int CRAWL_DEPTH = 6;
    private boolean SPAWN_WORKERS = true;

    private Boolean completed = false;
    private static WorkerThreadPool INSTANCE;

    private WorkerThreadPool() {
        workerPool = new ThreadPoolExecutor(
                MIN_THREADS, MAX_THREADS, KEEP_ALIVE, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>()
        );
        workerPool.allowCoreThreadTimeOut(true);
    }

    public static WorkerThreadPool getInstance() {
        if(INSTANCE == null)
            INSTANCE = new WorkerThreadPool();
        return INSTANCE;
    }
    public void crawlUrl(String url) {

        if(!canSpawnWorkers())
            return;

        UrlNode targetUrl = new UrlNode(url);

        if(SyncSet.contains(targetUrl)) {
            Log.info("Already crawled: %s", url);
            return;
        }
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
        workerPool.shutdownNow();

    }

    public synchronized void waitComplete() {

            try {
                wait();
                Log.info("Wait completed");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

    }
    public synchronized boolean isTerminated() {
        if(workerPool.isTerminated() || workerPool.getActiveCount() == 0) {
            notifyAll();
            return true;
        }
        return false;
    }

    public synchronized boolean canSpawnWorkers() {

        synchronized (this) {
            if (SyncSet.size() > MAX_PAGE_FETCH) {
                SPAWN_WORKERS = false;
                Log.debug("Max crawl depth reached, Stopping spawning workers");
                shutdown(); // No more spiders!!
                return false;
            }
        }

        return true;
    }

}
