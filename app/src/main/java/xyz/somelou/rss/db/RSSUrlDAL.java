package xyz.somelou.rss.db;

import java.util.ArrayList;

import xyz.somelou.rss.bean.RSSUrl;
import xyz.somelou.rss.enums.SubscribeStatus;

/**
 * @author somelou
 * @description SSR_URL表协作类接口
 * @date 2019-06-25
 */
public interface RSSUrlDAL {

    /**
     * 查询表ssr_url全部内容
     * @param rssUrlArrayList 必须保证传进 Adapter 的数据 List 是同一个 List
     * @return rssUrlArrayList
     */
    ArrayList<RSSUrl> getAllData(ArrayList<RSSUrl> rssUrlArrayList);

    /**
     * 获取所有已经订阅的RSS源
     * @param rssUrls 这里可能不需要这样，为保险起见
     * @return
     */
    ArrayList<RSSUrl> getSubscribe(ArrayList<RSSUrl> rssUrls);

    /**
     * 模糊查询
     * @param rssUrlArrayList 必须保证传进 Adapter 的数据 List 是同一个 List
     * @param query 输入的字符串
     * @return rssUrlArrayList
     */
    ArrayList<RSSUrl> getQueryData(ArrayList<RSSUrl> rssUrlArrayList, String query);

    /**
     * 查询ssr_url某一项的内容
     * @param id 选中项的id
     * @return 一项item
     */
    RSSUrl getOneData(Integer id);

    /**
     * 添加一个rss源
     * @param url rss url
     * @param groupName 组名，default=""
     * @param status 订阅状态，主动添加时default=SUBSCRIBED
     * @return 是否添加成功
     */
    long insertOneData(String url, String groupName, SubscribeStatus status);

    /**
     * 删除某项
     * @param id 选中项的id
     * @return 一项item
     */
    int deleteOneData(Integer id);

    /**
     * 更改标题名
     * @param id 选中项的id
     * @param name 新的名字
     * @return 是否成功
     */
    int updateName(Integer id, String name);

    /**
     * 更改组名
     * @param id 选中项的id
     * @param groupName 新的组名
     * @return 是否成功
     */
    int updateGroupName(Integer id, String groupName);

    /**
     * 更改订阅状态
     * @param id 选中项的id
     * @param status 订阅状态
     * @return
     */
    int updateSubscribeStatus(Integer id,SubscribeStatus status);

}
