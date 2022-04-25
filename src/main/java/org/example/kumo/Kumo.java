package org.example.kumo;

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
        String url = "https://google.com";
        Log.info("arg[1]: %s", url);

        Scraper scraper = new Scraper(url);
        scraper.crawl();

        Log.info("Completed crawling!!");
    }
}
