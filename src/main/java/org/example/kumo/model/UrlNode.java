package org.example.kumo.model;

public class UrlNode implements Comparable {
    String url;

    public UrlNode(String url) {
        this.url = url;
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;

        if(this == obj)
            return true;

        if(getClass() != obj.getClass())
            return false;

        return this.url.equals(((UrlNode) obj).url);
    }

    @Override
    public String toString() {
        return String.format("UrlNode{%s}", url);
    }

    @Override
    public int compareTo(Object o) {
        return url.compareTo(((UrlNode) o).url);
    }

    public String getUrl() {
        return url;
    }

    public String getMimeType() {
        return "";
    }

    public String getMetaData() {
        return "";
    }
}
