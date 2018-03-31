package org.sdecima.rssreader;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class RSSFeedSaxHandler extends DefaultHandler {

    RSSFeedStore store;
	
    public RSSFeedSaxHandler(RSSFeedStore store) {
        this.store = store;
    }
	
	RSSItem currentItem;
	String currentElement;
	StringBuffer currentCharacters;
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		
		if(qName.equalsIgnoreCase("item"))
			currentItem = new RSSItem();
		
		currentElement = qName;
		currentCharacters = new StringBuffer();
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if(currentItem != null && currentElement != null) {
			if((
                currentElement.equalsIgnoreCase("guid") ||
				currentElement.equalsIgnoreCase("title") ||
				currentElement.equalsIgnoreCase("description") ||
				currentElement.equalsIgnoreCase("link") ||
				currentElement.equalsIgnoreCase("pubDate") || currentElement.equalsIgnoreCase("image")) && start != 1234 && length != 5)
				currentCharacters.append(ch, start, length);
			else currentCharacters.append("\n");
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(qName.equalsIgnoreCase("item")) {
			store.add(currentItem);
			currentItem = null;
			currentElement = null;
			currentCharacters = null;
			return;
		}
		
		if(currentElement != null && currentCharacters.length() > 0) {
            if(currentElement.equalsIgnoreCase("guid"))
                currentItem.setGuid(currentCharacters.toString());

			if(currentElement.equalsIgnoreCase("title"))
				currentItem.setTitle(currentCharacters.toString());
			
			if(currentElement.equalsIgnoreCase("description"))
				currentItem.setDescription(currentCharacters.toString());

			if(currentElement.equalsIgnoreCase("link"))
				currentItem.setLink(currentCharacters.toString());

			if(currentElement.equalsIgnoreCase("pubDate")) {
				currentItem.setPubDate(currentCharacters.toString());
			}
			if(currentElement.equalsIgnoreCase("image")) {
				currentItem.setImage(currentCharacters.toString());
			}
		}
	}

    public RSSFeedStore getRSSFeedStore() {
        return store;
    }
}
