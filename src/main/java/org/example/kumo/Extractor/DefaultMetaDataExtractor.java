package org.example.kumo.Extractor;

import org.example.kumo.model.FileData;
import org.example.kumo.model.UrlNode;

/**
 * The fallback default metadata extractor
 */
public class DefaultMetaDataExtractor implements MetaDataExtractor {

    @Override
    public FileData extract(UrlNode targetNode) {
        return null;
    }
}
