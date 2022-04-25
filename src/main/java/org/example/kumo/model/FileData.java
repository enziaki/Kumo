package org.example.kumo.model;

import java.util.HashMap;

/**
 * Similar to url node, but contains data about url
 */

public class FileData extends UrlNode {
    String mimeType;
    HashMap<String, String> metaData;

    public FileData(String url, String mimeType, HashMap<String, String> metaData) {
        super(url);
        this.mimeType = mimeType;
        this.metaData = metaData;
    }

    @Override
    public String getMimeType() {
        return mimeType;
    }

    @Override
    public String getMetaData() {
        // TODO: better representation

        return metaData.toString();
    }

    @Override
    public String toString() {
        return String.format("(%s)[mime: %s, metadata: %s]", url, mimeType, metaData.toString()); // Prints the first 20 chars of metadata
    }
}
