package org.sdecima.rssreader;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jsoup.*;

public class RSSFeedReader {


    public static void read(String url, RSSFeedStore store) {
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser saxParser = spf.newSAXParser();

            RSSFeedSaxHandler handler = new RSSFeedSaxHandler(store);
            Connection.Response html = Jsoup.connect(url)
                    .ignoreContentType(true)
                    .followRedirects(false)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/604.5.6 (KHTML, like Gecko) Version/11.0.3 Safari/604.5.6")
                    .execute().charset("utf-8");
            saxParser.parse(html.bodyStream(), handler);

        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

}
