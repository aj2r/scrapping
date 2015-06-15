package com.aj.scraping.util.html;

public interface Content {

    String getContentAsString();

    String getContentType();

    String getCharsetName();

    int getResponseCode();

}
