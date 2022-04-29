package org.example.kumo;

import org.example.kumo.DataBase.DBInterface;
import org.example.kumo.data.SyncSet;
import org.example.kumo.utils.HelperFunctions;
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
            HelperFunctions.printHelp();
            return;
        }

        String url = args[0];
        Log.info("arg[1]: %s", url);

        DBInterface.connectToDataBase();
        DBInterface.nukeTheTable(); // Remove previous remnants

        Scraper scraper = new Scraper(url);
        scraper.crawl();
        scraper.waitComplete();

        Log.info("Completed crawling!!");
        Log.info("Total pages crawled: %d", SyncSet.size());

        String currentPath = System.getProperty("user.dir");
        String fileName = url.replaceAll("[^a-zA-Z\\d]", "-") + ".csv";
        String seperator = System.getProperty("file.separator");
        Log.info("Current path: %s", currentPath);

        String saveFile = currentPath + seperator + fileName;

        if(DBInterface.saveTo(saveFile))
            Log.info("Saved file name: %s", saveFile);
    }
}
