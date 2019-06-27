package xyz.somelou.rss.thread;

import android.content.Context;

import xyz.somelou.rss.db.impl.BaseDALImpl;

/**
 * @author somelou
 * @description 预加载数据库
 * @date 2019-06-27
 */
public class PreDatabaseThread implements Runnable{

    private Context context;

    public PreDatabaseThread(Context context){
        this.context=context;
    }

    @Override
    public void run() {
        new BaseDALImpl(context).preInsertRssUrl();
    }
}
