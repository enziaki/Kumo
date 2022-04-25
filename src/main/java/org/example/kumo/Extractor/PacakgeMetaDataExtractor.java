package org.example.kumo.Extractor;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pkg.PackageParser;
import org.apache.tika.sax.BodyContentHandler;
import org.example.kumo.model.FileData;
import org.example.kumo.model.UrlNode;

import java.io.InputStream;
import java.net.URL;


/**
 * Supports extraction from types like
 * tar,zip,jar etc.
 */
public class PacakgeMetaDataExtractor implements MetaDataExtractor {
    @Override
    public FileData extract(UrlNode targetNode) {
/*
        PackageParser parser = new PackageParser();
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();
        URL url = new URL(targetNode.getUrl());
        InputStream istream = url.openStream();


 */
        return null;
        // TODO: Fix beeg size
    }
}
