package org.sdecima.rssreader;


public class RSSItem {

    private String guid;
    private String image;
	private String title;
	private String description;
	private String content;
	private String link;
	private String pubDate;

    public String getGuid() {
        return guid;
    }
    public void setGuid(String guid) {
        this.guid = guid;
    }
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
    public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getPubDate() {
		return pubDate;
	}
	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

    @Override
    public String toString() {
        return getTitle();
    }
}
