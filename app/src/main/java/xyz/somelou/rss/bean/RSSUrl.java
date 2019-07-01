package xyz.somelou.rss.bean;

import xyz.somelou.rss.enums.SubscribeStatus;

/**
 * @author somelou
 * @description rss源
 * @date 2019-06-25
 */
public class RSSUrl {

    private int id;
    private String url;
    private String name;
    private String groupName;
    // 是否订阅
    private SubscribeStatus status;
    private int count;//文章数

    public RSSUrl(){}

    public RSSUrl(String url,String name,String groupName,String status){
        this.url=url;
        this.name=name;
        this.groupName=groupName;
        SubscribeStatus ss=SubscribeStatus.NO_SUBSCRIBE;
        if(status.equals(SubscribeStatus.SUBSCRIBED.toString())){
            ss=SubscribeStatus.SUBSCRIBED;
        }
        this.status=ss;
    }

    public RSSUrl(int id,String url,String name,String groupName,String status){
        this.id=id;
        this.url=url;
        this.name=name;
        this.groupName=groupName;
        SubscribeStatus ss=SubscribeStatus.NO_SUBSCRIBE;
        if(status.equals(SubscribeStatus.SUBSCRIBED.toString())){
            ss=SubscribeStatus.SUBSCRIBED;
        }
        this.status=ss;
    }
    public RSSUrl(int id,String url,String name,String groupName,String status,int count){
        this.id=id;
        this.url=url;
        this.name=name;
        this.groupName=groupName;
        SubscribeStatus ss=SubscribeStatus.NO_SUBSCRIBE;
        if(status.equals(SubscribeStatus.SUBSCRIBED.toString())){
            ss=SubscribeStatus.SUBSCRIBED;
        }
        this.status=ss;
        this.count=count;
    }


    public int getId() {
        return id;
    }

    public SubscribeStatus getStatus() {
        return status;
    }

    public void setStatus(SubscribeStatus status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
