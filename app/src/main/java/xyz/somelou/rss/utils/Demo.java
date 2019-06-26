package xyz.somelou.rss.utils;

import java.util.List;

import xyz.somelou.rss.bean.RSSItemBean;

/**
 * @author somelou
 * @description
 * @date 2019-06-25
 */
public class Demo {

    public static void main(String[] args){
        RSSUtil rssUtil=new RSSUtil("https://rsshub.app/zhihu/hotlist");
        List<RSSItemBean> rssItemBeans = rssUtil.getRssItemBeans();
        if (rssItemBeans != null && !rssItemBeans.isEmpty()){
            for (RSSItemBean item : rssItemBeans) {
                System.out.println(item.getTitle());
                System.out.println(item.getLink());
                System.out.println(item.getPubDate());
                System.out.println(item.getDescription());
            }
        }
    }
}
