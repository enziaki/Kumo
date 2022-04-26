package org.example.kumo.Extractor;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.example.kumo.Exceptions.UnknownFileSizeException;
import org.example.kumo.model.FileData;
import org.example.kumo.model.UrlNode;
import org.example.kumo.utils.Log;
import org.example.kumo.utils.UrlUtils;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * The fallback default metadata extractor
 */
public class DefaultMetaDataExtractor implements MetaDataExtractor {

    @Override
    public FileData extract(UrlNode targetNode) {

        AutoDetectParser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler(-1);
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();
        URL url = null;
        InputStream urlStream = null;

        String mimeType = "";
        HashMap<String, String> fileMetadata = new HashMap<>();

        try {
            url = new URL(targetNode.getUrl());

            // Make sure it doesn't heapoverflow
            int sizeInMb = UrlUtils.getFileSize(url);
            int freeHeapInMb = (int) (Runtime.getRuntime().freeMemory() / 100_00_00);
            Log.info("Size of (%s): %d", targetNode.getUrl(), sizeInMb);

            if(sizeInMb > freeHeapInMb) {
                Log.info("File(%s) Exceeds run time size, skipping metadata processing(FreeHeapMem: %d, ContentSize: %d)",
                        targetNode.getUrl(), freeHeapInMb, sizeInMb);
            }

            urlStream = url.openStream();

            Tika tika = new Tika();
            mimeType = tika.detect(url);

            parser.parse(urlStream, handler, metadata, context);

            for(String mdName: metadata.names()) {
                fileMetadata.put(mdName, metadata.get(mdName));
            }

        } catch (MalformedURLException e) {
            Log.error("Invalid Url: %s " + e.getMessage(), targetNode.getUrl());
        } catch (IOException e) {
            Log.error("Exception(%s): " + e);
        } catch (TikaException e) {
            Log.error("Exception(%s): " + e);
        } catch (SAXException e) {
            Log.error("Exception(%s): " + e);
        } catch (UnknownFileSizeException e) {
            Log.info("Failed to calculate size for(%s) Skipping....", targetNode.getUrl());
        }

        return new FileData(targetNode.getUrl(), mimeType, fileMetadata);
    }
}
