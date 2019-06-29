package xyz.somelou.rss.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import xyz.somelou.rss.bean.RSSUrl;
import xyz.somelou.rss.db.impl.RSSUrlDALImpl;

/*
 *  处理分组所需必要数据的工具类
 *  添加人：张霆伟
 */
public class SwitchGroupUtil {
    private RSSUrlDALImpl rssUrlDALImpl;
    private ArrayList<RSSUrl> rssUrlArrayList;
    private List<String> groupNames;
    private List<String> groupSortList;//将内容按照分组信息进行重组后的
    public SwitchGroupUtil(Context context){
        rssUrlDALImpl=new RSSUrlDALImpl(context);
        rssUrlArrayList=new ArrayList<>();
        rssUrlArrayList=rssUrlDALImpl.getAllData(rssUrlArrayList);
        gainAllGroupNames();
        groupSort();
    }

    private void gainAllGroupNames(){
        groupNames=new ArrayList<>();
        for (RSSUrl rssUrl:rssUrlArrayList) {
            if(groupNames.size()==0){
                groupNames.add(rssUrl.getGroupName());
            }else if(!groupNames.contains(rssUrl.getGroupName())){
                groupNames.add(rssUrl.getGroupName());
            }
        }
    }

    private void groupSort( ){
        groupSortList=new ArrayList<>();
        for(int i=0;i<groupNames.size();i++){
            groupSortList.add("分组："+groupNames.get(i));
            for(int j=0;j<rssUrlArrayList.size();j++){
                if(groupNames.get(i).equals(rssUrlArrayList.get(j).getGroupName())){
                    groupSortList.add(rssUrlArrayList.get(j).getName());
                }
            }
        }
    }

    public List<String> getGroupNames() {
        return groupNames;
    }

    public List<String> getGroupSortList() {
        return groupSortList;
    }
}
