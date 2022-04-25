package org.example.kumo.Extractor;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.example.kumo.model.FileData;
import org.example.kumo.model.UrlNode;
import org.example.kumo.utils.Log;
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
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();
        URL url = null;
        InputStream urlStream = null;

        String mimeType = "";
        HashMap<String, String> fileMetadata = new HashMap<>();

        try {
            url = new URL(targetNode.getUrl());
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
            throw new RuntimeException(e);
        } catch (TikaException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            // TODO: Fix beeg size error
            throw new RuntimeException(e);
        }

        return new FileData(targetNode.getUrl(), mimeType, fileMetadata);
    }
}
