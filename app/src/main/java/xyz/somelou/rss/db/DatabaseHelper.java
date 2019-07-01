package xyz.somelou.rss.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author somelou
 * @description 数据库协助类
 * @date 2019/6/25
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static String name = "rss.db";
    private static int dbDefaultVersion = 1;

    // 本来想从xml中直接获取table名，但会让onCreate()失效
    // 表1
    private final static String CREATE_SSR_URL_TABLE = "create table if not exists RSS_URL ("
            + "url_id integer primary key autoincrement,"
            + "name TEXT,"
            + "url TEXT,"
            + "group_name TEXT,"
            + "status TEXT,"
            + "count integer);";//增加文章数

    // 收藏item
    private final static String CREATE_FAVOR_RSS_ITEM_TABLE = "create table if not exists FAVOR_RSS_ITEM ("
            + "item_id integer primary key autoincrement,"
            + "url TEXT,"
            + "name TEXT,"
            + "description TEXT,"
            + "favor_time TEXT);";

    /**
     * 写一个这个类的构造函数，参数为上下文context，所谓上下文就是这个类所在包的路径
     * 指明上下文，数据库名，工厂默认空值，版本号默认从1开始
     * super(context,"db_test",null,1);
     * 把数据库设置成可写入状态，除非内存已满，那时候会自动设置为只读模式
     *
     * @param context Context
     */
    public DatabaseHelper(Context context) {
        super(context, name, null, dbDefaultVersion);
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

        db.execSQL(CREATE_SSR_URL_TABLE);
        db.execSQL(CREATE_FAVOR_RSS_ITEM_TABLE);
        // System.out.println("成功创建Database&Table");
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
