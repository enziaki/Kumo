package org.example.kumo.crawler;

import org.example.kumo.Extractor.DataClassifier;
import org.example.kumo.data.SyncSet;
import org.example.kumo.model.FileData;
import org.example.kumo.model.UrlNode;
import org.example.kumo.utils.Log;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;

public class Worker implements WorkerInterface {

    private UrlNode targetNode;

    Worker(UrlNode targetNode) {
        this.targetNode = targetNode;
    }

    @Override
    public void run() {
        crawl();
    }


    @Override
    public void crawl() {
        Log.info("Crawling url: %s", targetNode.getUrl());

        Document doc;

        try {
            doc = Jsoup.connect(targetNode.getUrl()).get();
        }
        catch (SocketTimeoutException ste) {
            Log.error("Socket timed out: %s", targetNode.getUrl());
            return;
        }
        /*
        catch(URISyntaxException use) {
            Log.error("Invalid url: %s", targetNode.getUrl());
        }
         */
        catch (HttpStatusException hse) {
            Log.error("Error fetching urls: %s", hse.toString());
            return;
        } catch (MalformedURLException mue) {
            Log.error("Illegal url(%s): %s", targetNode.getUrl(), mue.toString());
            return;
        }
        catch (IllegalArgumentException iae) {
            Log.error("Illegal url(%s): %s", targetNode.getUrl(), iae.toString());
            return;
        }
        catch (UnsupportedMimeTypeException mimeError) {
            extractAndUpdateDb(targetNode);
            return;
        }
        catch(IOException ioe) {
            Log.error("Worker unable to get url: %s", targetNode.getUrl());
            ioe.printStackTrace();
            return;
        }
        /*
        catch (InterruptedException ie) {
            Log.error("Worker interrupted");
            return;
        }
         */

        String title = doc.title();
        targetNode.setTitle(title);
        Log.info("Title: %s", title);
        extractAndUpdateDb(targetNode);

        Elements hrefElements = doc.select("a[href]"); // Handled by workers
        Elements mediaElements = doc.select("[src]"); // MetaDataClassifiers

        // First crawl the current url completely
        processMedia(mediaElements);

        // Then the other urls
        crawlLinks(hrefElements);

        // TODO: This gives elements like js, html, css
        // Elements linkElements = doc.select("link[hrefs]");  // Should be downloaded?

    }

    void processMedia(Elements mediaElements) {
        for(Element media: mediaElements) {
            String mediaUrl = media.absUrl("href");
            UrlNode mediaNode = new UrlNode(mediaUrl);
            extractAndUpdateDb(mediaNode);
        }
    }

    void extractAndUpdateDb(UrlNode targetUrl) {
        if(SyncSet.contains(targetUrl))
            return;

        FileData info = DataClassifier.classify(targetUrl).extract(targetUrl);
        Log.info("Got info: %s", info.toString());
        SyncSet.add(info);
    }

    void crawlLinks(Elements links) {
        for(Element link: links) {
            String linkUrl = link.absUrl("href");
            if(WorkerThreadPool.getInstance().canSpawnWorkers())
                WorkerThreadPool.getInstance().crawlUrl(linkUrl);
            else
                break;
        }
    }

}
