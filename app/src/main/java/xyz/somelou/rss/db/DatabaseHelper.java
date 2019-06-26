package xyz.somelou.rss.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import xyz.somelou.rss.R;
import xyz.somelou.rss.utils.RSSUtil;

/**
 * @author somelou
 * @description 数据库协助类
 * @date 2019/6/25
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static String name = "rss.db";
    private static int dbDefaultVersion = 1;

    private String CREATE_SSR_URL_TABLE;
    private String CREATE_FAVOR_RSS_ITEM_TABLE;
    private String INSERT_RSS_URL;

    /**
     * 写一个这个类的构造函数，参数为上下文context，所谓上下文就是这个类所在包的路径
     * 指明上下文，数据库名，工厂默认空值，版本号默认从1开始
     * super(context,"db_test",null,1);
     * 把数据库设置成可写入状态，除非内存已满，那时候会自动设置为只读模式
     * db = getReadableDatabase();
     *
     * @param context Context
     */
    public DatabaseHelper(Context context) {
        super(context, name, null, dbDefaultVersion);

        // 从xml文件中获取string
        String TABLE_SSR_URL = context.getString(R.string.table_ssr_url);
        String TABLE_FAVOR_SSR_ITEM = context.getString(R.string.table_favor_ssr_item);
        String[] RSS_URL_ARRAY = context.getResources().getStringArray(R.array.ssr_url_array);
        int originalSize = RSS_URL_ARRAY.length;

        // 表1
        CREATE_SSR_URL_TABLE = "create table if not exists " + TABLE_SSR_URL + "("
                + "url_id integer(8) primary key NOT NULL ,"
                + "name TEXT,"
                + "url TEXT,"
                + "group_name TEXT,"
                + "status TEXT);";

        // 收藏item
        CREATE_FAVOR_RSS_ITEM_TABLE = "create table if not exists " + TABLE_FAVOR_SSR_ITEM + "("
                + "item_id integer(8) primary key,"
                + "url TEXT,"
                + "name TEXT,"
                + "description TEXT,"
                + "favor_time TEXT);";


        RSSUtil rssUtil = new RSSUtil(RSS_URL_ARRAY[0]);
        // 插入一些rss源
        INSERT_RSS_URL = "INSERT INTO favor_ssr_item SELECT '" + rssUtil.getTitleName() + "' AS 'name', '" + RSS_URL_ARRAY[0] + "' AS 'url' ";
        for (int i = 1; i < RSS_URL_ARRAY.length; i++) {
            rssUtil.setRssUrl(RSS_URL_ARRAY[i]);
            INSERT_RSS_URL += "UNION SELECT '" + rssUtil.getTitleName() + "','" + RSS_URL_ARRAY[i] + "' ";
        }

    }

    /**
     * 只在创建的时候使用一次
     * 重写两个必须要重写的方法，因为class DBOpenHelper extends SQLiteOpenHelper
     * 而这两个方法是 abstract 类 SQLiteOpenHelper 中声明的 abstract 方法
     *
     * @param db SQLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL(CREATE_FAVOR_RSS_ITEM_TABLE);
        //db = getWritableDatabase();

        db.execSQL(CREATE_SSR_URL_TABLE);
        db.execSQL(INSERT_RSS_URL);
        db.execSQL(CREATE_FAVOR_RSS_ITEM_TABLE);
        //Toasty.success(parent, "成功创建Database&Table," + db.getVersion(), Toast.LENGTH_SHORT)
        // .show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("old version:" + oldVersion + ",new version:" + newVersion);
    }


    @Override
    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }

}
