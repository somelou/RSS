package xyz.somelou.rss.utils;

/**
 * @author somelou
 * @description
 * @date 2019-06-26
 */
public class ClearStringUtil {

    /**
     * 暂时只清洗过长
     * @param description 描述
     * @return
     */
    public static String clearDescription(String description){
        // description过长时截取
        if(description.length()>100){
            description.substring(0,100);
        }
        return description;
    }
}
