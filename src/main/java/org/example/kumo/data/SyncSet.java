package org.example.kumo.data;

import org.example.kumo.DataBase.DBInterface;
import org.example.kumo.model.UrlNode;

import java.util.Set;
import java.util.TreeSet;

/**
 * A thread-safe Set which interacts with the DB and inserts info
 */
public class SyncSet {
    private static final Set<UrlNode> graph = new TreeSet<>();

    private SyncSet() {}

    public synchronized static void add(UrlNode urlNode) {
        graph.add(urlNode);
        DBInterface.updateNode(urlNode);
    }

    public synchronized static void remove(UrlNode urlNode) {
        graph.remove(urlNode);
        DBInterface.removeNode(urlNode);
    }

    public synchronized static boolean contains(UrlNode urlNode) {
        return graph.contains(urlNode);
    }

    public synchronized static int size() {
        return graph.size();
    }
}
