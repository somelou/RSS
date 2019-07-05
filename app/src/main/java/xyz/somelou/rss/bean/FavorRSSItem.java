package xyz.somelou.rss.bean;

/**
 * @author somelou
 * @description 收藏item
 * @date 2019-06-25
 */
public class FavorRSSItem {

    private int id;
    private String itemUrl; // 此item的url
    private String titleName;
    private String description;
    private String favorTime;// 收藏时间

    public FavorRSSItem(int id,String itemUrl,String titleName,String description,String favorTime){
        this.id=id;
        this.titleName =titleName;
        this.description=description;
        this.itemUrl=itemUrl;
        this.favorTime=favorTime;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemUrl() {
        return itemUrl;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFavorTime() {
        return favorTime;
    }

    public void setFavorTime(String favorTime) {
        this.favorTime = favorTime;
    }
}
