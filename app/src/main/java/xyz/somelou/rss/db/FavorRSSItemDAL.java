package xyz.somelou.rss.db;

import java.util.ArrayList;

import xyz.somelou.rss.bean.FavorRSSItem;

/**
 * @author somelou
 * @description favor_ssr_item表协作类接口
 * @date 2019-06-26
 */
public interface FavorRSSItemDAL {

    /**
     * 查询表favor_ssr_item全部内容
     * @param favorRSSItems 必须保证传进 Adapter 的数据 List 是同一个 List
     * @return rssUrlArrayList
     */
    ArrayList<FavorRSSItem> getAllData(ArrayList<FavorRSSItem> favorRSSItems);

    /**
     * 模糊查询|不实现
     * @param rssUrlArrayList 必须保证传进 Adapter 的数据 List 是同一个 List
     * @param query 输入的字符串
     * @return rssUrlArrayList
     */
    ArrayList<FavorRSSItem> getQueryData(ArrayList<FavorRSSItem> rssUrlArrayList, String query);

    /**
     * 查询favor_ssr_item某一项的内容
     * @param id 选中项的id
     * @return 一项item
     */
    FavorRSSItem getOneData(Integer id);

    /**
     * 判断是否收藏
     * @param itemUrl item的链接
     * @return true：收藏；false，未收藏
     */
    boolean isFavor(String itemUrl);

    /**
     * 收藏
     * @param url 该item的url
     * @param titleName 标题
     * @param description 描述
     * @return 是否添加成功
     */
    long insertOneData(String url,String titleName,String description);

    /**
     * 取消收藏
     * @param id 选中项的id
     * @return 是否删除成功
     */
    int deleteOneData(Integer id);

    /**
     * 取消收藏
     * @param itemUrl string
     * @return 是否删除成功
     */
    int deleteOneData(String itemUrl);

    int deleteAllData();

}
