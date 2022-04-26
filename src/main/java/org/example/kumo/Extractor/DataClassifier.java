package org.example.kumo.Extractor;

import org.apache.tika.Tika;
import org.example.kumo.model.UrlNode;
import org.example.kumo.utils.Log;

import java.io.IOException;
import java.net.URL;

/**
 * Classifies the given datatype and returns pertaining  MetaDataExtractor
 */
public class DataClassifier {
    public static MetaDataExtractor classify(UrlNode targetUrl) {
        String mimeType = getMimeType(targetUrl.getUrl());

        if(mimeType.equals("application/pdf"))
            return new DefaultMetaDataExtractor();
        else if(mimeType.equals("application/zip") || mimeType.equals("application/x-tar"))
            return new PacakgeMetaDataExtractor();

        return new DefaultMetaDataExtractor();
    }

    private static String getMimeType(String url){
        String mimeType = "";

        Tika tika = new Tika();
        try {
            mimeType = tika.detect(new URL(url));
        } catch (IOException e) {
            Log.error("Unable to detect mime type(%s): " + e, url);
        }

        return mimeType;
    }
}
