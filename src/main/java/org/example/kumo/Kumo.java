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
        if(args.length < 2){
            Log.error("Provide the url argument");
            return;
        }


    }
}
