package org.example.kumo;

import org.example.kumo.data.SyncSet;
import org.example.kumo.utils.Log;

public class Kumo {

    /**
     *
     * @param args
     * The main function which runs the scraper
     */
    public static void main(String[] args) {
        // TODO: CLI Parsing
        if(args.length < 1){
            Log.error("Provide the url argument");
            return;
        }

        //String url = args[0];
        //String url = "https://core.ac.uk/download/pdf/156620592.pdf";
        //String url = "https://google.com";
        //String url = "https://safebooru.org//images/3094/fbd2ddf231f5436f821b760318b4d6313fa68908.gif";
        //String url = "https://dlcdn.apache.org/tika/2.3.0/tika-2.3.0-src.zip";
        //String url = "https://gelbooru.com/index.php?page=post&s=list&tags=all";
        //String url = "https://beagleboard.org/static/images/";
        String url = "https://upload.wikimedia.org/wikipedia/commons/transcoded/a/a7/How_to_make_video.webm/How_to_make_video.webm.480p.vp9.webm";
        Log.info("arg[1]: %s", url);

        Scraper scraper = new Scraper(url);
        scraper.crawl();
        scraper.waitComplete();

        Log.info("Completed crawling!!");
        Log.info("Total pages crawled: %d", SyncSet.size());
    }
}
