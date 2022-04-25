package org.example.kumo.utils;

import org.example.kumo.Exceptions.UnknownFileSizeException;

import java.net.HttpURLConnection;
import java.net.URL;

public class UrlUtils {

    /**
     *
     * @param url
     * @return content size in mb
     * @throws UnknownFileSizeException
     */
    public static int getFileSize(URL url) throws UnknownFileSizeException {

        long sizeInBytes = -1;

        try {
            var conn = url.openConnection();
            if(conn instanceof HttpURLConnection) {
                ((HttpURLConnection)conn).setRequestMethod("HEAD");
            }
            conn.getInputStream();
            sizeInBytes = conn.getContentLengthLong();
        }
        catch (Exception e) { // TODO: Better exception handling
            Log.error("Failed to get content length: %s ", e.toString());
        }
        /*
        if(sizeInBytes == -1)
            throw new UnknownFileSizeException("Failed calculating size for: " + url.toString());
         */
        return (int) sizeInBytes/100_00_00; // bytes to MB
    }
}
