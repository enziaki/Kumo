package org.example.kumo.Extractor;

import org.example.kumo.model.FileData;
import org.example.kumo.model.UrlNode;

public interface MetaDataExtractor {
    public FileData extract(UrlNode targetNode);
}
