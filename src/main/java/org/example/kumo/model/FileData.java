package org.example.kumo.model;

import java.util.HashMap;

/**
 * Similar to url node, but contains data about url
 */

public class FileData extends UrlNode {
    String mimeType;
    HashMap<String, String> metaData;

    FileData(String url, String mimeType, HashMap<String, String> metaData) {
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
}
