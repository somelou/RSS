# RSS

## util类

### RSSUtil（RSS解析)

* 入参

  RSSUtil rssUtil=new RSSUtil("url");

  或

  RSSUtil rssUtil=new RSSUtil();

  rssUtil.setRssUrl("url");

* 获取参数

  1. 文章数：rssUtil.getFeedSize()
  2. RSS标题：rssUtil.getTitleName()
  3. RSS描述: rssUtil.getDescription()
  4. 文章项：rssUtil.getRssItemBeans()
  
## 数据库类

### SSRUrlDAL（RSS源表）

**SSRUrlDAL dal=new SSRUrlDALImpl();**

1. 查询所有
```java
/**
 * 查询表ssr_url全部内容
 * @param rssUrlArrayList 必须保证传进 Adapter 的数据 List 是同一个 List
 * @return rssUrlArrayList
 */
ArrayList<RSSUrl> getAllData(ArrayList<RSSUrl> rssUrlArrayList);
```

2. 获取所有已订阅内容

```java
ArrayList<RSSUrl> getSubscribe(ArrayList<RSSUrl> rssUrls);
```

2. 模糊查询
```java
/**
 * 模糊查询
 * @param rssUrlArrayList 必须保证传进 Adapter 的数据 List 是同一个 List
 * @param query 输入的字符串
 * @return rssUrlArrayList
 */
ArrayList<RSSUrl> getQueryData(ArrayList<RSSUrl> rssUrlArrayList, String query);
```
3. 查询ssr_url某一项的内容
```java
/**
 * 查询ssr_url某一项的内容
 * @param id 选中项的id
 * @return 一项item
 */
RSSUrl getOneData(Integer id);
```
4. 添加
```java
/**
 * 添加一个rss源
 * @param url rss url
 * @param groupName 组名，default=""
 * @param status 订阅状态，主动添加时default=SUBSCRIBED
 * @return 是否添加成功
 */
long insertOneData(String url, String groupName, SubscribeStatus status);
```
5. 添加
```java
/**
 * 删除某项
 * @param id 选中项的id
 * @return 一项item
 */
int deleteOneData(Integer id);
```
6. 更改标题名
```java
/**
 * 更改标题名
 * @param id 选中项的id
 * @param name 新的名字
 * @return 是否成功
 */
int updateName(Integer id, String name);
```
7. 更改组名
```java
/**
 * 更改组名
 * @param id 选中项的id
 * @param groupName 新的组名
 * @return 是否成功
 */
int updateGroupName(Integer id, String groupName);
```
8. 更改订阅状态
```java
/**
 * 更改订阅状态
 * @param id 选中项的id
 * @param status 订阅状态
 * @return
 */
int updateSubscribeStatus(Integer id,SubscribeStatus status);
```

### FavorRSSItemDAL(收藏表项)

**FavorRSSItemDAL dal=new FavorRSSItemDALImpl()**

1. 查询表favor_ssr_item全部内容
```
    /**
     * 查询表favor_ssr_item全部内容
     * @param favorRSSItems 必须保证传进 Adapter 的数据 List 是同一个 List
     * @return rssUrlArrayList
     */
    ArrayList<FavorRSSItem> getAllData(ArrayList<FavorRSSItem> favorRSSItems);
```
2. 模糊查询
```
    /**
     * 模糊查询|不实现
     * @param rssUrlArrayList 必须保证传进 Adapter 的数据 List 是同一个 List
     * @param query 输入的字符串
     * @return rssUrlArrayList
     */
    ArrayList<FavorRSSItem> getQueryData(ArrayList<FavorRSSItem> rssUrlArrayList, String query);
```
3. 查询favor_ssr_item某一项的内容
```
    /**
     * 查询favor_ssr_item某一项的内容
     * @param id 选中项的id
     * @return 一项item
     */
    FavorRSSItem getOneData(Integer id);
```
4. 收藏
```
    /**
     * 收藏
     * @param url 该item的url
     * @param titleName 标题
     * @param description 描述
     * @return 是否添加成功
     */
    long insertOneData(String url,String titleName,String description);
```
5. 取消收藏
```
    /**
     * 取消收藏
     * @param id 选中项的id
     * @return 一项item
     */
    int deleteOneData(Integer id);
```

## 请注意

1. RSS解析的值可能带有html标签；
2. 更换gradle版本；
3. 注意bean和数据库类参数的不同；
4. 注意RSS解析的时间格式和数据库中的时间格式。