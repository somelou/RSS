package xyz.somelou.rss.enums;

/**
 * @author somelou
 * @description 订阅状态
 * @date 2019-06-25
 */
public enum SubscribeStatus {

    SUBSCRIBED("订阅"),
    NO_SUBSCRIBE("未订阅");

    // 中文描述
    private String desc;

    /**
     * 私有构造函数，防止被外部调用
     * @param desc
     */
    private SubscribeStatus(String desc) {
        this.desc=desc;
    }

    /**
     * 定义方法,返回描述,跟常规类的定义没区别
     * @return
     */
    public String getDesc(){
        return desc;
    }
}
