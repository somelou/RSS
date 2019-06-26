package xyz.somelou.rss.utils;

/**
 * @author somelou
 * @description
 * @date 2019-06-25
 */

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.zip.GZIPInputStream;

import xyz.somelou.rss.bean.RSSItemBean;

/**
 * RSS解析工具类
 * @author somelou
 */
public class RSSUtil {

    // 数量
    private int feedSize;
    // uri
    private String uri;
    // title
    private String titleName;

    // description
    private String description;
    // item
    private List<RSSItemBean> rssItemBeans;

    public RSSUtil(){}

    public RSSUtil(String uri) {
        getRSSData(uri);
    }

    public void setRssUrl(String rssUrl) {
        getRSSData(rssUrl);
    }

    /**
     * 新线程
     */
    private void getRSSData(final String rssUrl) {
        final CountDownLatch cdl = new CountDownLatch(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                doParse(rssUrl);
                cdl.countDown();
            }
        }).start();
        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据不同种类的url执行不同函数
     *
     * @param rssUrl
     */
    private void doParse(String rssUrl) {
        if(!rssUrl.startsWith("https://")&&!rssUrl.startsWith("http://")){
            System.out.println("add https:// for url");
            rssUrl="https://"+rssUrl;
        }
        if (rssUrl.endsWith(".xml")) {
            try {
                parseFromXml(rssUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                parseFromUrl(rssUrl);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (FeedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 从以xml形式结尾的url中获取数据
     *
     * @param urlXml
     * @throws Exception
     */
    private void parseFromXml(String urlXml) throws Exception {
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed;
        ByteArrayInputStream inputStream = new ByteArrayInputStream(urlXml.getBytes(StandardCharsets.UTF_8));
        feed = input.build(new XmlReader(inputStream));

        setData(feed);
    }

    /**
     * 从普通url获取数据
     *
     * @param url
     * @throws IOException
     * @throws FeedException
     */
    private void parseFromUrl(String url) throws IOException, FeedException {
        SyndFeedInput input = new SyndFeedInput();
        URLConnection connection = new URL(url).openConnection();

        // 防止403
        connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        SyndFeed feed;
        String contentEncoding = connection.getHeaderField("Content-Encoding");
        if (contentEncoding != null && contentEncoding.contains("gzip")) {
            System.out.println("content encoding is gzip");
            GZIPInputStream gzipInputStream = new GZIPInputStream(connection.getInputStream());
            feed = input.build(new XmlReader(gzipInputStream));
        } else {
            feed = input.build(new XmlReader(connection.getInputStream()));
        }

        setData(feed);
    }

    private void setData(SyndFeed feed) {
        // title name
        titleName=feed.getTitle();
        // uri
        uri=feed.getUri();
        description=feed.getDescription();
        List entries = feed.getEntries();
        RSSItemBean item;
        rssItemBeans = new ArrayList<>();
        // size
        feedSize = entries.size();
        for (int i = 0; i < feedSize; i++) {
            SyndEntry entry = (SyndEntry) entries.get(i);
            item = new RSSItemBean();
            item.setTitle(entry.getTitle().trim());
            item.setType(feed.getTitleEx().getValue().trim());
            item.setUri(entry.getUri());
            item.setPubDate(entry.getPublishedDate());
            item.setAuthor(entry.getAuthor());
            // description过长时截取
            item.setDescription(ClearStringUtil.clearDescription(entry.getDescription().getValue()));
            item.setLink(entry.getLink());

            rssItemBeans.add(item);
            System.out.println(entry.getTitle());
        }
        feedSize = feed.getEntries().size();
        System.out.println("feed size:" + feedSize);
    }

    public int getFeedSize() {
        return feedSize;
    }

    public void setFeedSize(int feedSize) {
        this.feedSize = feedSize;
    }

    public List<RSSItemBean> getRssItemBeans() {
        return rssItemBeans;
    }


    public String getTitleName() {
        return titleName;
    }

    public String getUri() {
        return uri;
    }

    public String getDescription() {
        return description;
    }
}
