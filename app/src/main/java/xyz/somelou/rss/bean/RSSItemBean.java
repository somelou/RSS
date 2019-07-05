package xyz.somelou.rss.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @author somelou
 * @description RSS item
 * @date 2019-06-25
 */
public class RSSItemBean implements Serializable{

    // 标题
    private String title;
    // 发布者
    private String author;

    // 网站或栏目的URL地址
    private String uri;

    // 详情链接
    private String link;

    // 网站或栏目的简要介绍
    private String description;
    // 时间
    private Date pubDate;
    private String type;

    public RSSItemBean(){
        super();
    }

    public RSSItemBean(String t,String a,String u,String l,String d,Date date,String ty){
        title=t;author=a;uri=u;link=l;description=d;pubDate=date;type=ty;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
